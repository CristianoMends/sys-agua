package edu.pies.sysaguaapp.models.Pedido;

public enum TipoPagamento {
    PIX("Pix"),
    DEBIT("Débito"),
    CREDIT("Crédito"),
    MONEY("Dinheiro"),
    TICKET("Boleto");
    private final String descricao;

    TipoPagamento(String descricao){
        this.descricao = descricao;
    }
    public String getDescricao() {
        return descricao;
    }
}
