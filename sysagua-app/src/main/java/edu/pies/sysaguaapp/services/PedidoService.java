package edu.pies.sysaguaapp.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.pies.sysaguaapp.dtos.pedido.SendPedidoDto;
import edu.pies.sysaguaapp.models.Pedido.Pedido;
import java.io.IOException;
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
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
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
        pedido.setActive(false);

        String pedidoJson = objectMapper.writeValueAsString(pedido);
        String urlComId = BASE_URL + "/" + pedido.getId();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlComId))
                .PUT(HttpRequest.BodyPublishers.ofString(pedidoJson))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200 || response.statusCode() == 204) {
            if (response.body().isEmpty()) {
                return pedido;
            } else {
                try {
                    return objectMapper.readValue(response.body(), Pedido.class);
                } catch (IOException e) {
                    throw new Exception("Erro ao processar a resposta do servidor: ");
                }
            }
        } else {
            throw new Exception("Erro ao inativar pedido: ");
        }
    }

}