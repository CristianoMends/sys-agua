package edu.pies.sysaguaapp.services.pedido;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.pies.sysaguaapp.models.Pedido.TransactionPedido;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class TransactionPedidoService {
    private static final String BASE_URL = "http://localhost:8080/transactions";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public TransactionPedidoService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public List<TransactionPedido> buscarTransacoes(String token) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<TransactionPedido>>() {
            });
        } else {
            throw new Exception("Erro ao buscar: " + response.body());
        }
    }

}
