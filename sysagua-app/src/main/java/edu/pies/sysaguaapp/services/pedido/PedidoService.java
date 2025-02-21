package edu.pies.sysaguaapp.services.pedido;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.pies.sysaguaapp.dtos.compra.SendPgtoCompraDto;
import edu.pies.sysaguaapp.dtos.pedido.SendPedidoDto;
import edu.pies.sysaguaapp.dtos.pedido.SendPgtoPedidoDto;
import edu.pies.sysaguaapp.enumeration.Pedidos.PedidoStatus;
import edu.pies.sysaguaapp.models.Pedido.Pedido;
import edu.pies.sysaguaapp.models.compras.Compra;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class PedidoService {
    private static final String BASE_URL = "http://localhost:8080/orders";
    private static HttpClient httpClient;
    private static ObjectMapper objectMapper;

    public PedidoService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }
    public List<Pedido> buscarPedidos(String token) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<Pedido>>() {
            });
        } else {
            throw new Exception("Erro ao buscar pedidos: ");
        }
    }

    public Pedido criarPedido(SendPedidoDto pedidos, String token) throws Exception {
        String pedidoJson = objectMapper.writeValueAsString(pedidos);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .POST(HttpRequest.BodyPublishers.ofString(pedidoJson))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        // Enviar a requisição
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 201) {
            return null;
        } else {
            throw new Exception("Erro ao cadastrar: " + response.body());
        }
    }

    public static Pedido cancelarPedido(Pedido pedido, String token) throws Exception {

        if (pedido.getReceivedAmount().compareTo(pedido.getTotalAmount()) > 0 ) {
            throw new Exception("O valor recebido ultrapassa o valor total do pedido.");
        }

        if (pedido.getDeliveryStatus() == PedidoStatus.CANCELED) {
            throw new Exception("O pedido já foi cancelado.");
        }

        pedido.setDeliveryStatus(PedidoStatus.CANCELED);
        ObjectMapper mapper = new ObjectMapper();


        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String jsonBody = mapper.writeValueAsString(pedido);
        String urlComId = BASE_URL + "/" + pedido.getId();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlComId))
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 204) {
            return pedido;
        } else {
            throw new Exception("Erro ao cancelar: " + response.body());
        }
    }

    public Pedido buscarPedidoId(Long id, String token) throws Exception {
        String urlComId = BASE_URL + "?id=" + id;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlComId))
                .GET()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            if (response.body().isEmpty()) {
                return null;
            }
            List<Pedido> pedidos = objectMapper.readValue(response.body(), new TypeReference<List<Pedido>>() {});
            if (!pedidos.isEmpty()) {
                return pedidos.get(0);
            }
            return null;
        } else {
            throw new Exception("Erro ao buscar pedido: " + response.body());
        }
    }

    public Pedido cadastrarPagamento(SendPgtoPedidoDto pagamento, Long idPedido, String token) throws Exception {
        String pedidoJson = objectMapper.writeValueAsString(pagamento);
        String urlPagamento = BASE_URL + "/" + idPedido;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlPagamento))
                .PUT(HttpRequest.BodyPublishers.ofString(pedidoJson))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 204) {
            return null;
        } else {
            throw new Exception("Erro ao cadastrar pagamento: " + response.body());
        }
    }

}