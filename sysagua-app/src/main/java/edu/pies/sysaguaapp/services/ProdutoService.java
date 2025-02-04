package edu.pies.sysaguaapp.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.pies.sysaguaapp.models.Produto;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ProdutoService {
    private static final String BASE_URL = "http://localhost:8080/products";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ProdutoService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public List<Produto> buscarProdutos(String token) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            if (response.body().isEmpty()) {
                return List.of();
            }
            return objectMapper.readValue(response.body(), new TypeReference<List<Produto>>() {});
        } else {
            throw new Exception("Erro ao buscar produtos: " + response.body());
        }
    }

    public Produto criarProduto(Produto produto, String token) throws Exception {
        // Converter o objeto Produto para JSON
        String produtoJson = objectMapper.writeValueAsString(produto);
        System.out.println(produtoJson);

        // Criar a requisição POST
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .POST(HttpRequest.BodyPublishers.ofString(produtoJson))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        // Enviar a requisição
        System.out.println(request);
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        System.out.println(response.body());

        // Verificar a resposta
        if (response.statusCode() == 201) {
            return null;
        } else {
            throw new Exception("Erro ao criar produto: " + response.body());
        }
    }

    public Produto editarProduto(Produto produto, String token) throws Exception {
        String produtoJson = objectMapper.writeValueAsString(produto);
        String urlComId = BASE_URL + "/" + produto.getId();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlComId))
                .PUT(HttpRequest.BodyPublishers.ofString(produtoJson))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200 || response.statusCode() == 204) {
            if (response.body().isEmpty()) {
                return produto;
            } else {
                return objectMapper.readValue(response.body(), Produto.class);
            }
        } else {
            throw new Exception("Erro ao editar produto: " + response.body());
        }
    }

    public Produto buscarProdutoId(String id, String token) throws Exception {
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
            return objectMapper.readValue(response.body(), Produto.class);
        } else {
            throw new Exception("Erro ao buscar produto: " + response.body());
        }
    }

    public Produto inativarProduto(Produto produto, String token) throws Exception {
        String urlComId = BASE_URL + "/" + produto.getId();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlComId))
                .DELETE()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 204) {
            return null;
        } else {
            throw new Exception("Erro ao inativar produto: " + response.body());
        }
    }
}
