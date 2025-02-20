package edu.pies.sysaguaapp.controllers.Usuarios;

import edu.pies.sysaguaapp.enumeration.Usuarios.UserAccess;
import edu.pies.sysaguaapp.enumeration.Usuarios.UserStatus;
import edu.pies.sysaguaapp.models.Usuario;
import edu.pies.sysaguaapp.services.TokenManager;
import edu.pies.sysaguaapp.services.UsuarioService;
import javafx.animation.PauseTransition;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.util.List;

public class UsuarioController {
    private final UsuarioService usuarioService;
    private final String token;

    @FXML
    private StackPane rootPane;

    @FXML
    private TableView<Usuario> tabelaUsuario;

    @FXML
    private HBox paginationContainer;

    @FXML
    private Button btnAnterior, btnProximo, btnAdicionar;

    @FXML
    private Label successMessage;

    @FXML
    private CheckBox exibirInativosCheckBox;

    private ObservableList<Usuario> usuarioObservable;
    private int paginaAtual = 0;
    private final int itensPorPagina = 18;
    private int totalPaginas;

    public UsuarioController() {
        this.usuarioService = new UsuarioService();
        token = TokenManager.getInstance().getToken();
        this.usuarioObservable = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        configurarTabela();
        carregarUsuario();
        showMenuContext();
        btnAdicionar.setCursor(Cursor.HAND);

        exibirInativosCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> carregarUsuario());
    }



    /*------------------- usuario --------------*/

    @FXML
    private void handleAddUsuario() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Usuario/AddUsuario.fxml"));
            Parent form = loader.load();

            rootPane.getChildren().clear();
            rootPane.getChildren().add(form);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar formulário de usuario: " + e.getMessage());
        }
    }

    private void showMenuContext(){
        ContextMenu contextMenu = new ContextMenu();

        MenuItem inativarUsuario = new MenuItem("Inativar Usuário");

        contextMenu.getItems().addAll(inativarUsuario);

        inativarUsuario.setOnAction(event -> handleInativarUsuario());

        tabelaUsuario.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY && !tabelaUsuario.getSelectionModel().isEmpty()) {
                contextMenu.show(tabelaUsuario, event.getScreenX(), event.getScreenY());
            } else {
                contextMenu.hide();
            }
        });
    }

    private void handleInativarUsuario() {
        Usuario usuarioSelecionado = tabelaUsuario.getSelectionModel().getSelectedItem();
        if (usuarioSelecionado != null) {
            try {
                usuarioService.inativarUsuario(usuarioSelecionado, token);
                tabelaUsuario.refresh();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Erro ao inativar Usuario: " + e.getMessage());
            }
        }
    }


    /* --------------------- tabela -------------*/

    private void configurarTabela() {
        // Configuração das colunas da tabela
        TableColumn<Usuario, String> colunaNome = new TableColumn<>("Nome");
        colunaNome.setCellValueFactory(celldata -> {
            String nome = celldata.getValue().getName();
            String subname = celldata.getValue().getSurname();
            return new SimpleStringProperty(nome + " " + subname);
        });
        colunaNome.setStyle("-fx-alignment: CENTER;");


        TableColumn<Usuario, String> colunaEmail = new TableColumn<>("Email");
        colunaEmail.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getEmail()));
        colunaEmail.setStyle("-fx-alignment: CENTER;");

        TableColumn<Usuario, String> colunaCargo = new TableColumn<>("Cargo");
        colunaCargo.setCellValueFactory(param -> {
            UserAccess access = param.getValue().getAccess();

            if (access != null) {
                // Verifica se é um dos três valores permitidos
                if (access == UserAccess.OWNER || access == UserAccess.EMPLOYEE || access == UserAccess.MANAGER) {
                    return new SimpleObjectProperty<>(access.getDescription());
                }
            }
            return new SimpleObjectProperty<>("");
        });
        colunaCargo.setStyle("-fx-alignment: CENTER;");

        TableColumn<Usuario, String> colunaTelefone = new TableColumn<>("Telefone");
        colunaTelefone.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPhone()));
        colunaTelefone.setStyle("-fx-alignment: CENTER;");


        tabelaUsuario.getColumns().addAll(colunaNome, colunaEmail, colunaCargo, colunaTelefone );

        tabelaUsuario.setItems(usuarioObservable);

        tabelaUsuario.setRowFactory(tv -> new TableRow<Usuario>() {
            @Override
            protected void updateItem(Usuario usuario, boolean empty) {
                super.updateItem(usuario, empty);
                if (usuario == null || empty) {
                    setStyle("");
                } else if (!usuario.getStatus().getDescription().equals(usuario.getAccess().getDescription())) {
                    setStyle("-fx-text-fill: red;");
                } else {
                    setStyle("");
                }
            }
        });
    }

    private void carregarUsuario() {
        try {
            String token = TokenManager.getInstance().getToken();
            List<Usuario> todosUsuario = usuarioService.buscarUsuario(token);

            if (!exibirInativosCheckBox.isSelected()) {
                todosUsuario.removeIf(usuario -> usuario.getStatus() != UserStatus.ACTIVE);
            }

            // Calcula o total de páginas
            totalPaginas = (int) Math.ceil((double) todosUsuario.size() / itensPorPagina);

            // Filtra os fornecedores para a página atual
            int inicio = paginaAtual * itensPorPagina;
            int fim = Math.min(inicio + itensPorPagina, todosUsuario.size());
            List<Usuario> usuariosPagina = todosUsuario.subList(inicio, fim);

            // Atualiza a lista observável
            usuarioObservable.setAll(usuariosPagina);
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
        carregarUsuario();
    }

    @FXML
    private void handlePaginaAnterior() {
        if (paginaAtual > 0) {
            paginaAtual--;
            carregarUsuario();
        }
    }

    @FXML
    private void handleProximaPagina() {
        if (paginaAtual < totalPaginas - 1) {
            paginaAtual++;
            carregarUsuario();
        }
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
