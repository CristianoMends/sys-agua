package edu.pies.sysaguaapp.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class AuthService {
    private static final String LOGIN_URL = "http://localhost:8080/users/login"; // Endpoint de login
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public AuthService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public String autenticar(String email, String password) throws Exception {
        String requestBody = objectMapper.writeValueAsString(Map.of("email", email, "password", password));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(LOGIN_URL))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {

            Map<String, String> responseBody = objectMapper.readValue(response.body(), new TypeReference<>() {});
            return responseBody.get("token");
        } else {
            throw new Exception("Falha na autenticação: " + response.body());
        }
    }
}

