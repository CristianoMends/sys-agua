package edu.pies.sysaguaapp.controllers;

import edu.pies.sysaguaapp.enumeration.TransactionType;
import edu.pies.sysaguaapp.models.Transaction;
import edu.pies.sysaguaapp.services.TokenManager;
import edu.pies.sysaguaapp.services.TransactionService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class FinanceiroController {
    private final TransactionService transactionService;
    private final String token;

    @FXML
    private StackPane rootPane;

    @FXML
    private TableView<Transaction> movimentacoesTableView;

    @FXML
    private ObservableList<Transaction> transacoesList;

    @FXML
    private TableColumn<Transaction, String> dataColumn, codigoColumn, tipoColumn, valorColumn;

    @FXML
    private Button btnCompras, btnPedido;

    @FXML
    private Label saldoLabel, previstoLabel, totalPedidosLabel, pendentePedidoLabel, totalComprasLabel, pendenteComprasLabel;

    private BigDecimal saldoAtual, saldoPrevisto, totalCompras, totalPedidos, pendenteCompras, pendentePedidos  = BigDecimal.ZERO;

    public FinanceiroController() {
        this.transactionService = new TransactionService();
        token = TokenManager.getInstance().getToken();
    }

    @FXML
    public void initialize() {
        btnPedido.setCursor(Cursor.HAND);
        btnCompras.setCursor(Cursor.HAND);
        transacoesList = FXCollections.observableArrayList();
        obterPagamentos();
        atualizarTotais();


        movimentacoesTableView.setItems(transacoesList);
        dataColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCreatedAt().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyyy"))));
        codigoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTransactable().getId().toString()));
        tipoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType().getDescription()));
        valorColumn.setCellValueFactory(cellData -> {
            BigDecimal valor = cellData.getValue().getAmount();
            return new SimpleStringProperty(valor != null ? "R$ " + valor.negate().setScale(2, RoundingMode.HALF_UP).toString().replace(".", ",") : "");
        });

    }


    private void obterPagamentos() {
        try {
            List<Transaction> transacoes = transactionService.buscarTransacoes(token);
            if (transacoes != null) {
                transacoesList.setAll(transacoes);
                movimentacoesTableView.refresh();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao obter pagamentos " + e.getMessage());
        }
    }

    private void atualizarTotais() {
        try {
            List<Transaction> transacoes = transactionService.buscarTransacoes(token);

            totalPedidos = transacoes.stream()
                    .filter(t -> t.getType() == TransactionType.INCOME)
                    .map(Transaction::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            totalCompras = transacoes.stream()
                    .filter(t -> t.getType() == TransactionType.EXPENSE)
                    .map(Transaction::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            pendentePedidos = transacoes.stream()
                    .filter(t -> t.getType() == TransactionType.INCOME)
                    .map(t -> t.getTransactable().getBalance())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            pendenteCompras = transacoes.stream()
                    .filter(t -> t.getType() == TransactionType.EXPENSE)
                    .map(t -> t.getTransactable().getBalance())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);


//            saldoAtual = transacoes.stream()
//                    .filter(t -> t.getType() == TransactionType.INCOME)
//                    .map(t -> t.getTransactable().get                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      )
//                    .reduce(BigDecimal.ZERO, BigDecimal::add)
//                    .subtract(
//                        transacoes.stream()
//                            .filter(t -> t.getType() == TransactionType.EXPENSE)
//                            .map(t -> t.getTransactable().getPaidAmount())
//                            .reduce(BigDecimal.ZERO, BigDecimal::add)
//                    );

            saldoPrevisto = totalPedidos.subtract(totalCompras);
//            saldoLabel.setText("R$ " + new DecimalFormat("#.##").format(saldoAtual));
            previstoLabel.setText("R$ " + new DecimalFormat("#.##").format(saldoPrevisto));
            totalPedidosLabel.setText("R$ " + new DecimalFormat("#.##").format(totalPedidos));
            pendentePedidoLabel.setText("R$ " + new DecimalFormat("#.##").format(pendentePedidos));
            totalComprasLabel.setText("R$ " + new DecimalFormat("#.##").format(totalCompras));
            pendenteComprasLabel.setText("R$ " + new DecimalFormat("#.##").format(pendenteCompras));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao atualizar totais " + e.getMessage());
        }
    }
}
