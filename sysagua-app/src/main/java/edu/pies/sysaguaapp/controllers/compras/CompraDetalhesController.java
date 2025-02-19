package edu.pies.sysaguaapp.controllers.compras;

import edu.pies.sysaguaapp.dtos.compra.SendPgtoCompraDto;
import edu.pies.sysaguaapp.enumeration.PaymentMethod;
import edu.pies.sysaguaapp.enumeration.TransactionType;
import edu.pies.sysaguaapp.models.TransactionCompra;
import edu.pies.sysaguaapp.models.compras.Compra;
import edu.pies.sysaguaapp.models.compras.ItemCompra;
import edu.pies.sysaguaapp.services.CompraService;
import edu.pies.sysaguaapp.services.TransactionCompraService;
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

public class CompraDetalhesController {
    private final CompraService compraService;
    private final TransactionCompraService transactionService;
    private final String token;
    private Compra compra;

    @FXML
    private StackPane rootPane;

    @FXML
    private ObservableList<ItemCompra> produtosAddList;

    @FXML
    private ObservableList<TransactionCompra> pagamentosAddList;

    @FXML
    private TableView<TransactionCompra> pagamentosTableView;

    @FXML
    private TableView<ItemCompra> produtosTableView;

    @FXML
    private TableColumn<ItemCompra, String> produtoColumn, precoColumn, codigoColumn;

    @FXML
    private TableColumn<TransactionCompra, String> dataColumn, tipoColumn, valorColumn;

    @FXML
    private TableColumn<ItemCompra, Integer> quantidadeColumn;

    @FXML
    private ComboBox<PaymentMethod> metodoPagamento;

    @FXML
    private TextField valorField;

    @FXML
    private Label totalLabel, totalItensLabel, saldoLabel, pagoLabel;

    @FXML
    private Label numeroNfe, fornecedorSocialReason, dataEntrada, pagamentoErrorLabel, valorErrorLabel;

    public CompraDetalhesController(CompraService compraService, String token, Compra compra) {
        this.compraService = compraService;
        transactionService = new TransactionCompraService();
        this.token = token;
        this.compra = compra;
        produtosAddList = FXCollections.observableArrayList();
        pagamentosAddList = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        produtosAddList.addAll(compra.getItems());
        preencherCampos();
        atualizarTotais();
        validarCampos();
        obterPagamentos();

        produtosTableView.setItems(produtosAddList);
        codigoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getId().toString()));
        produtoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getName()));
        quantidadeColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject());
        precoColumn.setCellValueFactory(cellData -> {
            BigDecimal preco = cellData.getValue().getPurchasePrice();
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

                SendPgtoCompraDto novoPagamento = new SendPgtoCompraDto();
                novoPagamento.setAmount(valor);
                novoPagamento.setPaymentMethod(metodoPagamento.getValue());
                novoPagamento.setDescription("Pagamento via " + metodoPagamento.getValue() + " em: " + LocalDateTime.now());
                Long idCompra = compra.getId();

                compraService.cadastrarPagamento(novoPagamento, idCompra, token);
                limparCampos();
                obterPagamentos();
                atualizaCompra();
                atualizarTotais();
                pagamentosTableView.refresh();

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Erro ao fazer pagamento" + e.getMessage());
            }
        }
    }

    private void preencherCampos() {
        numeroNfe.setText(compra.getNfe());
        fornecedorSocialReason.setText(compra.getSupplier().getSocialReason());
        dataEntrada.setText(compra.getEntryAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

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
            List<TransactionCompra> transacoes = transactionService.buscarTransacaoCompraId(compra.getId(), token);
            if (transacoes != null) {
                pagamentosAddList.setAll(transacoes.stream()
                    .filter(transacao -> transacao.getType() == TransactionType.EXPENSE && transacao.getTransactable().getId().equals(compra.getId()))
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
                .map(item -> item.getPurchasePrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int itens = produtosAddList.size();

        totalLabel.setText("R$ " + total.setScale(2, RoundingMode.HALF_UP));
        totalItensLabel.setText(String.valueOf(itens));

        saldoLabel.setText("R$ " + compra.getBalance().setScale(2, RoundingMode.HALF_UP).toString().replace(".", ","));
        pagoLabel.setText("R$ " + compra.getPaidAmount().setScale(2, RoundingMode.HALF_UP).toString().replace(".", ","));

    }

    private void atualizaCompra() {
        try {
            this.compra = compraService.buscarCompraId(this.compra.getId(), token);
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

        if (valor.compareTo(compra.getBalance()) > 0) {
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
