package edu.pies.sysaguaapp.controllers.compras;

import edu.pies.sysaguaapp.controllers.produto.AddProdutoController;
import edu.pies.sysaguaapp.dtos.compra.ItemCompraDto;
import edu.pies.sysaguaapp.dtos.compra.SendCompraDto;
import edu.pies.sysaguaapp.enumeration.PaymentMethod;
import edu.pies.sysaguaapp.models.Fornecedor;
import edu.pies.sysaguaapp.models.produto.Produto;
import edu.pies.sysaguaapp.models.compras.ItemCompra;
import edu.pies.sysaguaapp.services.compra.CompraService;
import edu.pies.sysaguaapp.services.FornecedorService;
import edu.pies.sysaguaapp.services.TokenManager;
import edu.pies.sysaguaapp.services.produto.ProdutoService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class AddCompraController {

    private final CompraService compraService;
    private final ProdutoService produtoService;
    private final FornecedorService fornecedorService;
    private final String token;

    @FXML
    private StackPane rootPane;

    @FXML
    private TableView<ItemCompra> adicionadosTableView;

    @FXML
    private DatePicker dataEntrada;

    @FXML
    private TextField numeroNfeField, precoField, quantidadeField;

    @FXML
    private ComboBox<Fornecedor> fornecedorComboBox;

    @FXML
    private ComboBox<Produto> produtoComboBox;

    @FXML
    private ComboBox<PaymentMethod> metodoPagamento;

    @FXML
    private TextArea descricao;

    @FXML
    private ObservableList<ItemCompra> produtosAddList;

    @FXML
    private TableColumn<ItemCompra, String> produtoColumn, precoColumn, codigoColumn;

    @FXML
    private TableColumn<ItemCompra, Integer> quantidadeColumn;

    @FXML
    private Label dataErrorLabel, fornecedorErrorLabel, produtoErrorLabel, precoErrorLabel, quantidadeErrorLabel, nfeErrorLabel;

    @FXML
    private Label totalLabel, totalItensLabel, saveErrorLabel, duplicadoErrorLabel;

    @FXML
    private Button btnSalvar, btnCancelar, btnNovoProduto, btnInserir;

    private final ContextMenu contextMenu = new ContextMenu();
    private ItemCompra itemEditando = null;


    public AddCompraController() {
        compraService = new CompraService();
        produtoService = new ProdutoService();
        fornecedorService = new FornecedorService();
        produtosAddList = FXCollections.observableArrayList();
        token = TokenManager.getInstance().getToken();
    }

    @FXML
    public void initialize() {
        validarCampos();
        carregarListaProdutos();
        carregarListaFornecedores();
        btnSalvar.setCursor(Cursor.HAND);
        btnInserir.setCursor(Cursor.HAND);
        btnNovoProduto.setCursor(Cursor.HAND);
        btnCancelar.setCursor(Cursor.HAND);

        adicionadosTableView.setItems(produtosAddList);
        codigoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getId().toString()));
        produtoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getName()));
        quantidadeColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject());
        precoColumn.setCellValueFactory(cellData -> {
            BigDecimal preco = cellData.getValue().getPurchasePrice();
            return new SimpleStringProperty(preco != null ? "R$ " + preco.setScale(2, RoundingMode.HALF_UP).toString().replace(".", ",") : "");
        });

        produtosAddList.addListener((ListChangeListener.Change<? extends ItemCompra> change) -> {
            atualizarTotais();
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

        atualizarTotais();
        showMenuContext();

    }

    @FXML
    private void handleInserir() {
        if (validarFormItem()) {
            ItemCompra novoItemCompra = criarItemCompra();

            if (novoItemCompra != null && novoItemCompra.getProduct() != null) {
                if (itemEditando != null) {
                    int index = produtosAddList.indexOf(itemEditando);
                    if (index != -1) {
                        produtosAddList.set(index, novoItemCompra); // Substitui o item
                    }
                    itemEditando = null;
                } else {
                    produtosAddList.add(novoItemCompra);
                }
                clearProdutoForm();
            }
        }
    }


    private ItemCompra criarItemCompra() {
        Produto produtoSelecionado = produtoComboBox.getValue();
        BigDecimal preco = new BigDecimal(precoField.getText().replace(",", "."));
        int quantidade = Integer.parseInt(quantidadeField.getText());

        ItemCompra novoItemCompra = new ItemCompra();

        if (produtoSelecionado != null) {
            novoItemCompra.setProduct(produtoSelecionado);
            novoItemCompra.setQuantity(quantidade);
            novoItemCompra.setPurchasePrice(preco);
        }

        return novoItemCompra;
    }

    @FXML
    private void handleSalvar() {

        if (validarFormCompra()){
            try {
                SendCompraDto novaCompra = new SendCompraDto();
                novaCompra.setSupplierId(fornecedorComboBox.getValue().getId());
                novaCompra.setNfe(numeroNfeField.getText());
                novaCompra.setEntryAt(dataEntrada.getValue().atTime(LocalTime.now()));
                novaCompra.setPaymentMethod(metodoPagamento.getValue());
                novaCompra.setDescription(descricao.getText());
                novaCompra.setPaidAmount(BigDecimal.ZERO);
                novaCompra.setTotalAmount(BigDecimal.ZERO);

                List<ItemCompraDto> itensDto = produtosAddList.stream()
                        .map(item -> {
                            ItemCompraDto dto = new ItemCompraDto();
                            dto.setProductId(item.getProduct().getId());
                            dto.setQuantity(item.getQuantity());
                            dto.setPurchasePrice(item.getPurchasePrice());
                            return dto;
                        })
                        .collect(Collectors.toList());

                novaCompra.setItems(itensDto);

                compraService.cadastrarCompra(novaCompra, token);

                clearAllForm();
                carregarTela("/views/Compras/Compras.fxml");

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Erro ao cadastrar compra" + e.getMessage());
            }

        }
    }


    @FXML
    private void handleCancelar() {
        carregarTela("/views/Compras/Compras.fxml");
    }

    @FXML
    private void handleNovoProduto() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Produtos/AddProdutos.fxml"));
            Parent addProduto = loader.load();

            AddProdutoController controller = loader.getController();
            controller.setFecharAoSair(true);
            controller.setOnProdutoSalvo((v) -> carregarListaProdutos());

            Stage stage = new Stage();
            stage.setTitle("Cadastar Produto");
            stage.setScene(new Scene(addProduto));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar tela de cadastro de produto: " + e.getMessage());
        }

    }

    private void showMenuContext() {
        if (contextMenu.getItems().isEmpty()) {
            MenuItem editarLancamento = new MenuItem("Editar lançamento");
            MenuItem removerLancamento = new MenuItem("Remover lançamento");
            editarLancamento.setOnAction(event -> handleEditarLancamento());
            removerLancamento.setOnAction(event -> handleRemoverLancamento());

            contextMenu.getItems().addAll(editarLancamento, removerLancamento);
        }

        adicionadosTableView.setRowFactory(tv -> {
            TableRow<ItemCompra> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY && !row.isEmpty()) {
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                } else {
                    contextMenu.hide();
                }
            });
            return row;
        });
    }

    private void handleEditarLancamento() {
        ItemCompra produtoSelecionado = adicionadosTableView.getSelectionModel().getSelectedItem();
        if (produtoSelecionado != null) {
            itemEditando = produtoSelecionado;
            preencherCampos(produtoSelecionado);
        }
    }

    private void preencherCampos(ItemCompra itemSelecionado) {
        produtoComboBox.setValue(itemSelecionado.getProduct());
        precoField.setText(itemSelecionado.getPurchasePrice().toString().replace(".",","));
        quantidadeField.setText(itemSelecionado.getQuantity().toString());
    }

    private void handleRemoverLancamento(){
        ItemCompra produtoSelecionado = adicionadosTableView.getSelectionModel().getSelectedItem();
        if (produtoSelecionado != null) {
            produtosAddList.remove(produtoSelecionado);
        }
        adicionadosTableView.refresh();
    }


    //------------------------- carregar itens -------------------------//

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

    private void carregarListaProdutos(){

        try {
            List<Produto> produtos = produtoService.buscarProdutos(token).stream()
                    .filter(Produto::isActive)
                    .sorted((p1, p2) -> Long.compare(p1.getId(), p2.getId()))
                    .collect(Collectors.toList());
            produtoComboBox.setItems(FXCollections.observableArrayList(produtos));

            produtoComboBox.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Produto item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getId() + " - " + item.getName());
                    }
                }
            });

            produtoComboBox.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(Produto item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getId() + " - " + item.getName());
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private void carregarListaFornecedores(){

        try {
            List<Fornecedor> fornecedores = fornecedorService.buscarFornecedores(token).stream()
                    .filter(Fornecedor::isActive)
                    .sorted((p1, p2) -> Long.compare(p1.getId(), p2.getId()))
                    .collect(Collectors.toList());
            fornecedorComboBox.setItems(FXCollections.observableArrayList(fornecedores));

            fornecedorComboBox.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Fornecedor item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getId() + " - " + item.getSocialReason());
                    }
                }
            });

            fornecedorComboBox.setButtonCell(new ListCell<Fornecedor>() {
                protected void updateItem(Fornecedor item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getId() + " - " + item.getSocialReason());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
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

    //--------------------------- validações -------------------------//

    private boolean validarFormItem() {
        boolean isValid = true;
        Produto produto = produtoComboBox.getValue();

        if (produto == null || produto.toString().isEmpty()) {
            produtoErrorLabel.setText("Produto é obrigatória.");
            produtoErrorLabel.setVisible(true);
            produtoErrorLabel.setManaged(true);
            isValid = false;
        } else {
            produtoErrorLabel.setVisible(false);
            produtoErrorLabel.setManaged(false);
        }

        if (precoField.getText().trim().isEmpty()) {
            precoErrorLabel.setText("Preço é obrigatória.");
            precoErrorLabel.setVisible(true);
            precoErrorLabel.setManaged(true);
            isValid = false;
        } else {
            precoErrorLabel.setVisible(false);
            precoErrorLabel.setManaged(false);
        }

        if (quantidadeField.getText().trim().isEmpty()) {
            quantidadeErrorLabel.setText("Quantidade é obrigatório.");
            quantidadeErrorLabel.setVisible(true);
            quantidadeErrorLabel.setManaged(true);
            isValid = false;
        } else {
            quantidadeErrorLabel.setVisible(false);
            quantidadeErrorLabel.setManaged(false);
        }

        BigDecimal preco = new BigDecimal(precoField.getText().replace(",", "."));

        if (preco.compareTo(BigDecimal.ZERO) == 0) {
            precoErrorLabel.setText("Campo não pode ser zero.");
            precoErrorLabel.setVisible(true);
            precoErrorLabel.setManaged(true);
            isValid = false;
        } else {
            precoErrorLabel.setVisible(false);
            precoErrorLabel.setManaged(false);
        }

        if (Integer.parseInt(quantidadeField.getText().trim()) == 0) {
            quantidadeErrorLabel.setText("Campo não pode ser zero.");
            quantidadeErrorLabel.setVisible(true);
            quantidadeErrorLabel.setManaged(true);
            isValid = false;
        } else {
            quantidadeErrorLabel.setVisible(false);
            quantidadeErrorLabel.setManaged(false);
        }


        if (produtoExiste(produto)) {
            if (itemEditando == null || produto != itemEditando.getProduct()) {
                duplicadoErrorLabel.setVisible(true);
                duplicadoErrorLabel.setManaged(true);
                isValid = false;
            } else {
                duplicadoErrorLabel.setVisible(false);
                duplicadoErrorLabel.setManaged(false);
            }
        } else {
            duplicadoErrorLabel.setVisible(false);
            duplicadoErrorLabel.setManaged(false);
        }

        return isValid;
    }

    private boolean produtoExiste(Produto produto) {
        return produtosAddList.stream()
                .anyMatch(item -> item.getProduct().getId().equals(produto.getId()));
    }

    private boolean validarFormCompra() {
        boolean isValid = true;

        if (fornecedorComboBox.getValue() == null || fornecedorComboBox.getValue().toString().isEmpty()) {
            fornecedorErrorLabel.setText("Fornecedor é obrigatório.");
            fornecedorErrorLabel.setVisible(true);
            fornecedorErrorLabel.setManaged(true);
            isValid = false;
        } else {
            fornecedorErrorLabel.setVisible(false);
            fornecedorErrorLabel.setManaged(false);
        }

        if (produtosAddList.isEmpty()){
            saveErrorLabel.setVisible(true);
            saveErrorLabel.setManaged(true);
            isValid = false;
        } else {
            saveErrorLabel.setVisible(false);
            saveErrorLabel.setManaged(false);
        }

        if (dataEntrada.getValue().toString().isEmpty()) {
            dataErrorLabel.setText("Data é obrigatório");
            dataErrorLabel.setVisible(true);
            dataErrorLabel.setManaged(true);
            isValid = false;
        } else {
            dataErrorLabel.setVisible(false);
            dataErrorLabel.setManaged(false);
        }

        if (dataEntrada.getValue().isAfter(LocalDate.now())) {
            dataErrorLabel.setText("Data de entrada não pode ser uma data futura.");
            dataErrorLabel.setVisible(true);
            dataErrorLabel.setManaged(true);
            isValid = false;
        } else {
            dataErrorLabel.setVisible(false);
            dataErrorLabel.setManaged(false);
        }

        return isValid;
    }

    private void validarCampos() {
        // Validar números
        configurarValidacaoDinheiro(precoField, precoErrorLabel);
        configurarValidacaoNumerica(quantidadeField, quantidadeErrorLabel);
        configurarValidacaoNfe(numeroNfeField, nfeErrorLabel);
    }


    private void configurarValidacaoNumerica(TextField textField, Label errorLabel) {
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")){
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


    private void configurarValidacaoTexto(TextField textField, Label errorLabel) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[\\p{L}\\s\\d.,-]*")) { // Permite letras, espaços, números, '.', ',' e '-'
                textField.setText(oldValue);
                errorLabel.setVisible(true);
                errorLabel.setManaged(true);
            } else {
                errorLabel.setVisible(false);
                errorLabel.setManaged(false);
            }
        });
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

    private void configurarValidacaoNfe(TextField textField, Label errorLabel) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            String digits = newValue.replaceAll("[^\\d]", "");

            if (digits.length() > 9) {
                digits = digits.substring(0, 9);
            }

            textField.setText(digits);

            if (digits.length() == 9) {
                errorLabel.setVisible(false);
                errorLabel.setManaged(false);
            } else {
                errorLabel.setVisible(true);
                errorLabel.setManaged(true);
            }
        });
    }

    private void clearProdutoForm(){
        produtoComboBox.getSelectionModel().clearSelection();
        precoField.clear();
        quantidadeField.clear();
    }

    private void clearAllForm(){
        fornecedorComboBox.getSelectionModel().clearSelection();
        produtoComboBox.getSelectionModel().clearSelection();
        numeroNfeField.clear();
        precoField.clear();
        quantidadeField.clear();
    }
}
