package edu.pies.sysaguaapp.controllers;

import edu.pies.sysaguaapp.models.Pedido.TransactionPedido;
import edu.pies.sysaguaapp.models.compras.TransactionCompra;
import edu.pies.sysaguaapp.services.compra.TransactionCompraService;
import edu.pies.sysaguaapp.services.pedido.TransactionPedidoService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import javax.swing.text.TableView;

public class FinanceiroController {
    private final TransactionPedidoService transactionPedidoService;
    private final TransactionCompraService transactionCompraService;

    @FXML
    private StackPane rootPane;

    @FXML
    private TableView movimentacoesTableView;

    @FXML
    private Button btnCompras, btnPedido;

    @FXML
    public void initialize() {

    }


    public FinanceiroController() {
        this.transactionPedidoService = new TransactionPedidoService();
        this.transactionCompraService = new TransactionCompraService();

    }
}
