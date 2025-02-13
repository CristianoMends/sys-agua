package edu.pies.sysaguaapp.controllers;

import edu.pies.sysaguaapp.models.Entregador;
import edu.pies.sysaguaapp.models.Produto;
import edu.pies.sysaguaapp.services.EntregadorService;
import edu.pies.sysaguaapp.services.TokenManager;
import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.beans.property.SimpleStringProperty;


import java.util.List;

public class EntregadorController {
    private final EntregadorService entregadorService;
    private final String token;

    @FXML
    private StackPane rootPane;

    @FXML
    private TableView<Entregador> tabelaEntregador;

    @FXML
    private HBox paginationContainer;

    @FXML
    private Button btnAnterior, btnProximo, btnSalvar, btnCancelar, btnAdicionar;

    @FXML
    private TextField nomeField, telefoneField;

    @FXML
    private BorderPane formCadastroEntregador;

    @FXML
    private VBox detailsFormEntregador;

    @FXML
    private Rectangle overlay;

    @FXML
    private VBox listEntregadorView;

    @FXML
    private Label successMessage, nomeErrorLabel, telefoneErrorLabel;

    @FXML
    private CheckBox exibirInativosCheckBox;

    private Entregador entregadorEditando = null;

    private ObservableList<Entregador> entregadorObservable;
    private int paginaAtual = 0;
    private final int itensPorPagina = 18;
    private int totalPaginas;

    public EntregadorController() {
        this.entregadorService = new EntregadorService();
        token = TokenManager.getInstance().getToken();
        this.entregadorObservable = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        configurarTabela();
        carregarEntregadores();
        showMenuContext();
        validarCampos();
        btnCancelar.setCursor(Cursor.HAND);
        btnSalvar.setCursor(Cursor.HAND);
        btnAdicionar.setCursor(Cursor.HAND);
        exibirInativosCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> carregarEntregadores());

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

    /*---------------------- modal ---------*/

    @FXML
    private void updateButtonText() {
        if (entregadorEditando != null) {
            btnSalvar.setText("Editar");
        } else {
            btnSalvar.setText("Salvar");
        }
    }

    @FXML
    private void handleSalvar() {
        String nome = nomeField.getText();
        String telefone = telefoneField.getText();

        //Fazer o tratamento correto de texto e numeros

        Entregador novoEntregador = new Entregador();
        novoEntregador.setName(nome);
        novoEntregador.setPhone(telefone);

        try {
            if (entregadorEditando != null) {
                novoEntregador.setId(entregadorEditando.getId());
                entregadorService.atualizarEntregador(novoEntregador, token);
                entregadorObservable.set(entregadorObservable.indexOf(entregadorEditando), novoEntregador);
            } else {
                entregadorService.cadastrarEntregador(novoEntregador, token);
                entregadorObservable.add(novoEntregador);
            }


        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Falha ao cadastrar");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

        hideForm();
        hideOverlay();
        clearFieldForm();
        listEntregadorView.setDisable(false);
        showSucessMessage();
        carregarEntregadores();
    }

    @FXML
    private void handleCancelar() {
        hideForm();
        hideOverlay();
        listEntregadorView.setDisable(false);
    }

    /*------------------- entregador --------------*/

    @FXML
    private void handleAddEntregador() {
        if (!formCadastroEntregador.isVisible()) {
            showOverlay();
            showForm();
            listEntregadorView.setDisable(true);

        }
    }

    private void showForm() {
        formCadastroEntregador.setManaged(true);
        formCadastroEntregador.toFront();
        formCadastroEntregador.setVisible(true);
    }

    @FXML
    private void handleDetailsEntregadorModal() {
        if (!detailsFormEntregador.isVisible()) {
            detailsFormEntregador.setManaged(true);
            detailsFormEntregador.toFront();
            detailsFormEntregador.setVisible(true);
        }
    }

    private void hideForm() {
        formCadastroEntregador.setVisible(false);
        formCadastroEntregador.setManaged(false);
    }

    private void showMenuContext(){
        ContextMenu contextMenu = new ContextMenu();

        MenuItem editarItem = new MenuItem("Editar entregador");
        MenuItem inativarItem = new MenuItem("Inativar entregador");

        contextMenu.getItems().addAll(editarItem, inativarItem);

        editarItem.setOnAction(event -> handleEditarEntregador());


        inativarItem.setOnAction(event -> handleInativarEntregador());

        tabelaEntregador.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY && !tabelaEntregador.getSelectionModel().isEmpty()) {
                contextMenu.show(tabelaEntregador, event.getScreenX(), event.getScreenY());
            } else {
                contextMenu.hide();
            }
        });
    }

    private void handleEditarEntregador() {
        Entregador entregadorSelecionado = tabelaEntregador.getSelectionModel().getSelectedItem();

        if (entregadorSelecionado != null) {
            try{
                preencherCampos(entregadorSelecionado);
                entregadorEditando = entregadorSelecionado;
                updateButtonText();
                showOverlay();
                listEntregadorView.setDisable(true);
                showForm();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void preencherCampos(Entregador entregador) {
        nomeField.setText(entregador.getName());
        telefoneField.setText(entregador.getPhone());

    }

    private void handleInativarEntregador() {
        Entregador entregadorSelecionado = tabelaEntregador.getSelectionModel().getSelectedItem();
        if (entregadorSelecionado != null) {
            try {
                entregadorService.inativarEntregador(entregadorSelecionado, token);
                tabelaEntregador.refresh();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Erro ao inativar entregador "+e.getMessage());
            }
        }
    }


    /* --------------------- tabela -------------*/

    private void configurarTabela() {
        // Configuração das colunas da tabela
        TableColumn<Entregador, String> colunaNome = new TableColumn<>("Nome");
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Entregador, Long> colunaCodigo = new TableColumn<>("Código");
        colunaCodigo.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        colunaCodigo.setStyle("-fx-alignment: CENTER;");
        colunaCodigo.setSortType(TableColumn.SortType.ASCENDING);

        TableColumn<Entregador, String> colunaTelefone = new TableColumn<>("Telefone");
        colunaTelefone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colunaTelefone.setStyle("-fx-alignment: CENTER;");

        TableColumn<Entregador, String> colunaData = new TableColumn<>("Data de cadastro");
        colunaData.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        colunaData.setStyle("-fx-alignment: CENTER;");

        TableColumn<Entregador, String> colunaStatus = new TableColumn<>("Status");
        colunaStatus.setCellValueFactory(cellData -> {
            if (cellData.getValue().getActive()) {
                return new SimpleStringProperty("Ativo");
            }
            return new SimpleStringProperty("Inativo");
        });
        colunaStatus.setStyle("-fx-alignment: CENTER;");

        tabelaEntregador.getColumns().addAll(colunaCodigo,colunaNome, colunaTelefone, colunaData, colunaStatus);

        tabelaEntregador.setItems(entregadorObservable);

        tabelaEntregador.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Entregador entregador, boolean empty) {
                super.updateItem(entregador, empty);
                if (entregador == null || empty) {
                    setStyle("");
                } else if (!entregador.isActive()) {
                    setStyle("-fx-text-fill: red;");
                } else {
                    setStyle("");
                }
            }
        });
    }

    private void carregarEntregadores() {
        try {
            List<Entregador> todosEntregadores = entregadorService.buscarEntregadores(token);

            if (!exibirInativosCheckBox.isSelected()) {
                todosEntregadores.removeIf(entregador -> !entregador.getActive());
            }

            // Calcula o total de páginas
            totalPaginas = (int) Math.ceil((double) todosEntregadores.size() / itensPorPagina);

            // Filtra os entregadores para a página atual
            int inicio = paginaAtual * itensPorPagina;
            int fim = Math.min(inicio + itensPorPagina, todosEntregadores.size());
            List<Entregador> entregadoresPagina = todosEntregadores.subList(inicio, fim);

            // Atualiza a lista observável
            entregadorObservable.setAll(entregadoresPagina);

            atualizarEstadoBotoes();
            atualizarPaginacao();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar lista: " + e.getMessage());
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
        carregarEntregadores();
    }

    @FXML
    private void handlePaginaAnterior() {
        if (paginaAtual > 0) {
            paginaAtual--;
            carregarEntregadores();
        }
    }

    @FXML
    private void handleProximaPagina() {
        if (paginaAtual < totalPaginas - 1) {
            paginaAtual++;
            carregarEntregadores();
        }
    }

    private void clearFieldForm() {
        nomeField.clear();
        telefoneField.clear();
    }


    /*--------- validações ----------*/
    private void validarCampos() {
        // Validar números
        configurarValidacaoTelefone(telefoneField, telefoneErrorLabel);

        // Validar texto
        configurarValidacaoTexto(nomeField, nomeErrorLabel);
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

    /*------------------------ mensagens ---------------*/

    private void showSucessMessage() {
        successMessage.setVisible(true);
        successMessage.toFront();

        // Oculta a mensagem após 3 segundos
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> successMessage.setVisible(false));
        pause.play();
    }
}
