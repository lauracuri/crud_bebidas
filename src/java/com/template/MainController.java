package com.template;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class MainController {

    @FXML private TextField txtMarca;
    @FXML private TextField txtSabor;
    @FXML private TextField txtPesquisa;

    @FXML private ComboBox<String> cmbTipo;
    @FXML private CheckBox chkAlcoolica;

    @FXML private Label lblMensagem;
    @FXML private Label lblContador;

    @FXML private Button btnSalvar;
    @FXML private Button btnEditar;
    @FXML private Button btnDeletar;
    @FXML private Button btnLimpar;

    @FXML private TableView<BebidasDTO> tblBebidas;
    @FXML private TableColumn<BebidasDTO, Integer> colId;
    @FXML private TableColumn<BebidasDTO, String> colMarca;
    @FXML private TableColumn<BebidasDTO, String> colTipo;
    @FXML private TableColumn<BebidasDTO, Boolean> colAlcoolica;
    @FXML private TableColumn<BebidasDTO, String> colSabor;

    private ObservableList<BebidasDTO> listaCompleta = FXCollections.observableArrayList();
    private FilteredList<BebidasDTO> listaFiltrada;

    private boolean aguardandoConfirmacaoExclusao = false;

    @FXML
    private void initialize() {
        configurarTabela();
        configurarComboBox();
        configurarPesquisa();
        configurarBotoes();
        carregarBebidas();
        limparCampos();

        txtMarca.requestFocus();
    }

    private void configurarTabela() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colAlcoolica.setCellValueFactory(new PropertyValueFactory<>("alcoolica"));
        colSabor.setCellValueFactory(new PropertyValueFactory<>("sabor"));

        tblBebidas.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            if (novo != null) {
                carregarCampos();
                btnEditar.setDisable(false);
                btnDeletar.setDisable(false);
                aguardandoConfirmacaoExclusao = false;
                btnDeletar.setText("Deletar");
                mostrarMensagem("Bebida selecionada. Agora você pode editar ou deletar.", "sucesso");
            }
        });
    }

    private void configurarComboBox() {
        cmbTipo.setItems(FXCollections.observableArrayList(
                "Refrigerante",
                "Suco",
                "Água",
                "Energético",
                "Cerveja",
                "Vinho",
                "Destilado",
                "Outro"
        ));
    }

    private void configurarPesquisa() {
        listaFiltrada = new FilteredList<>(listaCompleta, bebida -> true);
        tblBebidas.setItems(listaFiltrada);

        txtPesquisa.textProperty().addListener((obs, antigo, novo) -> {
            filtrarTabela(novo);
        });
    }

    private void configurarBotoes() {
        btnEditar.setDisable(true);
        btnDeletar.setDisable(true);
    }

    private void carregarBebidas() {
        BebidasDAO objDAO = new BebidasDAO();
        ArrayList<BebidasDTO> lista = objDAO.selecionarBebidas();

        listaCompleta.setAll(lista);

        atualizarContador();
    }

    private void filtrarTabela(String textoPesquisa) {
        listaFiltrada.setPredicate(bebida -> {
            if (textoPesquisa == null || textoPesquisa.trim().isEmpty()) {
                return true;
            }

            String filtro = textoPesquisa.toLowerCase();

            return bebida.getMarca().toLowerCase().contains(filtro)
                    || bebida.getTipo().toLowerCase().contains(filtro)
                    || bebida.getSabor().toLowerCase().contains(filtro);
        });

        atualizarContador();
    }

    private void atualizarContador() {
        int total = tblBebidas.getItems().size();

        if (total == 1) {
            lblContador.setText("1 registro encontrado");
        } else {
            lblContador.setText(total + " registros encontrados");
        }
    }

    @FXML
    private void carregarCampos() {
        BebidasDTO bebidaDto = tblBebidas.getSelectionModel().getSelectedItem();

        if (bebidaDto != null) {
            txtMarca.setText(bebidaDto.getMarca());
            cmbTipo.setValue(bebidaDto.getTipo());
            chkAlcoolica.setSelected(bebidaDto.isAlcoolica());
            txtSabor.setText(bebidaDto.getSabor());
        }
    }

    private void limparCampos() {
        txtMarca.clear();
        txtSabor.clear();
        cmbTipo.setValue(null);
        chkAlcoolica.setSelected(false);

        tblBebidas.getSelectionModel().clearSelection();

        btnEditar.setDisable(true);
        btnDeletar.setDisable(true);
        btnDeletar.setText("Deletar");

        aguardandoConfirmacaoExclusao = false;

        txtMarca.requestFocus();
    }

    private boolean validarCampos() {
        boolean camposValidos = true;

        txtMarca.setStyle(estiloCampoNormal());
        txtSabor.setStyle(estiloCampoNormal());
        cmbTipo.setStyle(estiloCampoNormal());

        if (txtMarca.getText().trim().isEmpty()) {
            txtMarca.setStyle(estiloCampoErro());
            camposValidos = false;
        }

        if (cmbTipo.getValue() == null) {
            cmbTipo.setStyle(estiloCampoErro());
            camposValidos = false;
        }

        if (txtSabor.getText().trim().isEmpty()) {
            txtSabor.setStyle(estiloCampoErro());
            camposValidos = false;
        }

        if (!camposValidos) {
            mostrarMensagem("Preencha corretamente os campos obrigatórios: marca, tipo e sabor.", "erro");
            return false;
        }

        return true;
    }

    private String estiloCampoNormal() {
        return "-fx-background-radius: 8; "
                + "-fx-border-radius: 8; "
                + "-fx-border-color: #81C784; "
                + "-fx-background-color: #F7FFF7;";
    }

    private String estiloCampoErro() {
        return "-fx-background-radius: 8; "
                + "-fx-border-radius: 8; "
                + "-fx-border-color: #B71C1C; "
                + "-fx-background-color: #FFEBEE;";
    }

    private void mostrarMensagem(String mensagem, String tipo) {
        lblMensagem.setText(mensagem);

        if (tipo.equals("erro")) {
            lblMensagem.setTextFill(javafx.scene.paint.Color.web("#B71C1C"));
        } else if (tipo.equals("sucesso")) {
            lblMensagem.setTextFill(javafx.scene.paint.Color.web("#1E5C1E"));
        } else {
            lblMensagem.setTextFill(javafx.scene.paint.Color.web("#555555"));
        }
    }

    @FXML
    private void btnSalvarAction(ActionEvent event) {
        if (!validarCampos()) {
            return;
        }

        BebidasDTO bebidaDto = new BebidasDTO();
        bebidaDto.setMarca(txtMarca.getText().trim());
        bebidaDto.setTipo(cmbTipo.getValue());
        bebidaDto.setAlcoolica(chkAlcoolica.isSelected());
        bebidaDto.setSabor(txtSabor.getText().trim());

        new BebidasDAO().cadastrarBebida(bebidaDto);

        carregarBebidas();
        limparCampos();

        mostrarMensagem("Bebida cadastrada com sucesso!", "sucesso");
    }

    @FXML
    private void btnEditarAction(ActionEvent event) {
        BebidasDTO bebidaSelecionada = tblBebidas.getSelectionModel().getSelectedItem();

        if (bebidaSelecionada == null) {
            mostrarMensagem("Selecione uma bebida na tabela para editar.", "erro");
            return;
        }

        if (!validarCampos()) {
            return;
        }

        BebidasDTO bebidaDto = new BebidasDTO();
        bebidaDto.setId(bebidaSelecionada.getId());
        bebidaDto.setMarca(txtMarca.getText().trim());
        bebidaDto.setTipo(cmbTipo.getValue());
        bebidaDto.setAlcoolica(chkAlcoolica.isSelected());
        bebidaDto.setSabor(txtSabor.getText().trim());

        new BebidasDAO().atualizarBebida(bebidaDto);

        carregarBebidas();
        limparCampos();

        mostrarMensagem("Bebida atualizada com sucesso!", "sucesso");
    }

    @FXML
    private void btnDeletarAction(ActionEvent event) {
        BebidasDTO bebidaSelecionada = tblBebidas.getSelectionModel().getSelectedItem();

        if (bebidaSelecionada == null) {
            mostrarMensagem("Selecione uma bebida na tabela para deletar.", "erro");
            return;
        }

        if (!aguardandoConfirmacaoExclusao) {
            aguardandoConfirmacaoExclusao = true;
            btnDeletar.setText("Confirmar");
            mostrarMensagem("Clique em Confirmar para excluir a bebida selecionada.", "erro");
            return;
        }

        new BebidasDAO().excluirBebida(bebidaSelecionada.getId());

        carregarBebidas();
        limparCampos();

        mostrarMensagem("Bebida excluída com sucesso!", "sucesso");
    }

    @FXML
    private void btnLimparAction(ActionEvent event) {
        limparCampos();
        mostrarMensagem("Campos limpos. Você pode cadastrar uma nova bebida.", "sucesso");
    }
}