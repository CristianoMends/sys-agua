package edu.pies.sysaguaapp.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.pies.sysaguaapp.dtos.usuario.UpdateUsuarioDto;
import edu.pies.sysaguaapp.enumeration.Usuarios.UserStatus;
import edu.pies.sysaguaapp.models.Usuario;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class UsuarioService {

    private static final String BASE_URL = "http://localhost:8080/users";
    private static HttpClient httpClient;
    private static ObjectMapper objectMapper;

    public  UsuarioService(){
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }
    public List<Usuario> buscarUsuario(String token) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<Usuario>>() {
            });
        } else {
            throw new Exception("Erro ao buscar: " + response.body());
        }
    }

    public Usuario criarUsuario(Usuario usuarios, String token) throws Exception {
        String usuarioJson = objectMapper.writeValueAsString(usuarios);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .POST(HttpRequest.BodyPublishers.ofString(usuarioJson))
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
    public Usuario editarUsuario(Usuario usuario, String token) throws Exception {
        UpdateUsuarioDto updateDTO = new UpdateUsuarioDto();
        updateDTO.setName(usuario.getName());
        updateDTO.setSurname(usuario.getSurname());
        updateDTO.setPhone(usuario.getPhone());
        updateDTO.setEmail(usuario.getEmail());
        updateDTO.setStatus(usuario.getStatus());
        updateDTO.setAccess(usuario.getAccess());

        String usuarioJson = objectMapper.writeValueAsString(usuario);
        String urlComId = BASE_URL + "?id=" + usuario.getId();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlComId))
                .PUT(HttpRequest.BodyPublishers.ofString(usuarioJson))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 204) {
            return usuario;
        } else {
            throw new Exception("Erro ao atualizar: " + response.body());
        }
    }
    public Usuario inativarUsuario(Usuario usuario, String token) throws Exception {

        if (usuario.getStatus() == UserStatus.INACTIVE) {
            throw new Exception("O usuário já está inativo.");
        }
        String emailCodificado = URLEncoder.encode(usuario.getEmail(), StandardCharsets.UTF_8);
        String urlComEmail = BASE_URL + "?email=" + emailCodificado;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlComEmail))
                .DELETE()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 204) {
            return usuario;
        } else {
            throw new Exception("Erro ao inativar: " + response.body());
        }
    }

    public Usuario buscarUsuarioId(String id, String token) throws Exception {
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
            List<Usuario> usuarios = objectMapper.readValue(response.body(), new TypeReference<List<Usuario>>() {});
            if (!usuarios.isEmpty()) {
                return usuarios.get(0);
            }
            return null;
        } else {
            throw new Exception("Erro ao buscar usuario: " + response.body());
        }
    }
}