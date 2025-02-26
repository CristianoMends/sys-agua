package edu.pies.sysaguaapp.controllers.Usuarios;

import edu.pies.sysaguaapp.enumeration.Usuarios.UserAccess;
import edu.pies.sysaguaapp.enumeration.Usuarios.UserStatus;
import edu.pies.sysaguaapp.models.Usuario;
import edu.pies.sysaguaapp.services.UsuarioService;
import edu.pies.sysaguaapp.services.TokenManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;

import java.util.Arrays;

public class AddUsuarioController {
    private final UsuarioService usuarioService;

    @FXML
    private StackPane rootPane;

    @FXML
    private TextField nomeField, surnameField, phoneField, emailField,senhaField;

    private final String token;

    @FXML
    private ComboBox<UserAccess> acessComboBox;

    @FXML
    private Label nomeErrorLabel, surnameErrorLabel, phoneErrorLabel, emailErrorLabel, senhaErrorLabel;

    @FXML
    private Button btnSalvar, btnCancelar;

    private Usuario usuarioEditando = null;

    public AddUsuarioController() {
        usuarioService = new UsuarioService();
        token = TokenManager.getInstance().getToken();
    }

    @FXML
    public void initialize() {
        validarCampos();
        btnCancelar.setCursor(Cursor.HAND);
        btnSalvar.setCursor(Cursor.HAND);

        acessComboBox.setItems(FXCollections.observableArrayList(
                Arrays.stream(UserAccess.values())
                        .filter(metodo -> metodo != UserAccess.DEVELOPER) // Filtra para remover Desenvolvedor
                        .toList()
        ));
        acessComboBox.setConverter(new StringConverter<UserAccess>() {
            @Override
            public String toString(UserAccess metodo) {
                return metodo != null ? metodo.getDescription() : "";
            }
            @Override
            public UserAccess fromString(String string) {
                return Arrays.stream(UserAccess.values())
                        .filter(metodo -> metodo.getDescription().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
    }

    @FXML
    private void handleSalvar() {
        if (validarFormulario()){
            Usuario novoUsuario = criarUsuario();
            try {
                if (usuarioEditando != null) {
                    novoUsuario.setId(usuarioEditando.getId());
//                    usuarioService.editarUsuario(novoUsuario, token);
                } else {
                    usuarioService.criarUsuario(novoUsuario, token);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Erro, Falha ao salvar usuario: " + e.getMessage());
            }
            usuarioEditando = null;
            clearFieldForm();
            carregarTela("/views/Usuario/Usuario.fxml");
        }

    }

    @FXML
    private void handleCancelar() {
        carregarTela("/views/Usuario/Usuario.fxml");
    }

    @FXML
    private void updateButtonText() {
        if (usuarioEditando != null) {
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

    public void setUsuarioEditando(String id){
        try {
            String token = TokenManager.getInstance().getToken();
            Usuario usuario = usuarioService.buscarUsuarioId(id, token);
            this.usuarioEditando = usuario;
            updateButtonText();
            preencherCampos(usuarioEditando);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar usuario: " + e.getMessage());
        }
    }

    private void preencherCampos(Usuario usuario) {
        nomeField.setText(usuario.getName());
        surnameField.setText(usuario.getSurname());
        phoneField.setText(usuario.getPhone());
        emailField.setText(usuario.getEmail());
    }

    private Usuario criarUsuario(){
        Usuario novoUsuario = new Usuario();
        novoUsuario.setName(nomeField.getText().trim());
        novoUsuario.setSurname(surnameField.getText().trim());
        novoUsuario.setPhone(phoneField.getText().trim());
        novoUsuario.setEmail(emailField.getText().trim());
        novoUsuario.setPassword(senhaField.getText().trim());
        novoUsuario.setAccess(acessComboBox.getValue());
        novoUsuario.setStatus(UserStatus.ACTIVE);
        return novoUsuario;
    }


    //--------------------------- validações -------------------------//

    private boolean validarFormulario() {
        boolean isValid = true;

        if (nomeField.getText().trim().isEmpty()) {
            nomeErrorLabel.setText("Nome é obrigatório.");
            nomeErrorLabel.setVisible(true);
            nomeErrorLabel.setManaged(true);
            isValid = false;
        } else {
            nomeErrorLabel.setVisible(false);
            nomeErrorLabel.setManaged(false);
        }

        if (surnameField.getText().trim().isEmpty()){
            surnameErrorLabel.setText("SobreNome é obrigatório");
            surnameErrorLabel.setVisible(true);
            surnameErrorLabel.setManaged(true);
            isValid = false;
        } else {
            surnameErrorLabel.setVisible(false);
            surnameErrorLabel.setManaged(false);
        }

        if (emailField.getText().trim().isEmpty()){
            emailErrorLabel.setText("E-mail é obrigatório");
            emailErrorLabel.setVisible(true);
            emailErrorLabel.setManaged(true);
            isValid = false;
        } else {
            emailErrorLabel.setVisible(false);
            emailErrorLabel.setManaged(false);
        }

        if (senhaField.getText().trim().isEmpty()){
            senhaErrorLabel.setText("Senha é obrigatório");
            senhaErrorLabel.setVisible(true);
            senhaErrorLabel.setManaged(true);
            isValid = false;
        } else if (!senhaField.getText().matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,}$")) {
            senhaErrorLabel.setText("A senha deve ter pelo menos 8 caracteres, incluir uma letra maiúscula, uma minúscula, um número e um caractere especial.");
            senhaErrorLabel.setWrapText(true);
            senhaErrorLabel.setMaxWidth(300);
            senhaErrorLabel.setVisible(true);
            senhaErrorLabel.setManaged(true);
            isValid = false;
        } else {
            senhaErrorLabel.setVisible(false);
            senhaErrorLabel.setManaged(false);
            isValid = true;
        }

        if (phoneField.getText().trim().isEmpty() || !phoneField.getText().matches("\\d{11}")) {
            phoneErrorLabel.setText("Telefone inválido. Deve conter 11 dígitos.");
            phoneErrorLabel.setVisible(true);
            phoneErrorLabel.setManaged(true);
            isValid = false;
        } else {
            phoneErrorLabel.setVisible(false);
            phoneErrorLabel.setManaged(false);
        }

        return isValid;
    }

    private void validarCampos() {
        configurarValidacaoTexto(nomeField, nomeErrorLabel);
        configurarValidacaoTexto(surnameField, surnameErrorLabel);
        configurarValidacaoTelefone(phoneField, phoneErrorLabel);
        configurarValidacaoEmail(emailField, emailErrorLabel);
        configurarValidacaoSenha(senhaField, senhaErrorLabel);
    }

    private static void configurarValidacaoEmail(TextField textField, Label emailErrorLabel){
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isEmailValido(newValue)) {
                emailErrorLabel.setText(""); // Remove a mensagem de erro se for válido
            } else {
                emailErrorLabel.setText("E-mail inválido");
            }
        });
    }

    private static boolean isEmailValido(String email) {
        String regexEmail = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(regexEmail);
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

    private void configurarValidacaoSenha(TextField textField, Label errorLabel) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Verifica se a senha atende aos critérios de segurança
            if (!newValue.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,}$")) {
                // A senha deve ter pelo menos 8 caracteres, incluir pelo menos uma letra maiúscula, uma minúscula, um número e um caractere especial
                senhaErrorLabel.setVisible(true);
                senhaErrorLabel.setManaged(true);
            } else {
                senhaErrorLabel.setVisible(false);
                senhaErrorLabel.setManaged(false);
            }
        });
    }

    private void clearFieldForm(){
        nomeField.clear();
        surnameField.clear();
        phoneField.clear();
        emailField.clear();
        senhaField.clear();
        acessComboBox.getSelectionModel().clearSelection();
    }

}