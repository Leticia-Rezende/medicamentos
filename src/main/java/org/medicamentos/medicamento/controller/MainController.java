package org.medicamentos.medicamento.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.medicamentos.medicamento.model.Fornecedor;
import org.medicamentos.medicamento.model.Medicamento;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private List<Medicamento> medicamentoList;
    private List<Fornecedor> fornecedorList;
    private Medicamento medicamento;
    private Fornecedor fornecedor;

    @FXML
    private TextField txtCodigo;
    @FXML
    private TextField txtNome;
    @FXML
    private TextField txtDescricao;
    @FXML
    private TextField txtPrincipioAtivo;
    @FXML
    private TextField txtDataValidade;
    @FXML
    private TextField txtQuantidade;
    @FXML
    private TextField txtPreco;
    @FXML
    private TextField txtControlado;
    @FXML
    private TextField txtFornecedor;




    @FXML
    private Button btnCadastrar;
    @FXML
    private Button btnExcluir;
    @FXML
    private Button btnConsultar;
    @FXML
    private Button btnListarMedicamentos;






    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
