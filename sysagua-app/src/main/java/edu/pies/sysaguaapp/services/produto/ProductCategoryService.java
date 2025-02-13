package edu.pies.sysaguaapp.services.produto;

import edu.pies.sysaguaapp.models.ProductCategory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ProductCategoryService {
    private static final String BASE_URL = "http://localhost:8080/products/category";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ProductCategoryService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public List<ProductCategory> buscarCategorias() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<ProductCategory>>() {});
        } else {
            throw new Exception("Erro ao buscar categorias: " + response.body());
        }
    }
}
