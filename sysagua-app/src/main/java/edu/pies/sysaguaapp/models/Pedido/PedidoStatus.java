package edu.pies.sysaguaapp.models;

public enum PedidoStatus {
    PENDING("Pendente"),
    DELIVERED("Entregue"),
    LATE("Atrasado"),
    CANCELED("Cancelado");
    private final String description;
    PedidoStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
