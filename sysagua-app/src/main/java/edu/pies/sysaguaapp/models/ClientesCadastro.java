package edu.pies.sysaguaapp.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ClientesCadastro {
    private  String nomeCompleto_RazaoSocial;
    private String enderecoCompleto;
    private long telefone;
    private long cnpj;
    private String email;

    // Getters e Setters
    public void setNomeCompleto_RazaoSocial(String nomeCompletoRazaoSocial) {
        this.nomeCompleto_RazaoSocial = nomeCompletoRazaoSocial;
    }
    public String getNomeCompleto_RazaoSocial() {
        return nomeCompleto_RazaoSocial;
    }

    public void setEnderecoCompleto(String enderecoCompleto) {
        this.enderecoCompleto = enderecoCompleto;
    }
    public String getEnderecoCompleto() {
        return enderecoCompleto;
    }

    public void setTelefone(long telefone) {
        this.telefone = telefone;
    }
    public long getTelefone() {
        return telefone;
    }

    public void setCnpj(long cnpj) {
        this.cnpj = cnpj;
    }
    public long getCnpj() {
        return cnpj;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }
}