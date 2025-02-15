package edu.pies.sysaguaapp.services;

import edu.pies.sysaguaapp.models.ProductLine;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ProductLineService {
    private static final String BASE_URL = "http://localhost:8080/products/line";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ProductLineService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public List<ProductLine> buscarLinhas() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<ProductLine>>() {});
        } else {
            throw new Exception("Erro ao buscar linhas: " + response.body());
        }
    }
}
