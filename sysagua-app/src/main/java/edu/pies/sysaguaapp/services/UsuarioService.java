package edu.pies.sysaguaapp.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.http.HttpClient;

public class UsuarioService {

    private static final String BASE_URL = "http://localhost:8080/users";
    private static HttpClient httpClient;
    private static ObjectMapper objectMapper;

    public  UsuarioService(){
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }
}
