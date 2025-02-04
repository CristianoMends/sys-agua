package edu.pies.sysaguaapp.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.pies.sysaguaapp.dtos.UpdateFornecedorDto;
import edu.pies.sysaguaapp.models.Fornecedor;
import edu.pies.sysaguaapp.models.Produto;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class FornecedorService {
    private static final String BASE_URL = "http://localhost:8080/suppliers";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public FornecedorService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public List<Fornecedor> buscarFornecedores(String token) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<Fornecedor>>() {
            });
        } else {
            throw new Exception("Erro ao buscar: " + response.body());
        }
    }

    public Fornecedor cadastrarFornecedor(Fornecedor fornecedor, String token) throws Exception {
        // Converter o objeto Fornecedor para JSON
        String fornecedorJson = objectMapper.writeValueAsString(fornecedor);
        System.out.println(fornecedorJson);

        // Criar a requisição POST
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .POST(HttpRequest.BodyPublishers.ofString(fornecedorJson))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        System.out.println(request);
        // Enviar a requisição
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Verificar a resposta
        if (response.statusCode() == 201) {
            return null;
        } else {
            throw new Exception("Erro ao cadastrar: " + response.body());
        }
    }

    public Fornecedor atualizarFornecedor(Fornecedor fornecedor, String token) throws Exception {

        UpdateFornecedorDto fornecedorDto = new UpdateFornecedorDto(fornecedor);
        String fornecedorJson = objectMapper.writeValueAsString(fornecedorDto);
        String urlFornecedor = BASE_URL + "/" + fornecedor.getId();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlFornecedor))
                .PUT(HttpRequest.BodyPublishers.ofString(fornecedorJson))
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

    public Fornecedor buscarFornecedorId(String id, String token) throws Exception {
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
            List<Fornecedor> fornecedores = objectMapper.readValue(response.body(), new TypeReference<List<Fornecedor>>() {});
            if (!fornecedores.isEmpty()) {
                return fornecedores.get(0);
            }
            return null;
        } else {
            throw new Exception("Erro ao buscar fornecedor: " + response.body());
        }
    }
}
