package edu.pies.sysaguaapp.controllers.fornecedor;

import edu.pies.sysaguaapp.models.Address;
import edu.pies.sysaguaapp.models.Fornecedor;
import edu.pies.sysaguaapp.models.Produto;
import edu.pies.sysaguaapp.services.FornecedorService;
import edu.pies.sysaguaapp.services.TokenManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;

import java.math.RoundingMode;
import java.util.List;

public class AddFornecedorController {
    private final FornecedorService fornecedorService;
    private Address address;

    @FXML
    private StackPane rootPane;

    @FXML
    private TextField razaoSocialField, fantasiaField, cnpjField, telefoneField;

    @FXML
    private TextField inscEstadualField, inscMunicipalField;

    @FXML
    private TextField numeroField, logradouroField, bairroField, cidadeField, estadoField;

    @FXML
    private Label numeroErrorLabel, logradErrorLabel, bairroErrorLabel, cidadeErrorLabel, estadoErrorLabel;

    @FXML
    private Label telefoneErrorLabel,cnpjErrorLabel,razaoSocialErrorLabel;

    @FXML
    private Label inscEstadualErrorLabel, inscMunicipalErrorLabel;

    @FXML
    private Button btnSalvar, btnCancelar;


    private Fornecedor fornecedorEditando = null;

    public AddFornecedorController() {
        fornecedorService = new FornecedorService();
    }

    @FXML
    public void initialize() {
        validarCampos();
    }

    @FXML
    private void handleSalvar() {
        if (validarFormulario()){
            Fornecedor novoFornecedor = new Fornecedor();
            novoFornecedor.setSocialReason(razaoSocialField.getText().trim());
            novoFornecedor.setPhone("55" + telefoneField.getText().trim());
            novoFornecedor.setCnpj(cnpjField.getText().trim());

            Address endereco = new Address();
            endereco.setNumber(numeroField.getText().trim());
            endereco.setStreet(logradouroField.getText().trim());
            endereco.setNeighborhood(bairroField.getText().trim());
            endereco.setCity(cidadeField.getText().trim());
            endereco.setState(estadoField.getText().trim());

            novoFornecedor.setAddress(endereco);

            try {
                String token = TokenManager.getInstance().getToken();
                if (fornecedorEditando != null) {
                    novoFornecedor.setId(fornecedorEditando.getId());
                    fornecedorService.atualizarFornecedor(novoFornecedor, token);
                } else {
                    fornecedorService.cadastrarFornecedor(novoFornecedor, token);
                }


            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Erro, Falha ao salvar fornecedor: " + e.getMessage());
            }

            fornecedorEditando = null;
            clearFieldForm();
            carregarTela("/views/Fornecedor/Fornecedor.fxml");
        }

    }


    @FXML
    private void handleCancelar() {
        carregarTela("/views/Fornecedor/Fornecedor.fxml");
    }

    @FXML
    private void updateButtonText() {
        if (fornecedorEditando != null) {
            btnSalvar.setText("Editar");
        } else {
            btnSalvar.setText("Salvar");
        }
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

    public void setFornecedorEditando(String id){
        try {
            String token = TokenManager.getInstance().getToken();
            Fornecedor fornecedor = fornecedorService.buscarFornecedorId(id, token);
            this.fornecedorEditando = fornecedor;
            updateButtonText();
            preencherCampos(fornecedorEditando);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar fornecedor: " + e.getMessage());
        }
    }

    private void preencherCampos(Fornecedor fornecedor) {
        razaoSocialField.setText(fornecedor.getSocialReason());
        cnpjField.setText(fornecedor.getCnpj());
        telefoneField.setText(fornecedor.getPhone());
        numeroField.setText(fornecedor.getAddress().getNumber());
        logradouroField.setText(fornecedor.getAddress().getStreet());
        bairroField.setText(fornecedor.getAddress().getNeighborhood());
        cidadeField.setText(fornecedor.getAddress().getCity());
        estadoField.setText(fornecedor.getAddress().getState());
    }


    //--------------------------- validações -------------------------//

    private boolean validarFormulario() {
        boolean isValid = true;

        // Validação do número
        if (numeroField.getText().trim().isEmpty()) {
            numeroErrorLabel.setText("Número é obrigatório.");
            numeroErrorLabel.setVisible(true);
            numeroErrorLabel.setManaged(true);
            isValid = false;
        } else {
            numeroErrorLabel.setVisible(false);
            numeroErrorLabel.setManaged(false);
        }

        // Validação da rua
        if (logradouroField.getText().trim().isEmpty()) {
            logradErrorLabel.setText("Rua é obrigatória.");
            logradErrorLabel.setVisible(true);
            logradErrorLabel.setManaged(true);
            isValid = false;
        } else {
            logradErrorLabel.setVisible(false);
            logradErrorLabel.setManaged(false);
        }

        // Validação do bairro
        if (bairroField.getText().trim().isEmpty()) {
            bairroErrorLabel.setText("Bairro é obrigatório.");
            bairroErrorLabel.setVisible(true);
            bairroErrorLabel.setManaged(true);
            isValid = false;
        } else {
            bairroErrorLabel.setVisible(false);
            bairroErrorLabel.setManaged(false);
        }

        // Validação da cidade
        if (cidadeField.getText().trim().isEmpty()) {
            cidadeErrorLabel.setText("Cidade é obrigatória.");
            cidadeErrorLabel.setVisible(true);
            cidadeErrorLabel.setManaged(true);
            isValid = false;
        } else {
            cidadeErrorLabel.setVisible(false);
            cidadeErrorLabel.setManaged(false);
        }

        // Validação do estado
        if (estadoField.getText().trim().isEmpty()) {
            estadoErrorLabel.setText("Estado é obrigatório.");
            estadoErrorLabel.setVisible(true);
            estadoErrorLabel.setManaged(true);
            isValid = false;
        } else {
            estadoErrorLabel.setVisible(false);
            estadoErrorLabel.setManaged(false);
        }

        // Validação do telefone
        if (telefoneField.getText().trim().isEmpty() || !telefoneField.getText().matches("\\d{11}")) {
            telefoneErrorLabel.setText("Telefone inválido. Deve conter 11 dígitos.");
            telefoneErrorLabel.setVisible(true);
            telefoneErrorLabel.setManaged(true);
            isValid = false;
        } else {
            telefoneErrorLabel.setVisible(false);
            telefoneErrorLabel.setManaged(false);
        }

        // Validação do CNPJ
        if (cnpjField.getText().trim().isEmpty()) {
            cnpjErrorLabel.setText("CNPJ inválido.");
            cnpjErrorLabel.setVisible(true);
            cnpjErrorLabel.setManaged(true);
            isValid = false;
        } else {
            cnpjErrorLabel.setVisible(false);
            cnpjErrorLabel.setManaged(false);
        }

        // Validação da razão social
        if (razaoSocialField.getText().trim().isEmpty()) {
            razaoSocialErrorLabel.setText("Razão Social é obrigatória.");
            razaoSocialErrorLabel.setVisible(true);
            razaoSocialErrorLabel.setManaged(true);
            isValid = false;
        } else {
            razaoSocialErrorLabel.setVisible(false);
            razaoSocialErrorLabel.setManaged(false);
        }

        return isValid;
    }

    private void validarCampos() {
        // Validar números
        configurarValidacaoTelefone(telefoneField, telefoneErrorLabel);
        configurarValidacaoNumerica(numeroField, numeroErrorLabel);
        configurarValidacaoNumerica(inscEstadualField, inscEstadualErrorLabel);
        configurarValidacaoNumerica(inscMunicipalField, inscMunicipalErrorLabel);
        configurarFormatacaoCNPJ(cnpjField, cnpjErrorLabel);

        // Validar texto
        configurarValidacaoTexto(razaoSocialField, razaoSocialErrorLabel);
        configurarValidacaoTexto(logradouroField, logradErrorLabel);
        configurarValidacaoTexto(bairroField, bairroErrorLabel);
        configurarValidacaoTexto(cidadeField, cidadeErrorLabel);
        configurarValidacaoEstado(estadoField, estadoErrorLabel);
    }

    private void configurarFormatacaoCNPJ(TextField textField, Label errorLabel) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Remove qualquer caractere não numérico
            String digits = newValue.replaceAll("[^\\d]", "");

            // Limita o comprimento do CNPJ a 14 caracteres
            if (digits.length() > 14) {
                digits = digits.substring(0, 14);
            }

            // Aplica a formatação do CNPJ (XX.XXX.XXX/XXXX-XX)
            StringBuilder formattedCNPJ = new StringBuilder(digits);
            if (digits.length() > 2) {
                formattedCNPJ.insert(2, ".");
            }
            if (digits.length() > 5) {
                formattedCNPJ.insert(6, ".");
            }
            if (digits.length() > 8) {
                formattedCNPJ.insert(10, "/");
            }
            if (digits.length() > 12) {
                formattedCNPJ.insert(15, "-");
            }

            // Só aplica a formatação se o texto alterado for diferente
            if (!newValue.equals(formattedCNPJ.toString())) {
                textField.setText(formattedCNPJ.toString());
                // Coloca o cursor na posição correta
                textField.positionCaret(formattedCNPJ.length());
            }

            // Exibe a validação do erro se o CNPJ não tiver 18 caracteres
            if (formattedCNPJ.length() == 18) {
                errorLabel.setVisible(false);
                errorLabel.setManaged(false);
            } else {
                errorLabel.setText("CNPJ inválido.");
                errorLabel.setVisible(true);
                errorLabel.setManaged(true);
            }
        });
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

    private void configurarValidacaoTelefone(TextField textField, Label errorLabel) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Remove qualquer caractere não numérico
            String digits = newValue.replaceAll("[^\\d]", "");

            // Limita o comprimento do telefone a 11 caracteres
            if (digits.length() > 11) {
                digits = digits.substring(0, 11);
            }

            textField.setText(digits);

            if (digits.length() == 11) {
                errorLabel.setVisible(false);
                errorLabel.setManaged(false);
            } else {
                errorLabel.setText("Telefone inválido.");
                errorLabel.setVisible(true);
                errorLabel.setManaged(true);
            }
        });
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

    private void configurarValidacaoEstado(TextField textField, Label errorLabel) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue.length() > 2){
                newValue = newValue.substring(0, 2);
            }

            textField.setText(newValue);

            if (!newValue.matches("\\p{L}*")) { // Permite letras, espaços, números, '.', ',' e '-'
                textField.setText(oldValue);
                errorLabel.setVisible(true);
                errorLabel.setManaged(true);
            } else {
                errorLabel.setVisible(false);
                errorLabel.setManaged(false);
            }
        });
    }

    private void clearFieldForm(){
        razaoSocialField.clear();
        fantasiaField.clear();
        cnpjField.clear();
        telefoneField.clear();
        numeroField.clear();
        logradouroField.clear();
        bairroField.clear();
        cidadeField.clear();
        estadoField.clear();
    }

}
