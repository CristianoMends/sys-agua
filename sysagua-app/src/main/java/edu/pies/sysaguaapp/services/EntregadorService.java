package edu.pies.sysaguaapp.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.pies.sysaguaapp.models.Entregador;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class EntregadorService {
    private static final String BASE_URL = "http://localhost:8080/deliveryPersons";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public EntregadorService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public List<Entregador> buscarEntregadores(String token) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<Entregador>>() {
            });
        } else {
            throw new Exception("Erro ao buscar: " + response.body());
        }
    }

    public Entregador cadastrarEntregador(Entregador entregador, String token) throws Exception {
        // Converter o objeto Entregador para JSON
        String entregadorJson = objectMapper.writeValueAsString(entregador);

        // Criar a requisição POST
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .POST(HttpRequest.BodyPublishers.ofString(entregadorJson))
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

    public Entregador atualizarEntregador(Entregador entregador, String token) throws Exception {
        String entregadorJson = objectMapper.writeValueAsString(entregador);

        String urlEntregador = BASE_URL + "/" + entregador.getId();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlEntregador))
                .PUT(HttpRequest.BodyPublishers.ofString(entregadorJson))
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

    public Entregador inativarEntregador(Entregador entregador, String token) throws Exception {
        String urlEntregador = BASE_URL + "/" + entregador.getId();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlEntregador))
                .DELETE()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 204) {
            return null;
        } else {
            throw new Exception("Erro ao inativar: " + response.body());
        }
    }
}
