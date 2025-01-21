package edu.pies.sysaguaapp.services;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.pies.sysaguaapp.models.Clientes;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;


public class ClientesService {
    private static final String BASE_URL = "http://localhost:8080/customers";
    private static HttpClient httpClient;
    private static ObjectMapper objectMapper;

    public ClientesService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }
    public List<Clientes> buscarClientes(String token) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<Clientes>>() {
            });
        } else {
            throw new Exception("Erro ao buscar clientes: " + response.body());
        }
    }

    public static Clientes criarCliente(Clientes clientes, String token) throws Exception {
        String clienteJson = objectMapper.writeValueAsString(clientes);
        System.out.println("Sending client data: " + clienteJson);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .POST(HttpRequest.BodyPublishers.ofString(clienteJson))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        // Enviar a requisição
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Verificar a resposta
        if (response.statusCode() == 201) {
            return objectMapper.readValue(response.body(), Clientes.class);
        } else if (response.statusCode() == 400) {
            System.out.println("Erro ao criar cliente: " + response.body());
        } else {
            System.out.println("Server response: " + response.body());
            throw new Exception("Erro ao criar cliente: " + response.body());
        }
        return clientes;
    }
    public static Clientes editarCliente(Long id, Clientes clienteAtualizado) throws Exception {
        String token = TokenManager.getInstance().getToken();
        if (token == null || token.isEmpty()) {
            throw new Exception("Token não encontrado. Por favor, faça login.");
        }

        String clienteJson = objectMapper.writeValueAsString(clienteAtualizado);

        // Criar a requisição PUT
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .PUT(HttpRequest.BodyPublishers.ofString(clienteJson))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        // Enviar a requisição
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Verificar a resposta
        if (response.statusCode() == 204) {
            return clienteAtualizado;
        } else if (response.statusCode() == 404) {
            throw new Exception("Cliente não encontrado: " + response.body());
        }
        else {
            throw new Exception("Erro ao editar cliente: " + response.body());
        }
    }

    public Clientes buscarClientePorId(Long id, String token) throws Exception {
        String url = BASE_URL + "/" + id;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), Clientes.class);
        } else {
            throw new Exception("Erro ao buscar cliente: " + response.body());
        }
    }

    public static void excluirCliente(Long id) throws Exception {
        String token = TokenManager.getInstance().getToken();
        if (token == null || token.isEmpty()) {
            throw new Exception("Token não encontrado. Por favor, faça login.");
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .DELETE()
                .header("Authorization", "Bearer " + token)
                .build();

        // Enviar a requisição
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Verificar a resposta
        if (response.statusCode() == 200 || response.statusCode() == 204) {
            System.out.println("Cliente excluído com sucesso.");
        } else {
            throw new Exception("Erro ao excluir cliente: " + response.body());
        }
    }
}