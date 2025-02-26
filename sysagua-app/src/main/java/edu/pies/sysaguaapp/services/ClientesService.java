package edu.pies.sysaguaapp.services;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.pies.sysaguaapp.models.Clientes;
import java.io.IOException;
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
            throw new Exception("Erro ao buscar: " + response.body());
        }
    }

    public static Clientes criarCliente(Clientes clientes, String token) throws Exception {
        String clienteJson = objectMapper.writeValueAsString(clientes);

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
            return null;
        } else {
            throw new Exception("Erro ao cadastrar: " + response.body());
        }
    }
    public static Clientes editarCliente(Clientes cliente, String token) throws Exception {
        String clienteJson = objectMapper.writeValueAsString(cliente);
        String urlComId = BASE_URL + "/" + cliente.getId();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlComId))
                .PUT(HttpRequest.BodyPublishers.ofString(clienteJson))
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
    public static Clientes inativarCliente(Clientes cliente, String token) throws Exception {
        String urlComId = BASE_URL + "/" + cliente.getId();
        
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
            throw new Exception("Erro ao deletar: " + response.body());
        }
    }

    public Clientes buscarClienteId(String id, String token) throws Exception {
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
            List<Clientes> clientes = objectMapper.readValue(response.body(), new TypeReference<List<Clientes>>() {});
            if (!clientes.isEmpty()) {
                return clientes.get(0);
            }
            return null;
        } else {
            throw new Exception("Erro ao buscar cliente: " + response.body());
        }
    }

    
}