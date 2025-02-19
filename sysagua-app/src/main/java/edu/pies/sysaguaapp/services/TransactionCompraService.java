package edu.pies.sysaguaapp.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.pies.sysaguaapp.models.Transaction;
import edu.pies.sysaguaapp.models.TransactionCompra;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class TransactionCompraService {
    private static final String BASE_URL = "http://localhost:8080/transactions";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public TransactionCompraService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public List<Transaction> buscarTransacoes(String token) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<Transaction>>() {
            });
        } else {
            throw new Exception("Erro ao buscar: " + response.body());
        }
    }

    public List<TransactionCompra> buscarTransacaoCompraId(Long id, String token) throws Exception {
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
            List<TransactionCompra> transacoes = objectMapper.readValue(response.body(), new TypeReference<List<TransactionCompra>>() {});
            if (!transacoes.isEmpty()) {
                return transacoes;
            }
            return null;
        } else {
            throw new Exception("Erro ao buscar pagamentos: " + response.body());
        }
    }

    public Transaction buscarTransacaoId(Long id, String token) throws Exception {
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
            List<Transaction> transacao = objectMapper.readValue(response.body(), new TypeReference<List<Transaction>>() {});
            if (!transacao.isEmpty()) {
                return transacao.get(0);
            }
            return null;
        } else {
            throw new Exception("Erro ao buscar pagamento: " + response.body());
        }
    }
}
