package com.template.controller;

import com.template.model.dto.BebidasDTO;
import com.template.services.BebidaService;
import com.template.util.DialogUtil;
import com.template.validator.BebidaValidator;
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

    // Instâncias das classes de serviço e validação (fora da responsabilidade do controller)
    private final BebidaService bebidaService = new BebidaService();
    private final BebidaValidator bebidaValidator = new BebidaValidator();

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
        ArrayList<BebidasDTO> lista = bebidaService.selecionarBebidas();
        listaCompleta.setAll(lista);
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
        resetarEstilosCampos();

        boolean valido = bebidaValidator.validarBebida(
                txtMarca.getText(),
                cmbTipo.getValue(),
                txtSabor.getText()
        );

        if (!valido) {
            destacarCamposObrigatorios();
        }

        return valido;
    }

    private void resetarEstilosCampos() {
        txtMarca.setStyle(estiloCampoNormal());
        txtSabor.setStyle(estiloCampoNormal());
        cmbTipo.setStyle(estiloCampoNormal());
    }

    private void destacarCamposObrigatorios() {
        txtMarca.setStyle(estiloCampoErro());
        cmbTipo.setStyle(estiloCampoErro());
        txtSabor.setStyle(estiloCampoErro());
    }

    private String estiloCampoNormal() {
        return "-fx-background-radius: 8; "
                + "-fx-border-radius: 8; "
                + "-fx-border-color: #64B5F6; "
                + "-fx-background-color: #F5F9FF;";
    }

    private String estiloCampoErro() {
        return "-fx-background-radius: 8; "
                + "-fx-border-radius: 8; "
                + "-fx-border-color: #B71C1C; "
                + "-fx-background-color: #FFEBEE;";
    }

    @FXML
    private void btnSalvarAction(ActionEvent event) {
        if (!validarCampos()) {
            return;
        }

        bebidaService.cadastrarBebida(
                txtMarca.getText().trim(),
                cmbTipo.getValue(),
                chkAlcoolica.isSelected(),
                txtSabor.getText().trim()
        );

        carregarBebidas();
        limparCampos();

        DialogUtil.showInfo("Sucesso", "Bebida cadastrada com sucesso!");
    }

    @FXML
    private void btnEditarAction(ActionEvent event) {
        BebidasDTO bebidaSelecionada = tblBebidas.getSelectionModel().getSelectedItem();

        if (bebidaSelecionada == null) {
            DialogUtil.showError("Erro", "Selecione uma bebida na tabela para editar.");
            return;
        }

        if (!validarCampos()) {
            return;
        }

        bebidaService.atualizarBebida(
                bebidaSelecionada.getId(),
                txtMarca.getText().trim(),
                cmbTipo.getValue(),
                chkAlcoolica.isSelected(),
                txtSabor.getText().trim()
        );

        carregarBebidas();
        limparCampos();

        DialogUtil.showInfo("Sucesso", "Bebida atualizada com sucesso!");
    }

    @FXML
    private void btnDeletarAction(ActionEvent event) {
        BebidasDTO bebidaSelecionada = tblBebidas.getSelectionModel().getSelectedItem();

        if (bebidaSelecionada == null) {
            DialogUtil.showError("Erro", "Selecione uma bebida na tabela para deletar.");
            return;
        }

        if (!aguardandoConfirmacaoExclusao) {
            aguardandoConfirmacaoExclusao = true;
            btnDeletar.setText("Confirmar");
            DialogUtil.showWarning("Atenção", "Clique em Confirmar para excluir a bebida selecionada.");
            return;
        }

        bebidaService.excluirBebida(bebidaSelecionada.getId());

        carregarBebidas();
        limparCampos();

        DialogUtil.showInfo("Sucesso", "Bebida excluída com sucesso!");
    }

    @FXML
    private void btnLimparAction(ActionEvent event) {
        limparCampos();
        DialogUtil.showInfo("Sucesso", "Campos limpos. Você pode cadastrar uma nova bebida.");
    }
}