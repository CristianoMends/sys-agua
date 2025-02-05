package edu.pies.sysaguaapp.controllers.produto;

import edu.pies.sysaguaapp.models.ProductCategory;
import edu.pies.sysaguaapp.models.ProductLine;
import edu.pies.sysaguaapp.models.Produto;
import edu.pies.sysaguaapp.services.ProductCategoryService;
import edu.pies.sysaguaapp.services.ProductLineService;
import edu.pies.sysaguaapp.services.ProdutoService;
import edu.pies.sysaguaapp.services.TokenManager;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class AddProdutoController {
    private final ProdutoService produtoService;
    private final ProductCategoryService productCategoryService;
    private final ProductLineService productLineService;

    @FXML
    private StackPane rootPane;

    @FXML
    private TextField nomeField, custoField, precoUnitarioField, marcaField, unidadeField;

    @FXML
    private ComboBox<String> categoriaComboBox;

    @FXML
    private TextField ncmField, descricaoField, cestField, gtinField;

    @FXML
    private Label successMessage;

    @FXML
    private Label nomeErrorLabel, categoriaErrorLabel, custoErrorLabel, precoUnitarioErrorLabel, marcaErrorLabel, linhaErrorLabel, unidadeErrorLabel;

    @FXML
    private Button btnSalvar;

    @FXML
    private Button btnCancelar;

    @FXML
    private ComboBox<String> linhaComboBox;

    @FXML
    private Label ncmErrorLabel, descricaoErrorLabel, cestErrorLabel, gtinErrorLabel;


    private List<ProductCategory> categorias;
    private List<ProductLine> linhas;
    Produto produtoEditando = null;


    public AddProdutoController() {
        this.produtoService = new ProdutoService();
        this.productCategoryService = new ProductCategoryService();
        this.productLineService = new ProductLineService();
    }

    @FXML
    public void initialize() {
        carregarCategorias();
        carregarLinhas();
        validarCampos();
    }


    @FXML
    private void updateButtonText() {
        if (produtoEditando != null) {
            btnSalvar.setText("Editar");
        } else {
            btnSalvar.setText("Salvar");
        }
    }

    private Produto criarProduto() {
        Produto novoProduto = new Produto();
        novoProduto.setName(nomeField.getText());
        ProductCategory categoriaSelecionada = categorias.stream()
                .filter(c -> c.getName().equals(categoriaComboBox.getValue()))
                .findFirst()
                .orElse(null);
        ProductLine linhaSelecionada = linhas.stream()
                .filter(l -> l.getName().equals(linhaComboBox.getValue()))
                .findFirst()
                .orElse(null);
        novoProduto.setCategoryId(categoriaSelecionada != null ? categoriaSelecionada.getId() : null);
        novoProduto.setLineId(linhaSelecionada != null ? linhaSelecionada.getId() : null);
        novoProduto.setCost(new BigDecimal(custoField.getText().replace(",", ".")));
        novoProduto.setPrice(new BigDecimal(precoUnitarioField.getText().replace(",", ".")));
        novoProduto.setUnit(unidadeField.getText());
        novoProduto.setBrand(marcaField.getText());
        novoProduto.setNcm(ncmField.getText());
        return novoProduto;
    }

    @FXML
    private void handleSalvar() {
        if (validarFormulario()) {
            Produto novoProduto = criarProduto();

            try {
                String token = TokenManager.getInstance().getToken();
                if (produtoEditando != null) {
                    novoProduto.setId(produtoEditando.getId());
                    novoProduto.setActive(true);
                    produtoService.editarProduto(novoProduto, token);
                } else {
                    produtoService.criarProduto(novoProduto, token);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Falha ao criar produto");
                System.out.println(e.getMessage());
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }

            clearFieldForm();
            carregarTela("/views/Produtos/Produtos.fxml");
        }
    }

    private void carregarTela(String caminho){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
            Parent view = loader.load();
            rootPane.getChildren().clear();
            rootPane.getChildren().add(view);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar tela: " + e.getMessage());
        }
    }


    //----------------- validações ----------------//
    private boolean validarFormulario() {
        boolean isValid = true;

        if (nomeField.getText().trim().isEmpty()) {
            nomeErrorLabel.setVisible(true);
            nomeErrorLabel.setManaged(true);
            isValid = false;
        }

        if (categoriaComboBox.getValue() == null || categoriaComboBox.getValue().trim().isEmpty()) {
            categoriaErrorLabel.setVisible(true);
            categoriaErrorLabel.setManaged(true);
            isValid = false;
        }

        if (custoField.getText().trim().isEmpty()) {
            custoErrorLabel.setVisible(true);
            custoErrorLabel.setManaged(true);
            isValid = false;
        }

        if (precoUnitarioField.getText().trim().isEmpty()) {
            precoUnitarioErrorLabel.setVisible(true);
            precoUnitarioErrorLabel.setManaged(true);
            isValid = false;
        }

        if (marcaField.getText().trim().isEmpty()) {
            marcaErrorLabel.setVisible(true);
            marcaErrorLabel.setManaged(true);
            isValid = false;
        }

        if (linhaComboBox.getValue() == null || linhaComboBox.getValue().trim().isEmpty()) {
            linhaErrorLabel.setVisible(true);
            linhaErrorLabel.setManaged(true);
            isValid = false;
        }

        if (unidadeField.getText().trim().isEmpty()) {
            unidadeErrorLabel.setVisible(true);
            unidadeErrorLabel.setManaged(true);
            isValid = false;
        }

        if (ncmField.getText().trim().isEmpty()) {
            ncmErrorLabel.setVisible(true);
            ncmErrorLabel.setManaged(true);
            isValid = false;
        }

        if (descricaoField.getText().trim().isEmpty()) {
            descricaoErrorLabel.setVisible(true);
            descricaoErrorLabel.setManaged(true);
            isValid = false;
        }

        if (cestField.getText().trim().isEmpty()) {
            cestErrorLabel.setVisible(true);
            cestErrorLabel.setManaged(true);
            isValid = false;
        }

        if (gtinField.getText().trim().isEmpty()) {
            gtinErrorLabel.setVisible(true);
            gtinErrorLabel.setManaged(true);
            isValid = false;
        }

        return isValid;
    }

    @FXML
    private void handleCancelar() {
        clearFieldForm();
        this.produtoEditando = null;
        carregarTela("/views/Produtos/Produtos.fxml");
    }

    /*------------------- produtos --------------*/

    public void setProdutoEditando(String id) {
        try{
            String token = TokenManager.getInstance().getToken();
            produtoEditando = produtoService.buscarProdutoId(id, token);
            updateButtonText();
            preencherCampos(produtoEditando);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar produto" + e.getMessage());
        }
    }

    private void preencherCampos(Produto produto) {
        nomeField.setText(produto.getName());
        categoriaComboBox.setValue(produto.getCategory() != null ? produto.getCategory().getName() : null);
        custoField.setText(produto.getCost() != null ? produto.getCost().setScale(2, RoundingMode.HALF_UP).toString().replace(".", ",") : "");
        precoUnitarioField.setText(produto.getPrice() != null ? produto.getPrice().setScale(2, RoundingMode.HALF_UP).toString().replace(".", ",") : "");
        marcaField.setText(produto.getBrand());
        linhaComboBox.setValue(produto.getLine() != null ? produto.getLine().getName() : null);
        unidadeField.setText(produto.getUnit());
        ncmField.setText(produto.getNcm());
    }


    private void carregarCategorias() {
        try {
            categorias = productCategoryService.buscarCategorias();
            categoriaComboBox.setItems(FXCollections.observableArrayList(
                    categorias.stream().map(ProductCategory::getName).toList()
            ));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar categorias: " + e.getMessage());
        }
    }

    private void carregarLinhas() {
        try {
            linhas = productLineService.buscarLinhas();
            linhaComboBox.setItems(FXCollections.observableArrayList(
                    linhas.stream().map(ProductLine::getName).toList()
            ));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar linhas: " + e.getMessage());
        }
    }

    private void clearFieldForm() {
        nomeField.clear();
        categoriaComboBox.setValue(null);
        unidadeField.clear();
        precoUnitarioField.clear();
        custoField.clear();
        marcaField.clear();
        linhaComboBox.setValue(null);
        ncmField.clear();
        descricaoField.clear();
        cestField.clear();
        gtinField.clear();
    }


    /*--------- validações ----------*/

    private void validarCampos() {
        // Validar números
        configurarValidacaoDinheiro(custoField, custoErrorLabel);
        configurarValidacaoDinheiro(precoUnitarioField, precoUnitarioErrorLabel);
        configurarValidacaoNumerica(cestField, cestErrorLabel);
        configurarValidacaoNumerica(ncmField, ncmErrorLabel);
        configurarValidacaoNumerica(gtinField, gtinErrorLabel);

        // Validar texto
        configurarValidacaoTexto(nomeField, nomeErrorLabel);
        nomeField.textProperty().addListener((obs, oldText, newText) -> {
            nomeErrorLabel.setVisible(newText.trim().isEmpty());
        });

        configurarValidacaoTexto(categoriaComboBox.getEditor(), categoriaErrorLabel);
        categoriaComboBox.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            categoriaErrorLabel.setVisible(newText.trim().isEmpty());
        });

        configurarValidacaoTexto(marcaField, marcaErrorLabel);
        configurarValidacaoTexto(unidadeField, unidadeErrorLabel);
        configurarValidacaoTexto(descricaoField, descricaoErrorLabel);
        configurarValidacaoTexto(ncmField, ncmErrorLabel);
        configurarValidacaoTexto(linhaComboBox.getEditor(), linhaErrorLabel);
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

    private void configurarValidacaoNumerica(TextField textField, Label errorLabel) {
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
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
}
