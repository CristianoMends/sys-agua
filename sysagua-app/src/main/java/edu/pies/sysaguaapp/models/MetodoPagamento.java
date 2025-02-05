package edu.pies.sysaguaapp.models;

public enum MetodoPagamento {
    PIX("Pix"),
    DEBIT("Débito"),
    CREDIT("Crédito"),
    MONEY("Dinheiro"),
    TICKET("Boleto");
    private final String descricao;

    MetodoPagamento(String descricao){
        this.descricao = descricao;
    }
    public String getDescricao() {
        return descricao;
    }
}
