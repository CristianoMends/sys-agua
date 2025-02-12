package edu.pies.sysaguaapp.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.pies.sysaguaapp.models.Pedido.Pedido;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class PedidoService {
    private static final String BASE_URL = "http://localhost:8080/orders";
    private static HttpClient httpClient;
    private static ObjectMapper objectMapper;

    public PedidoService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }
    public List<Pedido> buscarPedidos(String token) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<Pedido>>() {
            });
        } else {
            throw new Exception("Erro ao buscar pedidos: ");
        }
    }

    public static Pedido criarPedido(Pedido pedidos, String token) throws Exception {
        String pedidoJson = objectMapper.writeValueAsString(pedidos);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .POST(HttpRequest.BodyPublishers.ofString(pedidoJson))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        // Enviar a requisição
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Verificar a resposta
        if (response.statusCode() == 201) {
            if (response.body() != null && !response.body().isEmpty()) {
                // Se o corpo não estiver vazio, desserializar e retornar o cliente
                return objectMapper.readValue(response.body(), Pedido.class);
            } else {
                // Se o corpo estiver vazio, apenas retornar o cliente que foi enviado
                System.out.println("pedido criado com sucesso, mas sem dados retornados.");
                return pedidos;
            }
        } else if (response.statusCode() == 400) {
            System.out.println("Erro ao criar pedido: " + response.body());
            throw new Exception("Erro ao criar pedido: ");
        } else {
            System.out.println("Server response: " + response.body());
            throw new Exception("Erro ao criar pedido: ");
        }
    }
    public static Pedido editarPedido(Pedido pedido, String token) throws Exception {
        String pedidoJson = objectMapper.writeValueAsString(pedido);
        String urlComId = BASE_URL + "/" + pedido.getId();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlComId))
                .PUT(HttpRequest.BodyPublishers.ofString(pedidoJson))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200 || response.statusCode() == 204) {
            if (response.body().isEmpty()) {
                return pedido;
            } else {
                try {
                    return objectMapper.readValue(response.body(), Pedido.class);
                } catch (IOException e) {
                    throw new Exception("Erro ao processar a resposta do servidor: ");
                }
            }
        } else {
            throw new Exception("Erro ao editar pedido: ");
        }
    }
    public static Pedido inativarPedido(Pedido pedido, String token) throws Exception {
        pedido.setActive(false);

        String pedidoJson = objectMapper.writeValueAsString(pedido);
        String urlComId = BASE_URL + "/" + pedido.getId();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlComId))
                .PUT(HttpRequest.BodyPublishers.ofString(pedidoJson))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200 || response.statusCode() == 204) {
            if (response.body().isEmpty()) {
                return pedido;
            } else {
                try {
                    return objectMapper.readValue(response.body(), Pedido.class);
                } catch (IOException e) {
                    throw new Exception("Erro ao processar a resposta do servidor: ");
                }
            }
        } else {
            throw new Exception("Erro ao inativar pedido: ");
        }
    }

}