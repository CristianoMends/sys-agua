package edu.pies.sysaguaapp.controllers.estoque;

import edu.pies.sysaguaapp.models.Estoque;
import edu.pies.sysaguaapp.models.produto.Produto;
import edu.pies.sysaguaapp.services.EstoqueService;
import edu.pies.sysaguaapp.services.produto.ProdutoService;
import edu.pies.sysaguaapp.services.TokenManager;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.stream.Collectors;

public class AjusteEstoqueController {
    private final EstoqueService estoqueService;
    private final ProdutoService produtoService;

    @FXML
    private StackPane rootPane;

    @FXML
    private ComboBox<Produto> produtoComboBox;

    @FXML
    private VBox listProductView;

    @FXML
    private TextField quantidadeTextField;

    @FXML
    private Button btnSalvar, btnCancelar, btnInserir;

    @FXML
    private TableView<Estoque> ajustesTableView;

    @FXML
    private TableColumn<Estoque, String> produtoColumn;

    @FXML
    private TableColumn<Estoque, Integer> quantidadeColumn;

    @FXML
    private Label produtoErrorLabel;

    @FXML
    private Label quantidadeErrorLabel;

    private ObservableList<Estoque> ajustesList;

    private boolean ajustePositivo;

    public AjusteEstoqueController() {
        estoqueService = new EstoqueService();
        produtoService = new ProdutoService();
        ajustesList = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        btnInserir.setCursor(Cursor.HAND);
        carregarProdutos();
        ajustesTableView.setItems(ajustesList);
        produtoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getName()));
        quantidadeColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCurrentQuantity()).asObject());

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
    }

    private void carregarProdutos() {
        try {
            String token = TokenManager.getInstance().getToken();
            List<Produto> produtos = produtoService.buscarProdutos(token).stream()
                    .filter(Produto::isActive)
                    .sorted((p1, p2) -> Long.compare(p1.getId(), p2.getId()))
                    .collect(Collectors.toList());
            produtoComboBox.setItems(FXCollections.observableArrayList(produtos));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void setAjustePositivo(boolean ajustePositivo) {
        this.ajustePositivo = ajustePositivo;
    }

    @FXML
    private void handleSalvar() {
        for (Estoque ajuste : ajustesList) {
            if (ajuste.getProduct() != null) {
                int quantidade = ajustePositivo ? ajuste.getCurrentQuantity() : -ajuste.getCurrentQuantity();
                adicionarProdutoEstoque(ajuste.getProduct().getId(), quantidade);
            }
        }
        ajustesList.clear();
        carregarTela("/views/Estoque/Estoque.fxml");
    }

    @FXML
    private void handleCancelar() {
        carregarTela("/views/Estoque/Estoque.fxml");
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

    @FXML
    private void handleInserir() {
        if (validarFormulario()) {
            Estoque novoProdutoEstoque = criarProdutoEstoque();
            if (novoProdutoEstoque != null && novoProdutoEstoque.getProduct() != null) {
                ajustesList.add(novoProdutoEstoque);
                clearFieldForm();
            }
        }
    }

    private boolean validarFormulario() {
        boolean isValid = true;

        if (produtoComboBox.getValue() == null) {
            produtoErrorLabel.setVisible(true);
            produtoErrorLabel.setManaged(true);
            isValid = false;
        } else {
            produtoErrorLabel.setVisible(false);
            produtoErrorLabel.setManaged(false);
        }

        if (quantidadeTextField.getText().trim().isEmpty()) {
            quantidadeErrorLabel.setVisible(true);
            quantidadeErrorLabel.setManaged(true);
            isValid = false;
        } else {
            quantidadeErrorLabel.setVisible(false);
            quantidadeErrorLabel.setManaged(false);
        }

        return isValid;
    }

    private void clearFieldForm(){
        quantidadeTextField.clear();
        produtoComboBox.getSelectionModel().clearSelection();
    }

    private Estoque criarProdutoEstoque() {
        Produto produtoSelecionado = produtoComboBox.getValue();
        Estoque novoProdutoEstoque = new Estoque();
        int quantidade = Integer.parseInt(quantidadeTextField.getText());

        if (produtoSelecionado != null) {
            novoProdutoEstoque.setProduct(produtoSelecionado);
            novoProdutoEstoque.setCurrentQuantity(quantidade);
        }

        return novoProdutoEstoque;
    }

    private void adicionarProdutoEstoque(Long productId, int quantity) {
        try {
            String token = TokenManager.getInstance().getToken();
            estoqueService.addProdutoEstoque(productId, quantity, token);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

}
