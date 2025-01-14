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

import java.util.List;

public class ClientesController {

    private final ClientesService clienteService;
    private ObservableList<ClientesCadastro> clientesObservable;

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

    private ClientesCadastro.Address address;

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
        String nome = nomeField.getText();
        String telefoneStr = telefoneField.getText();
        String cnpjStr = CNPJField.getText();
        String numeroStr = numberField.getText();
        String rua = streetField.getText();
        String bairro = neighborhoodField.getText();
        String cidade = cityField.getText();
        String estado = stateField.getText();

        // Validações iniciais (campos obrigatórios)
        if (nome == null || nome.isEmpty() ||
                telefoneStr == null || telefoneStr.isEmpty() ||
                cnpjStr == null || cnpjStr.isEmpty() ||
                numeroStr == null || numeroStr.isEmpty() ||
                rua == null || rua.isEmpty() ||
                bairro == null || bairro.isEmpty() ||
                cidade == null || cidade.isEmpty() ||
                estado == null || estado.isEmpty()) {

            mostrarAlerta("Erro", "Campos obrigatórios", "Por favor, preencha todos os campos.");
            return;
        }

        // Validação com Regex (Telefone)
        if (!validarTelefone(telefoneStr)) {
            mostrarAlerta("Erro", "Telefone inválido", "Por favor, insira um telefone no formato (XX)XXXXXXXX ou (XX)XXXXXXXXX.");
            return;
        }

        // Validação com Regex (CNPJ)
        if (!validarCNPJ(cnpjStr)) {
            mostrarAlerta("Erro", "CNPJ inválido", "Por favor, insira um CNPJ no formato XX.XXX.XXX/XXXX-XX.");
            return;
        }

        Long telefone;
        try {
            telefone = Long.parseLong(telefoneStr.replaceAll("[^\\d]", ""));
        } catch (NumberFormatException e) {
            mostrarAlerta("Erro", "Telefone inválido", "Erro ao processar o telefone. Verifique o formato.");
            return;
        }

        Long cnpj;
        try {
            cnpj = Long.parseLong(cnpjStr.replaceAll("[^\\d]", ""));
        } catch (NumberFormatException e) {
            mostrarAlerta("Erro", "CNPJ inválido", "Erro ao processar o CNPJ. Verifique o formato.");
            return;
        }

        Long numero;
        try {
            numero = Long.parseLong(numeroStr);
        } catch (NumberFormatException e) {
            mostrarAlerta("Erro", "Número inválido", "Por favor, insira um número válido.");
            return;
        }

        // Cria o objeto Address
        ClientesCadastro.Address address = new ClientesCadastro.Address(); // Inicializa aqui!!
        address.setNumber(numero);
        address.setStreet(rua);
        address.setNeighborhood(bairro);
        address.setCity(cidade);
        address.setState(estado);

        ClientesCadastro novoCliente = new ClientesCadastro();
        novoCliente.setName(nome);
        novoCliente.setPhone(telefone);
        novoCliente.setCnpj(cnpj);
        novoCliente.setAddress(address);

        try {
            String token = TokenManager.getInstance().getToken();
            ClientesCadastro clienteCriado = ClientesService.criarCliente(novoCliente, token);

            hideForm();
            hideOverlay();
            clearFieldForm();
            listClienteView.setDisable(false);
            showSucessMessage();
            carregarClientes();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro inesperado", "Falha ao criar cliente", "Ocorreu um erro inesperado. Detalhes: " + e.getMessage());
        }
    }

    private boolean validarTelefone(String telefone) {
        return telefone.matches("\\(\\d{2}\\)\\d{8,9}");
    }

    private boolean validarCNPJ(String cnpj) {
        return cnpj.matches("\\d{2}\\.\\d{3}\\.\\d{3}\\/\\d{4}\\-\\d{2}");
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


    private void configurarTabela() {
        // Configuração das colunas da tabela
        TableColumn<ClientesCadastro, String> colunaNome = new TableColumn<>("Nome");
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("Nome"));

        TableColumn<ClientesCadastro, Long> colunaNumero = new TableColumn<>("Numero");
        colunaNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));

        TableColumn<ClientesCadastro, String> colunaRua = new TableColumn<>("Rua");
        colunaRua.setCellValueFactory(new PropertyValueFactory<>("rua"));

        TableColumn<ClientesCadastro, String> colunaVizinhanca = new TableColumn<>("Vizinhança");
        colunaVizinhanca.setCellValueFactory(new PropertyValueFactory<>("vizinhanca"));

        TableColumn<ClientesCadastro, String> colunaCidade = new TableColumn<>("Cidade");
        colunaCidade.setCellValueFactory(new PropertyValueFactory<>("cidade"));

        TableColumn<ClientesCadastro, String> colunaEstado = new TableColumn<>("Estado");
        colunaEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        TableColumn<ClientesCadastro, Long> colunaTelefone = new TableColumn<>("Telefone");
        colunaTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));

        TableColumn<ClientesCadastro, Long> colunaCnpj = new TableColumn<>("CNPJ");
        colunaCnpj.setCellValueFactory(new PropertyValueFactory<>("cnpj"));

        tabelaClientes.getColumns().addAll(colunaNome, colunaNumero, colunaRua, colunaVizinhanca, colunaCidade, colunaEstado, colunaTelefone , colunaCnpj);
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
}