package org.medicamentos.medicamento.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.medicamentos.medicamento.model.Fornecedor;
import org.medicamentos.medicamento.model.Medicamento;

import java.math.BigDecimal;
import java.net.URL;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainController implements Initializable {
    private List<Medicamento> medicamentoList = new ArrayList<>();
    private List<Fornecedor> fornecedorList = new ArrayList<>();
    private Medicamento medicamento;
    private Fornecedor fornecedor;

    // ************
    // Medicamento
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
    private TableView<Medicamento> medicamentosTableView;
    @FXML
    private TableColumn<Medicamento, String> codigoColumn;
    @FXML
    private TableColumn<Medicamento, String> nomeColumn;
    @FXML
    private TableColumn<Medicamento, String> descricaoColumn;
    @FXML
    private TableColumn<Medicamento, String> principioAtivoColumn;
    @FXML
    private TableColumn<Medicamento, LocalDate> dataValidadeColumn;
    @FXML
    private TableColumn<Medicamento, Integer> quantidadeColumn;
    @FXML
    private TableColumn<Medicamento, BigDecimal> precoColumn;
    @FXML
    private TableColumn<Medicamento, Boolean> controladoColumn;
    @FXML
    private TableColumn<Medicamento, Fornecedor> fornecedorColumn;

    @FXML
    private Label resultadoLabel;

    //**************
    // Fornecedor
    @FXML
    private TextField txtCnpj;
    @FXML
    private TextField txtRazaoSocial;
    @FXML
    private TextField txtTelefone;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtCidade;
    @FXML
    private TextField txtEstado;


    // ***********
    // Botões
    @FXML
    private Button btnCadastrar;
    @FXML
    private Button btnExcluir;
    @FXML
    private Button btnConsultar;
    @FXML
    private Button btnListarMedicamentos;

    //**************
    // Botões para consultar informações por filtro
    @FXML
    private Button btnEstoque5Uni;
    @FXML
    private Button btnFiltrar30dias;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.btnCadastrar.setDisable(false);
        this.btnExcluir.setDisable(true);
        this.btnConsultar.setDisable(true);
        this.btnListarMedicamentos.setDisable(true);
        this.btnEstoque5Uni.setDisable(true);
        this.btnFiltrar30dias.setDisable(true);
        this.medicamento = new Medicamento();
        this.fornecedor = new Fornecedor();
    }

    private void atualizarTabela() {
        medicamentosTableView.getItems().clear();
        medicamentosTableView.getItems().addAll(medicamentoList);
    }

    @FXML
    public void onBtnCadastrar(){

    }

    @FXML
    private void limparCampos() {
        this.txtCodigo.setText("");
        this.txtNome.setText("");
        this.txtDescricao.setText("");
        this.txtPrincipioAtivo.setText("");
        this.txtDataValidade.setText("");
        this.txtQuantidade.setText("");
        this.txtPreco.setText("");
        this.txtCnpj.setText("");
        this.txtRazaoSocial.setText("");
        this.txtTelefone.setText("");
        this.txtEmail.setText("");
        this.txtCidade.setText("");
        this.txtEstado.setText("");
    }


    //************
    // De acordo com o medicamento selecionado, o medicamento é excluido e aparece a mensagem
    @FXML
    public void onBtnExcluir() {
        Medicamento medicamentoSelecionado = medicamentosTableView.getSelectionModel().getSelectedItem();
        if (medicamentoSelecionado != null) {
            medicamentoList.remove(medicamentoSelecionado);
            atualizarTabela();
            resultadoLabel.setText("Medicamento excluído com sucesso!");
        } else {
            resultadoLabel.setText("Selecione um medicamento para excluir.");
        }
    }
    //********
    // Consulta os medicamentos
    @FXML
    public void onBtnConsultar() {
        String codigoConsulta = txtCodigo.getText();
        Medicamento medicamentoEncontrado = medicamentoList.stream()
                .filter(medicamento -> medicamento.getCodigo().equals(codigoConsulta))
                .findFirst()
                .orElse(null);

        if (medicamentoEncontrado != null) {
            txtNome.setText(medicamentoEncontrado.getNome());
            txtDescricao.setText(medicamentoEncontrado.getDescricao());
            txtPrincipioAtivo.setText(medicamentoEncontrado.getPrincipioAtivo());
            txtDataValidade.setText(medicamentoEncontrado.getDataValidade().toString());
            txtQuantidade.setText(String.valueOf(medicamentoEncontrado.getQuantidadeEstoque()));
            txtPreco.setText(medicamentoEncontrado.getPreco().toString());
            txtControlado.setText(String.valueOf(medicamentoEncontrado.isControlado()));
            resultadoLabel.setText("Medicamento encontrado.");
        } else {
            resultadoLabel.setText("Medicamento não encontrado.");
        }
    }

    //**********
    // Lista os medicamentos
    @FXML
    public void onBtnListarMedicamentos() {
        atualizarTabela();
        resultadoLabel.setText("Medicamentos listados.");

    }

    @FXML
    public void onBtnEstoque5Uni() {
        List<Medicamento> filtrados = estoqueBaixo5dias(medicamentoList);
        atualizarTabelaFiltrada(filtrados);
        resultadoLabel.setText("Medicamentos com estoque baixo listados.");
    }

    @FXML
    public void onBtnFiltrar30dias() {
        List<Medicamento> filtrados = filtrarMedicamentos30dias(medicamentoList);
        atualizarTabelaFiltrada(filtrados);
        resultadoLabel.setText("Medicamentos próximos da validade listados.");
    }

    private void atualizarTabelaFiltrada(List<Medicamento> medicamentos) {
        medicamentosTableView.getItems().clear();
        medicamentosTableView.getItems().addAll(medicamentos);
    }

    public List<Medicamento> filtrarMedicamentos30dias(List<Medicamento> medicamentos) {
        return medicamentos.stream()
                .filter(m -> m.getDataValidade().isBefore(LocalDate.now().plusDays(30)))
                .collect(Collectors.toList());
    }
    public List<Medicamento> estoqueBaixo5dias(List<Medicamento> medicamentos) {
        return medicamentos.stream()
                .filter(m -> m.getQuantidadeEstoque() < 5)
                .collect(Collectors.toList());

    }
}
