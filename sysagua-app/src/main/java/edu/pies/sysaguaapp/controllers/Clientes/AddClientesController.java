package edu.pies.sysaguaapp.controllers.Clientes;

import edu.pies.sysaguaapp.models.Address;
import edu.pies.sysaguaapp.models.Clientes;
import edu.pies.sysaguaapp.services.ClientesService;
import edu.pies.sysaguaapp.services.TokenManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;

public class AddClientesController {

    private final ClientesService clienteService;
    private Address address;

    @FXML
    private StackPane rootPane;

    @FXML
    private TextField nomeField, numberField, streetField, neighborhoodField, cityField, stateField, telefoneField, CNPJField;

    @FXML
    private Label nomeErrorLabel, numberErrorLabel, streetErrorLabel, neighborhoodErrorLabel, cityErrorLabel, stateErrorLabel, telefoneErrorLabel, CNPJErrorLabel;

    @FXML
    private Button btnSalvar, btnCancelar;

    private Clientes clienteEditado = null;

    public AddClientesController() {
        clienteService = new ClientesService();

    }

    @FXML
    public void initialize() {
        validarCampos();
        btnCancelar.setCursor(Cursor.HAND);
        btnSalvar.setCursor(Cursor.HAND);
    }

    @FXML
    private void handleSalvar() {
        if(validarFormulario()){
            Clientes novoCliente = criarCliente();
            try {
                String token = TokenManager.getInstance().getToken();
                if (clienteEditado != null) {
                    novoCliente.setId(clienteEditado.getId());
                    ClientesService.editarCliente(novoCliente, token);

                } else {
                    ClientesService.criarCliente(novoCliente, token);
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Erro, Falha ao salvar cliente: " + e.getMessage());
            }
            clienteEditado = null;
            clearFieldForm();
            carregarTela("/views/Clientes/Clientes.fxml");
        }
    }

    @FXML
    private void handleCancelar() {
        carregarTela("/views/Clientes/Clientes.fxml");
    }


    @FXML
    private void updateButtonText() {
        if (clienteEditado != null) {
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

    public void setClienteEditando(String id){
        try {
            String token = TokenManager.getInstance().getToken();
            Clientes cliente = clienteService.buscarClienteId(id, token);
            this.clienteEditado = cliente;
            updateButtonText();
            preencherCampos(clienteEditado);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar cliente: " + e.getMessage());
        }
    }

    private void preencherCampos(Clientes clientes) {
        nomeField.setText(clientes.getName());
        telefoneField.setText(clientes.getPhone());
        CNPJField.setText(clientes.getCnpj());
        numberField.setText(clientes.getAddress().getNumber());
        streetField.setText(clientes.getAddress().getStreet());
        neighborhoodField.setText(clientes.getAddress().getNeighborhood());
        cityField.setText(clientes.getAddress().getCity());
        stateField.setText(clientes.getAddress().getState());
    }

    private Clientes criarCliente(){
        Clientes novoCliente = new Clientes();
        novoCliente.setName(nomeField.getText().trim());
        novoCliente.setPhone("55" + telefoneField.getText().trim());
        novoCliente.setCnpj(CNPJField.getText().trim());

        Address endereco = new Address();
        endereco.setNumber(numberField.getText().trim());
        endereco.setStreet(streetField.getText().trim());
        endereco.setNeighborhood(neighborhoodField.getText().trim());
        endereco.setCity(cityField.getText().trim());
        endereco.setState(stateField.getText().trim());

        novoCliente.setAddress(endereco);

        return novoCliente;
    }



//    validações

    private boolean validarFormulario() {
        boolean isValid = true;

        if(nomeField.getText().trim().isEmpty()){
            nomeErrorLabel.setText("Nome da empresa é obrigatório");
            nomeErrorLabel.setVisible(true);
            nomeErrorLabel.setManaged(true);
            isValid = false;
        } else {
            nomeErrorLabel.setVisible(false);
            nomeErrorLabel.setManaged(false);
        }

        // Validação do número
        if (numberField.getText().trim().isEmpty()) {
            numberErrorLabel.setText("Número é obrigatório.");
            numberErrorLabel.setVisible(true);
            numberErrorLabel.setManaged(true);
            isValid = false;
        } else {
            numberErrorLabel.setVisible(false);
            numberErrorLabel.setManaged(false);
        }

        // Validação da rua
        if (streetField.getText().trim().isEmpty()) {
            streetErrorLabel.setText("Bairro é obrigatória.");
            streetErrorLabel.setVisible(true);
            streetErrorLabel.setManaged(true);
            isValid = false;
        } else {
            streetErrorLabel.setVisible(false);
            streetErrorLabel.setManaged(false);
        }

        // Validação do bairro
        if (neighborhoodField.getText().trim().isEmpty()) {
            neighborhoodErrorLabel.setText("Logradouro é obrigatório.");
            neighborhoodErrorLabel.setVisible(true);
            neighborhoodErrorLabel.setManaged(true);
            isValid = false;
        } else {
            neighborhoodErrorLabel.setVisible(false);
            neighborhoodErrorLabel.setManaged(false);
        }

        // Validação da cidade
        if (cityField.getText().trim().isEmpty()) {
            cityErrorLabel.setText("Cidade é obrigatória.");
            cityErrorLabel.setVisible(true);
            cityErrorLabel.setManaged(true);
            isValid = false;
        } else {
            cityErrorLabel.setVisible(false);
            cityErrorLabel.setManaged(false);
        }

        // Validação do estado
        if (stateField.getText().trim().isEmpty()) {
            stateErrorLabel.setText("Estado é obrigatório.");
            stateErrorLabel.setVisible(true);
            stateErrorLabel.setManaged(true);
            isValid = false;
        } else {
            stateErrorLabel.setVisible(false);
            stateErrorLabel.setManaged(false);
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
        if (CNPJField.getText().trim().isEmpty()) {
            CNPJErrorLabel.setText("CNPJ inválido.");
            CNPJErrorLabel.setVisible(true);
            CNPJErrorLabel.setManaged(true);
            isValid = false;
        } else {
            CNPJErrorLabel.setVisible(false);
            CNPJErrorLabel.setManaged(false);
        }

        return isValid;
    }

    private void validarCampos() {
        // Validar números
        configurarValidacaoTelefone(telefoneField, telefoneErrorLabel);
        configurarValidacaoNumerica(numberField, numberErrorLabel);
        configurarFormatacaoCNPJ(CNPJField, CNPJErrorLabel);

        // Validar texto
        configurarValidacaoTexto(nomeField, nomeErrorLabel);
        configurarValidacaoTexto(neighborhoodField, neighborhoodErrorLabel);
        configurarValidacaoTexto(streetField, streetErrorLabel);
        configurarValidacaoTexto(cityField, cityErrorLabel);
        configurarValidacaoEstado(stateField, stateErrorLabel);
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
    private void clearFieldForm() {
        nomeField.clear();
        telefoneField.clear();
        CNPJField.clear();
        numberField.clear();
        streetField.clear();
        neighborhoodField.clear();
        cityField.clear();
        stateField.clear();
    }
}