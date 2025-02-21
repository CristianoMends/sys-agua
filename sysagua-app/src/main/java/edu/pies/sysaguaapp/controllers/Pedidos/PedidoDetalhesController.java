package edu.pies.sysaguaapp.controllers.Pedidos;

import edu.pies.sysaguaapp.dtos.compra.SendPgtoCompraDto;
import edu.pies.sysaguaapp.dtos.pedido.SendPgtoPedidoDto;
import edu.pies.sysaguaapp.enumeration.PaymentMethod;
import edu.pies.sysaguaapp.enumeration.TransactionType;
import edu.pies.sysaguaapp.models.Pedido.ItemPedido;
import edu.pies.sysaguaapp.models.Pedido.Pedido;
import edu.pies.sysaguaapp.models.Pedido.TransactionPedido;
import edu.pies.sysaguaapp.models.compras.TransactionCompra;
import edu.pies.sysaguaapp.models.compras.Compra;
import edu.pies.sysaguaapp.services.pedido.PedidoService;
import edu.pies.sysaguaapp.services.compra.TransactionCompraService;
import edu.pies.sysaguaapp.services.pedido.TransactionPedidoService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class PedidoDetalhesController {
    private final PedidoService pedidoService;
    private final TransactionPedidoService transactionService;
    private final String token;
    private Pedido pedido;

    @FXML
    private StackPane rootPane;

    @FXML
    private ObservableList<ItemPedido> produtosAddList;

    @FXML
    private ObservableList<TransactionPedido> pagamentosAddList;

    @FXML
    private TableView<TransactionPedido> pagamentosTableView;

    @FXML
    private TableView<ItemPedido> produtosTableView;

    @FXML
    private TableColumn<ItemPedido, String> produtoColumn, precoColumn, codigoColumn;

    @FXML
    private TableColumn<TransactionPedido, String> dataColumn, tipoColumn, valorColumn;

    @FXML
    private TableColumn<ItemPedido, Integer> quantidadeColumn;

    @FXML
    private ComboBox<PaymentMethod> metodoPagamento;

    @FXML
    private TextField valorField;

    @FXML
    private Label totalLabel, totalItensLabel, saldoLabel, pagoLabel, pedidoIdLabel;

    @FXML
    private Label entregadorLabel, clienteLabel, dataEntrada, pagamentoErrorLabel, valorErrorLabel;

    public PedidoDetalhesController(PedidoService pedidoService, String token, Pedido pedido) {
        this.pedidoService = pedidoService;
        transactionService = new TransactionPedidoService();
        this.token = token;
        this.pedido = pedido;
        produtosAddList = FXCollections.observableArrayList();
        pagamentosAddList = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        produtosAddList.addAll(pedido.getProductOrders());
        atualizaPedido();
        preencherCampos();
        validarCampos();
        obterPagamentos();
        atualizarTotais();

        produtosTableView.setItems(produtosAddList);
        codigoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getId().toString()));
        produtoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getName()));
        quantidadeColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject());
        precoColumn.setCellValueFactory(cellData -> {
            BigDecimal preco = cellData.getValue().getUnitPrice();
            return new SimpleStringProperty(preco != null ? "R$ " + preco.setScale(2, RoundingMode.HALF_UP).toString().replace(".", ",") : "");
        });

        pagamentosTableView.setItems(pagamentosAddList);
        dataColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCreatedAt().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyyy"))));
        tipoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPaymentMethod().getDescription()));
        valorColumn.setCellValueFactory(cellData -> {
            BigDecimal valor = cellData.getValue().getAmount();
            return new SimpleStringProperty(valor != null ? "R$ " + valor.setScale(2, RoundingMode.HALF_UP).toString().replace(".", ",") : "");
        });

        metodoPagamento.setItems(FXCollections.observableArrayList(PaymentMethod.values()));
        metodoPagamento.setConverter(new StringConverter<PaymentMethod>() {
            @Override
            public String toString(PaymentMethod metodo) {
                return metodo != null ? metodo.getDescription() : "";
            }
            @Override
            public PaymentMethod fromString(String string) {
                for (PaymentMethod metodo : PaymentMethod.values()) {
                    if (metodo.getDescription().equals(string)) {
                        return metodo;
                    }
                }
                return null;
            }
        });
    }

    @FXML
    private void handleInserirPagamento() {
        if (validarFormItem()) {
            try {
                BigDecimal valor = new BigDecimal(valorField.getText().replace(",", "."));

                SendPgtoPedidoDto novoPagamento = new SendPgtoPedidoDto();
                novoPagamento.setReceivedAmount(valor);
                novoPagamento.setPaymentMethod(PaymentMethod.PIX);
                Long idCompra = pedido.getId();

                pedidoService.cadastrarPagamento(novoPagamento, idCompra, token);
                limparCampos();
                obterPagamentos();
                atualizaPedido();
                atualizarTotais();
                pagamentosTableView.refresh();

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Erro ao fazer pagamento" + e.getMessage());
            }
        }
    }

    private void preencherCampos() {
        entregadorLabel.setText(pedido.getDeliveryPerson().getName());
        clienteLabel.setText(pedido.getCustomer().getName());
        dataEntrada.setText(pedido.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        pedidoIdLabel.setText("N° " + pedido.getId());

    }

    private void carregarTela(String caminho) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
            Parent addProduto = loader.load();
            rootPane.getChildren().clear();
            rootPane.getChildren().add(addProduto);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar tela: " + e.getMessage());
        }
    }

    private void obterPagamentos() {
        try {
            List<TransactionPedido> transacoes = transactionService.buscarTransacoes(token);
            if (transacoes != null) {
                pagamentosAddList.setAll(transacoes.stream()
                        .filter(transacao -> transacao.getType() == TransactionType.INCOME &&
                                transacao.getTransactable() != null &&
                                transacao.getTransactable().getId().equals(pedido.getId()))
                        .collect(Collectors.toList()));
                pagamentosTableView.refresh();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao obter pagamentos " + e.getMessage());
        }
    }

    private void atualizarTotais() {
        BigDecimal total = produtosAddList.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int itens = produtosAddList.size();

        totalLabel.setText("R$ " + total.setScale(2, RoundingMode.HALF_UP));
        totalItensLabel.setText(String.valueOf(itens));

        saldoLabel.setText("R$ " + pedido.getBalance().setScale(2, RoundingMode.HALF_UP).toString().replace(".", ","));
        pagoLabel.setText("R$ " + pedido.getReceivedAmount().setScale(2, RoundingMode.HALF_UP).toString().replace(".", ","));

    }

    private void atualizaPedido() {
        try {
            this.pedido = pedidoService.buscarPedidoId(this.pedido.getId(), token);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao atualizar compra" + e.getMessage());
        }
    }

    private void validarCampos() {
        configurarValidacaoDinheiro(valorField, valorErrorLabel);
    }

    private boolean validarFormItem() {
        boolean isValid = true;

        if (metodoPagamento == null || metodoPagamento.toString().isEmpty()) {
            pagamentoErrorLabel.setText("Tipo é obrigatório.");
            pagamentoErrorLabel.setVisible(true);
            pagamentoErrorLabel.setManaged(true);
            isValid = false;
        } else {
            pagamentoErrorLabel.setVisible(false);
            pagamentoErrorLabel.setManaged(false);
        }

        if (valorField.getText().trim().isEmpty()) {
            valorErrorLabel.setText("Valor é obrigatório.");
            valorErrorLabel.setVisible(true);
            valorErrorLabel.setManaged(true);
            isValid = false;
        } else {
            valorErrorLabel.setVisible(false);
            valorErrorLabel.setManaged(false);
        }

        BigDecimal valor = new BigDecimal(valorField.getText().replace(",", "."));

        if (valor.compareTo(BigDecimal.ZERO) == 0) {
            valorErrorLabel.setText("Campo não pode ser zero.");
            valorErrorLabel.setVisible(true);
            valorErrorLabel.setManaged(true);
            isValid = false;
        } else {
            valorErrorLabel.setVisible(false);
            valorErrorLabel.setManaged(false);
        }

        if (valor.compareTo(pedido.getBalance()) > 0) {
            valorErrorLabel.setText("Valor não permitido maior que o saldo.");
            valorErrorLabel.setVisible(true);
            valorErrorLabel.setManaged(true);
            isValid = false;
        }
        else{
            valorErrorLabel.setVisible(false);
            valorErrorLabel.setManaged(false);
        }

        return isValid;
    }


    private void configurarValidacaoDinheiro(TextField textField, Label errorLabel) {
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*([,]\\d{0,4})?")) { // Permite números e até 4 casas decimais
                errorLabel.setVisible(false);
                errorLabel.setManaged(false);
                return change;
            }
            errorLabel.setVisible(true);
            errorLabel.setManaged(true);
            return null; // Rejeita mudanças inválidas
        });
        textField.setTextFormatter(formatter);
    }

    private void limparCampos() {
        metodoPagamento.getSelectionModel().clearSelection();
        valorField.clear();
    }
}
