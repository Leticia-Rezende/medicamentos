package org.medicamentos.medicamento.controller;

import javafx.beans.Observable;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.medicamentos.medicamento.model.Fornecedor;
import org.medicamentos.medicamento.model.Medicamento;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MainController implements Initializable {
    private List<Medicamento> medicamentoList = new ArrayList<>();
    private List<Fornecedor> fornecedorList = new ArrayList<>();
    private Medicamento medicamento;
    private Fornecedor fornecedor;

    private static final String ARQUIVO_DADOS = "dadosFarmacia.csv";
    private static final String DELIMITADOR = ";";
    private ObservableList<Medicamento> medicamentos = FXCollections.observableArrayList();


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
        this.medicamentoList = new ArrayList<>();
        this.fornecedorList = new ArrayList<>();
        this.medicamento = new Medicamento();
        this.fornecedor = new Fornecedor();

        this.btnCadastrar.setDisable(false);
        this.btnExcluir.setDisable(true);
        this.btnConsultar.setDisable(true);
        this.btnListarMedicamentos.setDisable(true);
        this.btnEstoque5Uni.setDisable(true);
        this.btnFiltrar30dias.setDisable(true);


    }

    private void atualizarTabela() {
        medicamentosTableView.getItems().clear();
        medicamentosTableView.getItems().addAll(medicamentoList);
    }



    //************
    // Ações dos botões

    @FXML
    public void onBtnCadastrar() {
        if (this.medicamento != null) {
            lerFormulario();

            if (codigoMedicamentoJaExiste(this.medicamento.getCodigo())) {
                System.out.println("Erro: Já existe um medicamento com o código " + this.medicamento.getCodigo());
                return;
            }

            this.medicamentoList.add(this.medicamento);
            updateTableView(medicamentoList);
        }
        this.btnExcluir.setDisable(false);
        this.btnConsultar.setDisable(false);
        this.btnListarMedicamentos.setDisable(false);
        limparCampos();
        lerDadosMedicamentos();
        gravarMedicamentosEmArquivo(medicamentoList);

    }

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
        lerDadosMedicamentos();

    }
        @FXML
        public void onBtnListarMedicamentos () {
            atualizarTabela();
            resultadoLabel.setText("Medicamentos listados.");
        }

        // Botões de filtragem de 5 e 30
        @FXML
        public void onBtnEstoque5Uni () {
            List<Medicamento> estoqueBaixo5dias = (List<Medicamento>) medicamentoList.stream();
            atualizarTabelaFiltrada(estoqueBaixo5dias);
            resultadoLabel.setText("Medicamentos com estoque baixo listados.");
        }

        @FXML
        public void onBtnFiltrar30dias () {
            LocalDate diaAtual = LocalDate.now();
            LocalDate diaLimiteEmDias = diaAtual.plusDays(30);

            List<Medicamento> medicamentosCom30Dias = medicamentoList.stream()
                    .filter(medicamento1 -> medicamento.getDataValidade().isBefore(diaLimiteEmDias) &&
                            medicamento.getDataValidade().isAfter(diaAtual))
                    .collect(Collectors.toList());
            updateTableView(medicamentosCom30Dias);

        }

        private void atualizarTabelaFiltrada (List < Medicamento > medicamentos) {
            medicamentosTableView.getItems().clear();
            medicamentosTableView.getItems().addAll(medicamentos);
        }

        public List<Medicamento> filtrarMedicamentos30dias (List < Medicamento > medicamentos) {
            return medicamentos.stream()
                    .filter(m -> m.getDataValidade().isBefore(LocalDate.now().plusDays(30)))
                    .collect(Collectors.toList());
        }
        public List<Medicamento> estoqueBaixo5dias (List < Medicamento > medicamentos) {
            return medicamentos.stream()
                    .filter(m -> m.getQuantidadeEstoque() < 5)
                    .collect(Collectors.toList());

        }

        public void updateTableView (List < Medicamento > medicamentos) {
            ObservableList<Medicamento> observableList = FXCollections.observableArrayList(medicamentos);

        }
    public class Validador {

        private static final Pattern CODIGO_PATTERN = Pattern.compile("[a-zA-Z0-9]{7}");
        private static final Pattern CNPJ_PATTERN = Pattern.compile("^\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}$");

        public static boolean validarCodigo(String codigo) {
            return codigo != null && CODIGO_PATTERN.matcher(codigo).matches();
        }

        public static boolean validarNome(String nome) {
            return nome != null && !nome.trim().isEmpty() && nome.trim().length() >= 3; // Exemplo de tamanho mínimo
        }

        // TODO: Implementar validação de CNPJ com dígito verificador
        public static boolean validarCNPJ(String cnpj) {
            return cnpj != null && CNPJ_PATTERN.matcher(cnpj).matches();
        }
    }

    private boolean codigoMedicamentoJaExiste(String codigo) {
        return medicamentoList.stream().anyMatch(medicamento -> medicamento.getCodigo().equals(codigo));
    }

    private boolean cnpjFornecedorJaExiste(String cnpj) {
        return fornecedorList.stream().anyMatch(fornecedor -> fornecedor.getCnpj().equals(cnpj));
    }

    public Medicamento lerFormulario() {
        this.medicamento.setCodigo(this.txtCodigo.getText());
        this.medicamento.setNome(this.txtNome.getText());
        this.medicamento.setDescricao(this.txtDescricao.getText());
        this.medicamento.setPrincipioAtivo(this.txtPrincipioAtivo.getText());
        this.medicamento.setDataValidade(LocalDate.parse(this.txtDataValidade.getText()));
        this.medicamento.setQuantidadeEstoque(Integer.parseInt(this.txtQuantidade.getText()));
        String precoString = this.txtPreco.getText();
        BigDecimal preco = new BigDecimal(precoString);
        this.medicamento.setPreco(preco);
        this.medicamento.setControlado(Boolean.parseBoolean(this.txtControlado.getText()));

        String Fornecedor = this.txtFornecedor.getText();

        if (fornecedor == null) {
            return null;
        } else {
            this.medicamento.setFornecedor(fornecedor);
        }

        return this.medicamento;
    }
    public List<Medicamento> lerDadosMedicamentos() {
        File file = new File("dadosFarmacia.txt");

        if (!file.exists() || file.length() == 0) {
            System.out.println("O arquivo 'dadosFarmacia.txt' não existe ou está vazio.");
            return Collections.emptyList();
        }

        List<Medicamento> medicamentos = new ArrayList<>();

        try {
            medicamentos = Files.lines(Paths.get("dadosFarmacia.txt"))
                    .map(linha -> linha.split(","))
                    .filter(dados -> dados.length == 9)
                    .map(dados -> {
                        try {
                            String codigo = dados[0];
                            String nome = dados[1];
                            String descricao = dados[2];
                            String pricipioAtivo = dados[3];
                            LocalDate dataValidade = LocalDate.parse(dados[4]);
                            int quantidadeEstoque = Integer.parseInt(dados[5]);
                            BigDecimal preco = new BigDecimal(dados[6]);
                            boolean controlado = Boolean.parseBoolean(dados[7]);
                            String fornecedorNome = dados[8];

                            Medicamento medicamento = new Medicamento();
                            medicamento.setCodigo(codigo);
                            medicamento.setNome(nome);
                            medicamento.setDescricao(descricao);
                            medicamento.setPrincipioAtivo(pricipioAtivo);
                            medicamento.setDataValidade(dataValidade);
                            medicamento.setQuantidadeEstoque(quantidadeEstoque);
                            medicamento.setPreco(preco);
                            medicamento.setControlado(controlado);
                            return medicamento;
                        } catch (Exception e) {
                            System.err.println("Erro ao converter dados da linha: " + String.join(",", dados));
                            return null;
                        }
                    })
                    .filter(medicamento -> medicamento != null)
                    .collect(Collectors.toList());


            medicamentoList.addAll(medicamentos);


            medicamentos.forEach(medicamento ->
                    System.out.printf("Código: %s, Nome: %s, Descrição: %s, Princípio Ativo: %s, Data de Validade: %s, " +
                                    "Quantidade em Estoque: %d, Preço: %.2f, Controlado: %b, Fornecedor: %s%n",
                            medicamento.getCodigo(), medicamento.getNome(), medicamento.getDescricao(),
                            medicamento.getPrincipioAtivo(), medicamento.getDataValidade(), medicamento.getQuantidadeEstoque(),
                            medicamento.getPreco(), medicamento.isControlado(), medicamento.getFornecedor() != null ? medicamento.getFornecedor().getRazaoSocial() : "N/A")
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        return medicamentos;
    }
    public void gravarMedicamentosEmArquivo(List<Medicamento> medicamentos) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("medicamentos.txt"))) {
            for (Medicamento medicamento : medicamentos) {
                String linha = String.join(",",
                        medicamento.getCodigo(),
                        medicamento.getNome(),
                        medicamento.getDescricao(),
                        medicamento.getPrincipioAtivo(),
                        medicamento.getDataValidade().toString(),
                        String.valueOf(medicamento.getQuantidadeEstoque()),
                        decimalFormat.format(medicamento.getPreco()),
                        String.valueOf(medicamento.isControlado()),
                        medicamento.getFornecedor() != null
                                ? medicamento.getFornecedor().getRazaoSocial() : "");
                writer.write(linha);
                writer.newLine();
            }
            System.out.println("Medicamentos gravados com sucesso em 'medicamentos.txt'.");
        } catch (IOException e) {
            System.err.println("Erro ao gravar medicamentos em arquivo: " + e.getMessage());
        }
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

}


