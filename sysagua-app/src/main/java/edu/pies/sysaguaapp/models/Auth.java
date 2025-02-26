package edu.pies.sysaguaapp.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Auth {
    private final StringProperty email;
    private final StringProperty senha;


    public Auth(String email, String senha) {
        this.email = new SimpleStringProperty(email);
        this.senha = new SimpleStringProperty(senha);
    }

    // Getters e Setters
    public StringProperty getEmail() {
        return email;
    }

    public StringProperty getSenha() {
        return senha;
    }
}
