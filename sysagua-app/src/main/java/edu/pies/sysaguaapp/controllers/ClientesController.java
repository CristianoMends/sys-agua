package edu.pies.sysaguaapp.controllers;

import edu.pies.sysaguaapp.models.Cliente;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.Arrays;

public class ClientesController {
    @FXML
    private TableView<Cliente> clientesTable; // Tabela para exibir os clientes

    @FXML
    private TableColumn<Cliente, String> colNome;
    @FXML
    private TableColumn<Cliente, String> colCNPJ;
    @FXML
    private TableColumn<Cliente, String> colEndereco;
    @FXML
    private TableColumn<Cliente, String> colCidade;
    @FXML
    private TableColumn<Cliente, String> colEmail;

    @FXML
    private void initialize() {
        // Inicializa as colunas da tabela
        colNome.setCellValueFactory(cellData -> cellData.getValue().getNome());
        colCNPJ.setCellValueFactory(cellData -> cellData.getValue().getCnpj());
        colEndereco.setCellValueFactory(cellData -> cellData.getValue().getEndereco());
        colCidade.setCellValueFactory(cellData -> cellData.getValue().getCidade());
        colEmail.setCellValueFactory(cellData -> cellData.getValue().getEmail());

        // Dados hardcoded
        clientesTable.getItems().setAll(
                Arrays.asList(
                        new Cliente("Empresa A", "12.345.678/0001-99", "Rua A, 123", "São Paulo", "empresa@a.com"),
                        new Cliente("Empresa B", "98.765.432/0001-88", "Rua B, 456", "Rio de Janeiro", "empresa@b.com"),
                        new Cliente("Empresa C", "11.223.344/0001-77", "Rua C, 789", "Belo Horizonte", "empresa@c.com")
                )
        );
    }
}
