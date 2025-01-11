package edu.pies.sysaguaapp.controllers;
import edu.pies.sysaguaapp.models.ClientesCadastro;
import edu.pies.sysaguaapp.services.ClientesService;
import edu.pies.sysaguaapp.services.TokenManager;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.math.BigDecimal;
import java.util.List;

public class ClientesController {

    private final ClientesService clienteService;
    private ObservableList<ClientesCadastro> clientesObservable;

    @FXML
    private StackPane rootPane;

    @FXML
    private Label successMessage;

    @FXML
    private TextField nomeCompleto_RazaoSocialField;

    @FXML
    private TextField EnderecoCompletoField;

    @FXML
    private TextField telefoneField;

    @FXML
    private TextField CNPJField;

    @FXML
    private TextField EmailField;

    @FXML
    private Label nomeCompleto_RazaoSocialErrorLabel;

    @FXML
    private BorderPane formCadastroClientes;

    @FXML
    private Button btnAnterior;

    @FXML
    private Rectangle overlay;

    @FXML
    private Button btnProximo;

    @FXML
    private VBox listClienteView;

    @FXML
    private VBox detailsFormCliente;

    @FXML
    private HBox paginationContainer;


    @FXML
    private TableView<ClientesCadastro> tabelaClientes;


    private int paginaAtual = 0;
    private final int itensPorPagina = 18;
    private int totalPaginas;


    public ClientesController() {
        this.clienteService = new ClientesService();
        this.clientesObservable = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        configurarTabela();
        carregarClientes();


        configurarValidacaoTexto(nomeCompleto_RazaoSocialField);
        nomeCompleto_RazaoSocialField.textProperty().addListener((obs, oldText, newText) -> {
            nomeCompleto_RazaoSocialErrorLabel.setVisible(newText.trim().isEmpty());
        });

        configurarValidacaoTexto(EnderecoCompletoField);
        configurarValidacaoNumerica(telefoneField);
        configurarValidacaoNumerica(CNPJField);
        configurarValidacaoTexto(EmailField);

    }

    private void showOverlay() {
        overlay.maxWidth(rootPane.widthProperty().get());
        overlay.maxHeight(rootPane.heightProperty().get());
        overlay.widthProperty().bind(rootPane.widthProperty());
        overlay.heightProperty().bind(rootPane.heightProperty());
        overlay.setVisible(true);
        overlay.setManaged(true);
    }

    private void hideOverlay() {
        overlay.setVisible(false);
        overlay.setManaged(false);
    }

    @FXML
    private void handleSalvar() {
        String nomeCompletoRazaoSocial = nomeCompleto_RazaoSocialField.getText();
        String enderecoCompleto = EnderecoCompletoField.getText();
        Long telefone = Long.parseLong(telefoneField.getText());
        Long cnpj = Long.parseLong(CNPJField.getText());
        String email = EmailField.getText();

        //Fazer o tratamento correto de texto e numeros

        ClientesCadastro novoCliente = new ClientesCadastro();
        novoCliente.setNomeCompleto_RazaoSocial(nomeCompletoRazaoSocial);
        novoCliente.setEnderecoCompleto(enderecoCompleto);
        novoCliente.setTelefone(telefone);
        novoCliente.setCnpj(cnpj);
        novoCliente.setEmail(email);

        try {
            String token = TokenManager.getInstance().getToken();
            ClientesService.criarCliente(novoCliente, token);
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Falha ao criar cliente");
            System.out.println(e.getMessage());
            alert.setContentText(e.getMessage());
            hideForm();
            hideOverlay();
            alert.showAndWait();
        }

        hideForm();
        hideOverlay();
        clearFieldForm();
        listClienteView.setDisable(false);
        showSucessMessage();
        carregarClientes();
    }
    @FXML
    private void handleCancelar() {
        hideForm();
        hideOverlay();
        listClienteView.setDisable(false);
    }

    @FXML
    private void handleDetailsClienteModal() {
        if (!detailsFormCliente.isVisible()) {
            showModal(detailsFormCliente);
        }
    }

    private void showModal(VBox toShow) {
        // Exibe o modal atual
        toShow.setManaged(true);
        toShow.toFront();
        toShow.setVisible(true);
    }

    @FXML
    private void hideDetailsModal(VBox root) {
        root.setManaged(false);
        root.setVisible(false);
    }

    @FXML
    private void handleAddCliente() {
        if (!formCadastroClientes.isVisible()) {
            showOverlay();
            showForm();
            listClienteView.setDisable(true);
        }
    }

    private void showForm() {
        formCadastroClientes.setManaged(true);
        formCadastroClientes.toFront();
        formCadastroClientes.setVisible(true);
    }

    private void hideForm() {
        formCadastroClientes.setVisible(false);
        formCadastroClientes.setManaged(false);
    }

    private void carregarClientes() {
        try {
            String token = TokenManager.getInstance().getToken();
            List<ClientesCadastro> todosClientes = clienteService.buscarClientes(token);

            // Calcula o total de páginas
            totalPaginas = (int) Math.ceil((double) todosClientes.size() / itensPorPagina);

            // Filtra os produtos para a página atual
            int inicio = paginaAtual * itensPorPagina;
            int fim = Math.min(inicio + itensPorPagina, todosClientes.size());
            List<ClientesCadastro> clientesPagina = todosClientes.subList(inicio, fim);

            // Atualiza a lista observável
            clientesObservable.setAll(clientesPagina);
            atualizarEstadoBotoes();
            atualizarPaginacao();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar clientes: " + e.getMessage());
        }
    }

    private void atualizarPaginacao() {
        paginationContainer.getChildren().clear();
        paginationContainer.getChildren().add(btnAnterior);

        for (int i = 0; i < totalPaginas; i++) {
            int indice = i;
            Button botaoPagina = new Button(String.valueOf(i + 1));
            botaoPagina.setOnAction(event -> irParaPagina(indice));
            paginationContainer.getChildren().add(botaoPagina);
        }

        paginationContainer.getChildren().add(btnProximo);
    }

    private void atualizarEstadoBotoes() {
        btnAnterior.setDisable(paginaAtual == 0);
        btnProximo.setDisable(paginaAtual >= totalPaginas - 1);
    }

    private void irParaPagina(int pagina) {
        paginaAtual = pagina;
        carregarClientes();
    }

    @FXML
    private void handlePaginaAnterior() {
        if (paginaAtual > 0) {
            paginaAtual--;
            carregarClientes();
        }
    }

    @FXML
    private void handleProximaPagina() {
        if (paginaAtual < totalPaginas - 1) {
            paginaAtual++;
            carregarClientes();
        }
    }

    private void clearFieldForm() {
        nomeCompleto_RazaoSocialField.clear();
        EnderecoCompletoField.clear();
        telefoneField.clear();
        CNPJField.clear();
        EmailField.clear();
    }


    private void configurarTabela() {
        // Configuração das colunas da tabela
        TableColumn<ClientesCadastro, String> colunaNomeCompleto_RazaoSocial = new TableColumn<>("Nome Completo ou Razão Social");
        colunaNomeCompleto_RazaoSocial.setCellValueFactory(new PropertyValueFactory<>("name_razao_social"));

        TableColumn<ClientesCadastro, String> colunaEnderecoCompleto = new TableColumn<>("Endereço Completo");
        colunaEnderecoCompleto.setCellValueFactory(new PropertyValueFactory<>("endereco"));

        TableColumn<ClientesCadastro, Long> colunaTelefone = new TableColumn<>("Telefone");
        colunaTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));

        TableColumn<ClientesCadastro, Long> colunaCnpj = new TableColumn<>("CNPJ");
        colunaCnpj.setCellValueFactory(new PropertyValueFactory<>("cnpj"));

        TableColumn<ClientesCadastro, String> colunaEmail = new TableColumn<>("Email");
        colunaEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        tabelaClientes.getColumns().addAll(colunaNomeCompleto_RazaoSocial, colunaEnderecoCompleto, colunaTelefone , colunaCnpj, colunaEmail);
        tabelaClientes.setItems(clientesObservable);
    }

    /*--------- validações ----------*/

    private void configurarValidacaoNumerica(TextField textField) {
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            if (change.getText().matches("\\d*([.]\\d*)?")) { // Permite números e ponto decimal
                return change;
            }
            return null; // Rejeita mudanças inválidas
        });
        textField.setTextFormatter(formatter);
    }

    private void configurarValidacaoTexto(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[\\p{L}\\s\\d.,-]*")) { // Permite letras, espaços, números, '.', ',' e '-'
                textField.setText(oldValue);
            }
        });
    }

    private void showSucessMessage() {
        successMessage.setVisible(true);
        successMessage.toFront();

        // Oculta a mensagem após 3 segundos
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> successMessage.setVisible(false));
        pause.play();
    }
}