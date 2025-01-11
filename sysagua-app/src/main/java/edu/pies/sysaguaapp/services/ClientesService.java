package edu.pies.sysaguaapp.services;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.pies.sysaguaapp.models.ClientesCadastro;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;


public class ClientesService {
    private static final String BASE_URL = "http://localhost:8080/clients";
    private static HttpClient httpClient;
    private static ObjectMapper objectMapper;

    public ClientesService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public List<ClientesCadastro> buscarClientes(String token) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<ClientesCadastro>>() {
            });
        } else {
            throw new Exception("Erro ao buscar clientes: " + response.body());
        }
    }

    public static ClientesCadastro criarCliente(ClientesCadastro clientes, String token) throws Exception {
        // Converter o objeto Produto para JSON
        String produtoJson = objectMapper.writeValueAsString(clientes);

        // Criar a requisição POST
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .POST(HttpRequest.BodyPublishers.ofString(produtoJson))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        // Enviar a requisição
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Verificar a resposta
        if (response.statusCode() == 201) {
            return null;
        } else {
            throw new Exception("Erro ao criar cliente: " + response.body());
        }
    }
}
