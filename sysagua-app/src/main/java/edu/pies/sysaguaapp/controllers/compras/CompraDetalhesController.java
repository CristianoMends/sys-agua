package edu.pies.sysaguaapp.controllers.compras;

import edu.pies.sysaguaapp.enumeration.PaymentMethod;
import edu.pies.sysaguaapp.models.Produto;
import edu.pies.sysaguaapp.models.compras.Compra;
import edu.pies.sysaguaapp.models.compras.ItemCompra;
import edu.pies.sysaguaapp.services.CompraService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;

public class CompraDetalhesController {
    private final CompraService compraService;
    private final String token;
    private Compra compra;

    @FXML
    private StackPane rootPane;

    @FXML
    private ObservableList<ItemCompra> produtosAddList;

    @FXML
    private TableView<ItemCompra> pagamentosTableView;

    @FXML
    private TableView<ItemCompra> produtosTableView;

    @FXML
    private TableColumn<ItemCompra, String> produtoColumn, precoColumn, codigoColumn;

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
        this.token = token;
        this.compra = compra;
        produtosAddList = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        produtosAddList.addAll(compra.getItems());
        preencherCampos();
        atualizarTotais();

        produtosTableView.setItems(produtosAddList);
        codigoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getId().toString()));
        produtoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getName()));
        quantidadeColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject());
        precoColumn.setCellValueFactory(cellData -> {
            BigDecimal preco = cellData.getValue().getPurchasePrice();
            return new SimpleStringProperty(preco != null ? "R$ " + preco.setScale(2, RoundingMode.HALF_UP).toString().replace(".", ",") : "");
        });
    }

    @FXML
    private void handleInserirPagamento() {
//        if (validarFormItem()) {
//            ItemCompra novoItemCompra = criarItemCompra();
//
//            if (novoItemCompra != null && novoItemCompra.getProduct() != null) {
//                if (itemEditando != null) {
//                    int index = produtosAddList.indexOf(itemEditando);
//                    if (index != -1) {
//                        produtosAddList.set(index, novoItemCompra); // Substitui o item
//                    }
//                    itemEditando = null;
//                } else {
//                    produtosAddList.add(novoItemCompra);
//                }
//                clearProdutoForm();
//            }
//        }
        System.out.println("add pagamento");
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

    private void atualizarTotais() {
        BigDecimal total = produtosAddList.stream()
                .map(item -> item.getPurchasePrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int itens = produtosAddList.size();

        totalLabel.setText("R$ " + total.setScale(2, RoundingMode.HALF_UP));
        totalItensLabel.setText(String.valueOf(itens));

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
            valorErrorLabel.setText("Valor é obrigatória.");
            valorErrorLabel.setVisible(true);
            valorErrorLabel.setManaged(true);
            isValid = false;
        } else {
            valorErrorLabel.setVisible(false);
            valorErrorLabel.setManaged(false);
        }

        BigDecimal preco = new BigDecimal(valorField.getText().replace(",", "."));

        if (preco.compareTo(BigDecimal.ZERO) == 0) {
            valorErrorLabel.setText("Campo não pode ser zero.");
            valorErrorLabel.setVisible(true);
            valorErrorLabel.setManaged(true);
            isValid = false;
        } else {
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
}
