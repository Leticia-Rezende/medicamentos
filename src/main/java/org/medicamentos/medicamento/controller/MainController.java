package org.medicamentos.medicamento.controller;

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
    private TableColumn<Medicamento, String> fornecedorColumn;

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
    private Button btnNovoMedicamento;

    @FXML
    private Button btnNovoFornecedor;
    @FXML
    private Button btnCadastrarMedicamento;
    @FXML
    private Button btnExcluirMedicamento;
    @FXML
    private Button btnConsultarMedicamento;
    @FXML
    private Button btnListarMedicamentos;

    @FXML
    private Button btnCadastrarFornecedor;

    @FXML
    private Button btnExcluirFornecedor;

    @FXML
    private Button btnConsultarFornecedor;


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

        this.btnCadastrarMedicamento.setDisable(false);
        this.btnExcluirMedicamento.setDisable(true);
        this.btnConsultarMedicamento.setDisable(false);
        this.btnListarMedicamentos.setDisable(false);

        this.btnEstoque5Uni.setDisable(false);
        this.btnFiltrar30dias.setDisable(false);

        this.btnExcluirFornecedor.setDisable(true);
        this.btnConsultarFornecedor.setDisable(true);

        codigoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodigo()));
        nomeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));
        descricaoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescricao()));
        principioAtivoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrincipioAtivo()));
        dataValidadeColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDataValidade()));
        quantidadeColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantidadeEstoque()).asObject());
        precoColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPreco()));
        controladoColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isControlado()).asObject());

        fornecedorColumn.setCellValueFactory(cellData -> {
            Fornecedor fornecedor = cellData.getValue().getFornecedor();
            return new SimpleStringProperty(fornecedor != null ? fornecedor.getRazaoSocial() : "");
        });

        List<Fornecedor> fornecedoresCarregados = lerDadosDoArquivoFornecedores();
        fornecedorList.addAll(fornecedoresCarregados);

        List<Medicamento> medicamentosCarregados = lerDadosDoArquivoMedicamentos();
        medicamentoList.addAll(medicamentosCarregados);
    }
    private void atualizarTabela() {
        medicamentosTableView.getItems().clear();
        medicamentosTableView.getItems().addAll(medicamentoList);
    }
    //************
    // Ações dos botões Medicamento e Fornecedores
    @FXML
    public void onBtnNovoMedicamento(){
        this.medicamento = new Medicamento();
        limparCampos();
        this.btnCadastrarMedicamento.setDisable(false);
        this.btnExcluirMedicamento.setDisable(false);

        String codigo = codigoColumn.getText();
        String nome = nomeColumn.getText();
        String descricao = descricaoColumn.getText();
        String precoAtivo = precoColumn.getText();
        String dataValidade = dataValidadeColumn.getText();
        String quantidade = quantidadeColumn.getText();
        String preco = precoColumn.getText();
        String controlado = controladoColumn.getText();
        String fornecedor = fornecedorColumn.getText();
    }
    @FXML
    public void onBtnNovoFornecedor(){
        this.fornecedor = new Fornecedor();
        limparCampos();
        this.btnCadastrarFornecedor.setDisable(false);
        this.btnExcluirFornecedor.setDisable(false);
    }
    @FXML
    public void onBtnCadastrarMedicamento() {
        if (this.medicamento != null) {
            if (lerFormulario() == null) return;

            Medicamento medicamentoExistente = medicamentoList.stream()
                    .filter(m -> m.getCodigo().equals(this.medicamento.getCodigo()))
                    .findFirst()
                    .orElse(null);

            if (medicamentoExistente != null) {
                medicamentoExistente.setNome(this.medicamento.getNome());
                medicamentoExistente.setDescricao(this.medicamento.getDescricao());
                medicamentoExistente.setPrincipioAtivo(this.medicamento.getPrincipioAtivo());
                medicamentoExistente.setDataValidade(this.medicamento.getDataValidade());
                medicamentoExistente.setQuantidadeEstoque(this.medicamento.getQuantidadeEstoque());
                medicamentoExistente.setPreco(this.medicamento.getPreco());
                medicamentoExistente.setControlado(this.medicamento.isControlado());
                medicamentoExistente.setFornecedor(this.medicamento.getFornecedor());

                System.out.println("Medicamento atualizado com sucesso: " + medicamentoExistente.getCodigo());
            } else {
                this.medicamentoList.add(this.medicamento);
                System.out.println("Medicamento adicionado com sucesso: " + this.medicamento.getCodigo());
            }
            gravarMedicamentosEmArquivo(medicamentoList);
            updateTableView(medicamentoList);
        }
        medicamentosTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                preencherFormularioComMedicamento(newValue);
            }
        });
    }
    @FXML
    public void onBtnCadastrarFornecedor() {
        if (this.fornecedor != null) {
            lerDadosFornecedor();

            if (razaoSocialFornecedorJaExiste(this.fornecedor.getRazaoSocial())) {
                System.out.println("Erro: Já existe um fornecedor com essa Razão Social " + this.fornecedor.getRazaoSocial());
                return;
            }
            this.fornecedorList.add(this.fornecedor);
            gravarFornecedoresEmArquivo(fornecedorList);
        }
        limparCampos();
        lerDadosFornecedor();
    }
    @FXML
    public void onBtnExcluirMedicamento() {
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
    public void onBtnConsultarMedicamento() {
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
    public void onBtnConsultarFornecedor() {
        String razaoSocialConsulta = txtRazaoSocial.getText();
        Fornecedor razaoSocialEncontrado = medicamentoList.stream()
                .filter(medicamento -> fornecedor.getRazaoSocial().equals(razaoSocialConsulta))
                .findFirst()
                .orElse(null).getFornecedor();

        if (razaoSocialEncontrado != null) {
            txtCnpj.setText(razaoSocialEncontrado.getCnpj());
            txtTelefone.setText(razaoSocialEncontrado.getTelefone());
            txtEmail.setText(razaoSocialEncontrado.getEmail());
            txtCidade.setText(razaoSocialEncontrado.getCidade());
            txtEstado.setText(razaoSocialEncontrado.getEstado());

            resultadoLabel.setText("Fornecedor  encontrado.");
        } else {
            resultadoLabel.setText("Fornecedor não encontrado.");
        }
        lerDadosFornecedor();

    }

    @FXML
    public void onBtnListarMedicamentos() {
        atualizarTabela();
        resultadoLabel.setText("Medicamentos listados.");
    }

    // Botões de filtragem de 5 e 30
    @FXML
    public void onBtnEstoque5Uni() {
        List<Medicamento> estoqueBaixo5dias = (List<Medicamento>) medicamentoList.stream();
        atualizarTabelaFiltrada(estoqueBaixo5dias);
        resultadoLabel.setText("Medicamentos com estoque baixo listados.");
    }

    @FXML
    public void onBtnFiltrar30dias() {
        LocalDate diaAtual = LocalDate.now();
        LocalDate diaLimiteEmDias = diaAtual.plusDays(30);

        List<Medicamento> medicamentosCom30Dias = medicamentoList.stream()
                .filter(medicamento1 -> medicamento.getDataValidade().isBefore(diaLimiteEmDias) &&
                        medicamento.getDataValidade().isAfter(diaAtual))
                .collect(Collectors.toList());
        updateTableView(medicamentosCom30Dias);

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

    public void updateTableView(List<Medicamento> medicamentos) {
        ObservableList<Medicamento> observableList = FXCollections.observableArrayList(medicamentos);
        medicamentosTableView.setItems(observableList);

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

    private boolean razaoSocialFornecedorJaExiste(String razaoSocial) {
        return fornecedorList.stream().anyMatch(fornecedor -> fornecedor.getRazaoSocial().equals(razaoSocial));
    }


    public Medicamento lerFormulario() {
        this.medicamento.setCodigo(this.txtCodigo.getText());
        this.medicamento.setNome(this.txtNome.getText());
        this.medicamento.setDescricao(this.txtDescricao.getText());
        this.medicamento.setPrincipioAtivo(this.txtPrincipioAtivo.getText());
        String dataValidadeStr = txtDataValidade.getText();
        LocalDate dataValidade = null; // Inicialize como null

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
        File file = new File("medicamentos.txt");

        if (!file.exists() || file.length() == 0) {
            System.out.println("O arquivo 'medicamentos.txt' não existe ou está vazio.");
            return Collections.emptyList();
        }

        List<Medicamento> medicamentos = new ArrayList<>();

        try {
            medicamentos = Files.lines(Paths.get("medicamentos.txt"))
                    .map(linha -> linha.split(";"))
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

    private Fornecedor lerDadosFornecedor() {
        this.fornecedor.setCnpj(this.txtCnpj.getText());
        if (Validador.validarCNPJ(this.fornecedor.getCnpj())) {
            showAlert("CNPJ inválido", "Por favor, digite um CNPJ valído");
            return null;
        }
        this.fornecedor.setRazaoSocial(this.txtRazaoSocial.getText());
        if (this.fornecedor.getRazaoSocial().isEmpty()) {
            showAlert("Nome inválido", "A Razão Social não pode ser vazia");
            return null;
        }
        this.fornecedor.setTelefone(this.txtTelefone.getText());
        this.fornecedor.setCidade(this.txtCidade.getText());
        this.fornecedor.setEstado(this.txtEstado.getText());
        this.fornecedor.setEmail(this.txtEmail.getText());

        return this.fornecedor;
    }


    public List<Fornecedor> lerDadosDoArquivoFornecedores() {
        File file = new File("medicamentos.csv");

        if (!file.exists() || file.length() == 0) {
            System.out.println("O arquivo 'medicamentos.csv' não existe ou está vazio.");
            return Collections.emptyList();
        }

        List<Fornecedor> fornecedores = new ArrayList<>();

        try {
            fornecedores = Files.lines(Paths.get("medicamentos.csv"))
                    .map(linha -> linha.split(";"))
                    .filter(dados -> dados.length == 6)
                    .map(dados -> {
                        Fornecedor fornecedor = new Fornecedor();
                        fornecedor.setCnpj(dados[0]);
                        fornecedor.setRazaoSocial(dados[1]);
                        fornecedor.setTelefone(dados[2]);
                        fornecedor.setEmail(dados[3]);
                        fornecedor.setCidade(dados[4]);
                        fornecedor.setEstado(dados[5]);
                        return fornecedor;
                    })
                    .collect(Collectors.toList());

            fornecedorList.addAll(fornecedores);

            fornecedores.forEach(fornecedor ->
                    System.out.printf("CNPJ: %s, Razão Social: %s, Telefone: %s, Email: %s, Cidade: %s, Estado: %s%n",
                            fornecedor.getCnpj(), fornecedor.getRazaoSocial(), fornecedor.getTelefone(),
                            fornecedor.getEmail(), fornecedor.getCidade(), fornecedor.getEstado())
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fornecedores;
    }

    public List<Medicamento> lerDadosDoArquivoMedicamentos() {
        File file = new File("medicamentos.csv");

        if (!file.exists() || file.length() == 0) {
            System.out.println("O arquivo 'medicamentos.csv' não existe ou está vazio.");
            return Collections.emptyList();
        }

        List<Medicamento> medicamentos = new ArrayList<>();

        try {
            medicamentos = Files.lines(Paths.get("medicamentos.csv"))
                    .map(linha -> linha.split(";"))
                    .filter(dados -> dados.length == 9)
                    .map(dados -> {
                        Medicamento medicamento = new Medicamento();
                        medicamento.setCodigo(dados[0]);
                        medicamento.setNome(dados[1]);
                        medicamento.setDescricao(dados[2]);
                        medicamento.setPrincipioAtivo(dados[3]);
                        medicamento.setDataValidade(LocalDate.parse(dados[4]));
                        medicamento.setQuantidadeEstoque(Integer.parseInt(dados[5]));
                        medicamento.setPreco(new BigDecimal(dados[6]));
                        medicamento.setControlado(Boolean.parseBoolean(dados[7]));
                        medicamento.setFornecedor(buscarFornecedorPorNome(dados[8]));
                        return medicamento;
                    })
                    .collect(Collectors.toList());

            medicamentoList.addAll(medicamentos);

            medicamentos.forEach(medicamento ->
                    System.out.printf("Código: %s, Nome: %s, Descrição: %s, Princípio Ativo: %s, Data de Validade: %s, " +
                                    "Quantidade em Estoque: %d, Preço: %.2f, Controlado: %b, Fornecedor: %s%n",
                            medicamento.getCodigo(), medicamento.getNome(), medicamento.getDescricao(),
                            medicamento.getPrincipioAtivo(), medicamento.getDataValidade(), medicamento.getQuantidadeEstoque(),
                            medicamento.getPreco(), medicamento.isControlado(), medicamento.getFornecedor() != null ? medicamento.getFornecedor().getRazaoSocial() : "N/A")
            );
        } catch (Exception e) {
            System.err.println("Erro ao ler dados do arquivo: " + e.getMessage());
        }

        return medicamentos;
    }

    public void gravarFornecedoresEmArquivo(List<Fornecedor> fornecedores) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("fornecedores.csv"))) {
            for (Fornecedor fornecedor : fornecedores) {
                String linha = String.join(";",
                        fornecedor.getCnpj(),
                        fornecedor.getRazaoSocial(),
                        fornecedor.getTelefone(),
                        fornecedor.getEmail(),
                        fornecedor.getCidade(),
                        fornecedor.getEstado());
                writer.write(linha);
                writer.newLine();
            }
            System.out.println("Fornecedores gravados com sucesso em 'fornecedores.csv'.");
        } catch (IOException e) {
            System.err.println("Erro ao gravar fornecedores em arquivo: " + e.getMessage());
        }
    }

    public void gravarMedicamentosEmArquivo(List<Medicamento> medicamentos) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("medicamentos.csv"))) {
            for (Medicamento medicamento : medicamentos) {
                String linha = String.join(";",
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
            System.out.println("Medicamentos gravados com sucesso em 'medicamentos.csv'.");
        } catch (IOException e) {
            System.err.println("Erro ao gravar medicamentos em arquivo: " + e.getMessage());
        }
    }


    public Fornecedor buscarFornecedorPorNome(String nomeFornecedor) {
        for (Fornecedor fornecedor : fornecedorList) {
            if (fornecedor.getRazaoSocial().equalsIgnoreCase(nomeFornecedor)) {
                return fornecedor;
            }
        }
        return null;
    }

    private void showAlert (String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void preencherFormularioComMedicamento(Medicamento medicamento) {
        txtCodigo.setText(medicamento.getCodigo());
        txtNome.setText(medicamento.getNome());
        txtDescricao.setText(medicamento.getDescricao());
        txtPrincipioAtivo.setText(medicamento.getPrincipioAtivo());
        txtDataValidade.setText(medicamento.getDataValidade().toString());
        txtQuantidade.setText(String.valueOf(medicamento.getQuantidadeEstoque()));
        txtPreco.setText(medicamento.getPreco().toString());
        txtControlado.setText(String.valueOf(medicamento.isControlado()));

        Fornecedor fornecedor = medicamento.getFornecedor();
        txtFornecedor.setText(fornecedor != null ? fornecedor.getRazaoSocial() : "");
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



