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
            throw new Exception("Erro ao buscar clientes: ");
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
            if (response.body() != null && !response.body().isEmpty()) {
                // Se o corpo não estiver vazio, desserializar e retornar o cliente
                return objectMapper.readValue(response.body(), Clientes.class);
            } else {
                // Se o corpo estiver vazio, apenas retornar o cliente que foi enviado
                System.out.println("Cliente criado com sucesso, mas sem dados retornados.");
                return clientes;
            }
        } else if (response.statusCode() == 400) {
            System.out.println("Erro ao criar cliente: " + response.body());
            throw new Exception("Erro ao criar cliente: ");
        } else {
            System.out.println("Server response: " + response.body());
            throw new Exception("Erro ao criar cliente: ");
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

        if (response.statusCode() == 200 || response.statusCode() == 204) {
        if (response.body().isEmpty()) {
            return cliente; // Retorna o cliente sem alterações se não houver corpo na resposta
        } else {
            try {
                return objectMapper.readValue(response.body(), Clientes.class); // Tenta converter a resposta para o objeto Cliente
            } catch (IOException e) {
                throw new Exception("Erro ao processar a resposta do servidor: ");
            }
        }
    } else {
        throw new Exception("Erro ao editar cliente: ");
    }
    }
    public static Clientes inativarCliente(Clientes cliente, String token) throws Exception {
        cliente.setActive(false);
    
        String clienteJson = objectMapper.writeValueAsString(cliente);
        String urlComId = BASE_URL + "/" + cliente.getId();
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlComId))
                .PUT(HttpRequest.BodyPublishers.ofString(clienteJson)) // Envia a atualização do cliente
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();
    
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    
        if (response.statusCode() == 200 || response.statusCode() == 204) {
            if (response.body().isEmpty()) {
                return cliente;
            } else {
                try {
                    return objectMapper.readValue(response.body(), Clientes.class);
                } catch (IOException e) {
                    throw new Exception("Erro ao processar a resposta do servidor: ");
                }
            }
        } else {
            throw new Exception("Erro ao inativar cliente: ");
        }
    }
    
}