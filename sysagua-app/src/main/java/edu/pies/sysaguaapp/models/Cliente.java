package edu.pies.sysaguaapp.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Cliente {
    private final StringProperty nome;
    private final StringProperty cnpj;
    private final StringProperty endereco;
    private final StringProperty cidade;
    private final StringProperty email;

    public Cliente(String nome, String cnpj, String endereco, String cidade, String email) {
        this.nome = new SimpleStringProperty(nome) ;
        this.cnpj = new SimpleStringProperty(cnpj) ;
        this.endereco = new SimpleStringProperty(endereco) ;
        this.cidade = new SimpleStringProperty(cidade) ;
        this.email = new SimpleStringProperty(email) ;
    }

    // Getters e Setters
    public StringProperty getNome() {
        return nome;
    }

    public StringProperty getCnpj() {
        return cnpj;
    }

    public StringProperty getEndereco() {
        return endereco;
    }

    public StringProperty getCidade() {
        return cidade;
    }

    public StringProperty getEmail() {
        return email;
    }
}
