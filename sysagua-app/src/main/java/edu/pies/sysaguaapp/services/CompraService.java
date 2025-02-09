package edu.pies.sysaguaapp.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.pies.sysaguaapp.dtos.compra.SendCompraDto;
import edu.pies.sysaguaapp.models.compras.Compra;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class CompraService {
    private static final String BASE_URL = "http://localhost:8080/purchases";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public CompraService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public List<Compra> buscarCompras(String token) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<Compra>>() {
            });
        } else {
            throw new Exception("Erro ao buscar: " + response.body());
        }
    }

    public Compra cadastrarCompra(SendCompraDto compra, String token) throws Exception {
        String compraJson = objectMapper.writeValueAsString(compra);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .POST(HttpRequest.BodyPublishers.ofString(compraJson))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 201) {
            return null;
        } else {
            throw new Exception("Erro ao cadastrar: " + response.body());
        }
    }

    public Compra atualizarCompra(Compra compra, String token) throws Exception {
        String compraJson = objectMapper.writeValueAsString(compra);
        String urlCompra = BASE_URL + "/" + compra.getId();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlCompra))
                .PUT(HttpRequest.BodyPublishers.ofString(compraJson))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 204) {
            return null;
        } else {
            throw new Exception("Erro ao atualizar: " + response.body());
        }
    }

    public Compra buscarCompraId(String id, String token) throws Exception {
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
            List<Compra> compras = objectMapper.readValue(response.body(), new TypeReference<List<Compra>>() {});
            if (!compras.isEmpty()) {
                return compras.get(0);
            }
            return null;
        } else {
            throw new Exception("Erro ao buscar fornecedor: " + response.body());
        }
    }

    public Compra cancelarCompra(Compra compra, String token) throws Exception {
        String urlCompra = BASE_URL + "/" + compra.getId();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlCompra))
                .DELETE()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 204) {
            return null;
        } else {
            throw new Exception("Erro ao deletar: " + response.body());
        }
    }
}
