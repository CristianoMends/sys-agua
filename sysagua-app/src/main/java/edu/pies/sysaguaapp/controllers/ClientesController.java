package edu.pies.sysaguaapp.controllers;
import edu.pies.sysaguaapp.models.Address;
import edu.pies.sysaguaapp.models.Clientes;
import edu.pies.sysaguaapp.services.ClientesService;
import edu.pies.sysaguaapp.services.TokenManager;
import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;

public class ClientesController {


    private final ClientesService clienteService;
    private ObservableList<Clientes> clientesObservable;

    @FXML
    private StackPane rootPane;

    @FXML
    private Label successMessage;

    @FXML
    private TextField nomeField;

    @FXML
    private VBox addressBox;
    @FXML
    private TextField numberField;
    @FXML
    private TextField streetField;
    @FXML
    private TextField neighborhoodField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField stateField;

    private Address address;

    @FXML
    private TextField telefoneField;

    @FXML
    private TextField CNPJField;

    @FXML
    private Label nomeLabelErrorField;

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
    private TableView<Clientes> tabelaClientes;


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
        configurarValidacaoTexto(nomeField);
        configurarValidacaoTexto(numberField);
        configurarValidacaoTexto(streetField);
        configurarValidacaoTexto(neighborhoodField);
        configurarValidacaoTexto(cityField);
        configurarValidacaoTexto(stateField);
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
        try{
            String nome = nomeField.getText();
            String telefoneStr = telefoneField.getText();
            String cnpjStr = CNPJField.getText();
            String numeroStr = numberField.getText();
            String rua = streetField.getText();
            String bairro = neighborhoodField.getText();
            String cidade = cityField.getText();
            String estado = stateField.getText();

            if (nomeField.getText().isEmpty() || CNPJField.getText().isEmpty()) {
                mostrarAlerta("Campos obrigatórios", "Preencha todos os campos obrigatórios.", "");
                return; // Sai do método se a validação falhar
            }

            // Cria o objeto Address
            Address clienteAddress = new Address();
            clienteAddress.setNumber(numeroStr);
            clienteAddress.setStreet(rua);
            clienteAddress.setNeighborhood(bairro);
            clienteAddress.setCity(cidade);
            clienteAddress.setState(estado);
            Clientes novoCliente = new Clientes();
            novoCliente.setName(nome);
            novoCliente.setAddress(clienteAddress);
            novoCliente.setPhone(telefoneStr);
            novoCliente.setCnpj(cnpjStr);

            String token = TokenManager.getInstance().getToken();
            Clientes clienteCriado = ClientesService.criarCliente(novoCliente, token);
            hideForm();
            hideOverlay();
            clearFieldForm();
            listClienteView.setDisable(false);
            showSucessMessage();
        } catch (IOException e) { // Captura exceções mais específicas
            e.printStackTrace();
            mostrarAlerta("Erro de Conexão", "Falha ao criar cliente", "Verifique sua conexão com a internet.");
        } catch (Exception e) { // Captura outras exceções (menos específico)
            e.printStackTrace();
            mostrarAlerta("Erro Inesperado", "Falha ao criar cliente", "Ocorreu um erro inesperado.");
        }
        }
    private void mostrarAlerta(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
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
        nomeLabelErrorField.setVisible(false);
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
            List<Clientes> todosClientes = clienteService.buscarClientes(token);

            // Calcula o total de páginas
            totalPaginas = (int) Math.ceil((double) todosClientes.size() / itensPorPagina);

            // Filtra os produtos para a página atual
            int inicio = paginaAtual * itensPorPagina;
            int fim = Math.min(inicio + itensPorPagina, todosClientes.size());
            List<Clientes> clientesPagina = todosClientes.subList(inicio, fim);

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
//        carregarClientes();
    }

    @FXML
    private void handlePaginaAnterior() {
        if (paginaAtual > 0) {
            paginaAtual--;
//            carregarClientes();
        }
    }

    @FXML
    private void handleProximaPagina() {
        if (paginaAtual < totalPaginas - 1) {
            paginaAtual++;
//            carregarClientes();
        }
    }


    private void configurarTabela() {
        // Configuração das colunas da tabela
        TableColumn<Clientes, String> colunaNome = new TableColumn<>("Nome");
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Clientes, String> colunaNumero = new TableColumn<>("Numero");
        colunaNumero.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress().getNumber()));

        TableColumn<Clientes, String> colunaRua = new TableColumn<>("Rua");
        colunaRua.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress().getStreet()));

        TableColumn<Clientes, String> colunaBairro = new TableColumn<>("Bairro");
        colunaBairro.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress().getNeighborhood()));

        TableColumn<Clientes, String> colunaCidade = new TableColumn<>("Cidade");
        colunaCidade.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress().getCity()));

        TableColumn<Clientes, String> colunaEstado = new TableColumn<>("Estado");
        colunaEstado.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress().getState()));

        TableColumn<Clientes, String> colunaTelefone = new TableColumn<>("Telefone");
        colunaTelefone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        TableColumn<Clientes, String> colunaCnpj = new TableColumn<>("CNPJ");
        colunaCnpj.setCellValueFactory(new PropertyValueFactory<>("cnpj"));

        tabelaClientes.getColumns().addAll(colunaNome, colunaNumero, colunaRua, colunaBairro, colunaCidade, colunaEstado, colunaTelefone , colunaCnpj);
        tabelaClientes.setItems(clientesObservable);
    }

    private void showSucessMessage() {
        successMessage.setVisible(true);
        successMessage.toFront();

        // Oculta a mensagem após 3 segundos
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> successMessage.setVisible(false));
        pause.play();
    }

    private void configurarValidacaoTexto(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[\\p{L}\\s\\d.,-]*")) { // Permite letras, espaços, números, '.', ',' e '-'
                textField.setText(oldValue);
            }
        });
    }
}