package com.template.controller;

import com.template.model.dto.BebidasDTO;
import com.template.services.BebidaService;
import com.template.services.IBebidaService;
import com.template.util.DialogUtil;
import com.template.validator.BebidaValidador;
import com.template.validator.IBebidaValidador;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;

/**
 * Controller responsável pela interface gráfica de Bebidas (MVC).
 *
 * Melhorias implementadas:
 * - Layout (UI):
 *   1. Cabeçalho moderno com degradê, ícone e botão "Sobre o Sistema".
 *   2. Cards modernos, botões com ícones gráficos e paleta semântica.
 *   3. Tabela estilizada com badges visuais na coluna 'Alcoólica' (🍷 Sim / 🥤 Não).
 *
 * - Usabilidade (UX):
 *   1. Contador dinâmico de registros com feedback em tempo real na pesquisa.
 *   2. Barra de status com mensagens coloridas na tela (Sucesso, Aviso, Erro e Info).
 *   3. Atalhos de teclado (Ctrl+S, Ctrl+E, Delete, Esc, Ctrl+F), Tooltips e foco automático.
 */
public class MainController {

    @FXML private AnchorPane rootPane;
    @FXML private Button btnSobre;

    @FXML private TextField txtMarca;
    @FXML private TextField txtSabor;
    @FXML private TextField txtPesquisa;
    @FXML private Button btnLimparPesquisa;

    @FXML private ComboBox<String> cmbTipo;
    @FXML private CheckBox chkAlcoolica;

    @FXML private Button btnSalvar;
    @FXML private Button btnEditar;
    @FXML private Button btnDeletar;
    @FXML private Button btnLimpar;

    @FXML private Label lblContador;

    @FXML private TableView<BebidasDTO> tblBebidas;
    @FXML private TableColumn<BebidasDTO, Integer> colId;
    @FXML private TableColumn<BebidasDTO, String> colMarca;
    @FXML private TableColumn<BebidasDTO, String> colTipo;
    @FXML private TableColumn<BebidasDTO, Boolean> colAlcoolica;
    @FXML private TableColumn<BebidasDTO, String> colSabor;

    @FXML private AnchorPane paneStatus;
    @FXML private Label lblStatus;

    private final ObservableList<BebidasDTO> listaCompleta = FXCollections.observableArrayList();
    private FilteredList<BebidasDTO> listaFiltrada;

    // Dependências abstraídas por interfaces (DIP)
    private final IBebidaService bebidaService;
    private final IBebidaValidador bebidaValidator;

    public MainController(IBebidaService bebidaService, IBebidaValidador bebidaValidator) {
        this.bebidaService = bebidaService;
        this.bebidaValidator = bebidaValidator;
    }

    public MainController() {
        this(new BebidaService(), new BebidaValidador());
    }

    @FXML
    private void initialize() {
        configurarTabela();
        configurarComboBox();
        configurarPesquisa();
        configurarBotoes();
        configurarAtalhosTeclado();
        carregarBebidas();
        limparCampos();

        mostrarStatusInfo("✓ Sistema pronto. Digite os dados da bebida ou selecione um registro.");
        Platform.runLater(() -> txtMarca.requestFocus());
    }

    /**
     * UI 3: Configuração da tabela com badges visuais na coluna 'Alcoólica'
     */
    private void configurarTabela() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colAlcoolica.setCellValueFactory(new PropertyValueFactory<>("alcoolica"));
        colSabor.setCellValueFactory(new PropertyValueFactory<>("sabor"));

        colId.setStyle("-fx-alignment: CENTER;");

        // Customização da célula Alcoólica com badges visuais (UI 3)
        colAlcoolica.setCellFactory(col -> new TableCell<BebidasDTO, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("");
                } else if (item) {
                    setText("🍷 Sim");
                    setStyle("-fx-text-fill: #C62828; -fx-font-weight: bold; -fx-alignment: CENTER;");
                } else {
                    setText("🥤 Não");
                    setStyle("-fx-text-fill: #2E7D32; -fx-font-weight: bold; -fx-alignment: CENTER;");
                }
            }
        });

        tblBebidas.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            if (novo != null) {
                carregarCampos();
                mostrarStatusInfo("ℹ️ Bebida '" + novo.getMarca() + "' selecionada. Faça alterações e clique em 'Editar'.");
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

    /**
     * UX 1: Pesquisa com contador em tempo real
     */
    private void configurarPesquisa() {
        listaFiltrada = new FilteredList<>(listaCompleta, bebida -> true);
        tblBebidas.setItems(listaFiltrada);

        txtPesquisa.textProperty().addListener((obs, antigo, novo) -> {
            filtrarTabela(novo);
            atualizarContador();
        });
    }

    private void configurarBotoes() {
        btnEditar.disableProperty().bind(tblBebidas.getSelectionModel().selectedItemProperty().isNull());
        btnDeletar.disableProperty().bind(tblBebidas.getSelectionModel().selectedItemProperty().isNull());
    }

    /**
     * UX 3: Configuração de atalhos de teclado para maior produtividade
     */
    private void configurarAtalhosTeclado() {
        Platform.runLater(() -> {
            if (rootPane != null && rootPane.getScene() != null) {
                Scene scene = rootPane.getScene();
                // Ctrl + S: Salvar
                scene.getAccelerators().put(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN),
                        () -> btnSalvarAction(null));
                // Ctrl + E: Editar
                scene.getAccelerators().put(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN),
                        () -> {
                            if (!btnEditar.isDisable()) {
                                btnEditarAction(null);
                            }
                        });
                // Delete: Excluir
                scene.getAccelerators().put(new KeyCodeCombination(KeyCode.DELETE),
                        () -> {
                            if (!btnDeletar.isDisable()) {
                                btnDeletarAction(null);
                            }
                        });
                // Esc: Limpar
                scene.getAccelerators().put(new KeyCodeCombination(KeyCode.ESCAPE),
                        this::limparCampos);
                // Ctrl + F: Focar na Pesquisa
                scene.getAccelerators().put(new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN),
                        () -> txtPesquisa.requestFocus());
            }
        });
    }

    private void carregarBebidas() {
        ArrayList<BebidasDTO> lista = bebidaService.selecionarBebidas();
        listaCompleta.setAll(lista);
        atualizarContador();
    }

    /**
     * UX 1: Atualização dinâmica do contador de registros
     */
    private void atualizarContador() {
        int total = listaCompleta.size();
        int filtrados = listaFiltrada.size();
        String busca = txtPesquisa.getText();

        if (busca == null || busca.trim().isEmpty()) {
            lblContador.setText(String.format("📊 Total: %d bebida(s) cadastrada(s)", total));
        } else {
            lblContador.setText(String.format("🔍 Mostrando %d de %d bebida(s) encontrada(s)", filtrados, total));
        }
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
    private void btnLimparPesquisaAction(ActionEvent event) {
        txtPesquisa.clear();
        txtPesquisa.requestFocus();
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

    @FXML
    private void limparCampos() {
        txtMarca.clear();
        txtSabor.clear();
        cmbTipo.setValue(null);
        chkAlcoolica.setSelected(false);

        tblBebidas.getSelectionModel().clearSelection();
        txtMarca.requestFocus();
        mostrarStatusInfo("Formulário limpo e pronto para novo cadastro.");
    }

    @FXML
    private void btnSalvarAction(ActionEvent event) {
        boolean valido = bebidaValidator.validarBebida(
                txtMarca.getText(),
                cmbTipo.getValue(),
                txtSabor.getText()
        );

        if (!valido) {
            mostrarStatusAviso("⚠️ Por favor, revise os dados informados nos campos.");
            return;
        }

        bebidaService.cadastrarBebida(
                txtMarca.getText().trim(),
                cmbTipo.getValue(),
                chkAlcoolica.isSelected(),
                txtSabor.getText().trim()
        );

        String marcaCadastrada = txtMarca.getText().trim();
        carregarBebidas();
        limparCampos();

        mostrarStatusSucesso("✓ Bebida '" + marcaCadastrada + "' cadastrada com sucesso!");
    }

    @FXML
    private void btnEditarAction(ActionEvent event) {
        BebidasDTO bebidaSelecionada = tblBebidas.getSelectionModel().getSelectedItem();

        if (bebidaSelecionada == null) {
            mostrarStatusAviso("⚠️ Selecione uma bebida na tabela para editar.");
            return;
        }

        boolean valido = bebidaValidator.validarBebida(
                txtMarca.getText(),
                cmbTipo.getValue(),
                txtSabor.getText()
        );

        if (!valido) {
            mostrarStatusAviso("⚠️ Não foi possível atualizar. Corrija os erros indicados.");
            return;
        }

        bebidaService.atualizarBebida(
                bebidaSelecionada.getId(),
                txtMarca.getText().trim(),
                cmbTipo.getValue(),
                chkAlcoolica.isSelected(),
                txtSabor.getText().trim()
        );

        String marcaAtualizada = txtMarca.getText().trim();
        carregarBebidas();
        limparCampos();

        mostrarStatusSucesso("✓ Bebida '" + marcaAtualizada + "' atualizada com sucesso!");
    }

    @FXML
    private void btnDeletarAction(ActionEvent event) {
        BebidasDTO bebidaSelecionada = tblBebidas.getSelectionModel().getSelectedItem();

        if (bebidaSelecionada == null) {
            mostrarStatusAviso("⚠️ Selecione uma bebida na tabela para excluir.");
            return;
        }

        boolean confirmar = DialogUtil.showConfirmation(
                "Confirmar Exclusão",
                "Tem certeza de que deseja excluir a bebida '" + bebidaSelecionada.getMarca() + " - " + bebidaSelecionada.getSabor() + "'?"
        );

        if (confirmar) {
            bebidaService.excluirBebida(bebidaSelecionada.getId());
            carregarBebidas();
            limparCampos();
            mostrarStatusSucesso("✓ Bebida excluída com sucesso do sistema.");
        }
    }

    @FXML
    private void btnLimparAction(ActionEvent event) {
        limparCampos();
    }

    /**
     * UI 1: Tela/Diálogo informativo "Sobre o Sistema"
     */
    @FXML
    private void btnSobreAction(ActionEvent event) {
        DialogUtil.showInfo(
                "Sobre o Sistema",
                "🍹 SISTEMA DE GERENCIAMENTO DE BEBIDAS\n\n"
                        + "Arquitetura: MVC com JavaFX e FXML\n"
                        + "Princípios SOLID aplicados:\n"
                        + "  • SRP: Controllers focados em eventos; validações e regras isoladas.\n"
                        + "  • OCP: Validadores genéricos extensíveis sem modificar o controller.\n"
                        + "  • LSP: Tratamento polimórfico na lista de Validador<T>.\n"
                        + "  • ISP: Interfaces específicas (IBebidaValidador, IBebidaService, IBebidasDAO).\n"
                        + "  • DIP: Dependência de abstrações injetadas via ControllerFactory.\n\n"
                        + "Melhorias de UI/UX (3º Bimestre):\n"
                        + "  • Layout: Header temático, botões semânticos e tabela com badges.\n"
                        + "  • Usabilidade: Contador dinâmico, barra de status visual e atalhos de teclado."
        );
    }

    /**
     * UX 2: Feedback visual com cores dinâmicas para sucesso, aviso, erro e informação
     */
    private void mostrarStatusSucesso(String mensagem) {
        paneStatus.setStyle("-fx-background-color: #E8F5E9; -fx-border-color: #C8E6C9; -fx-border-width: 1 0 0 0;");
        lblStatus.setStyle("-fx-text-fill: #2E7D32; -fx-font-weight: bold;");
        lblStatus.setText(mensagem);
    }

    private void mostrarStatusAviso(String mensagem) {
        paneStatus.setStyle("-fx-background-color: #FFF8E1; -fx-border-color: #FFE082; -fx-border-width: 1 0 0 0;");
        lblStatus.setStyle("-fx-text-fill: #F57F17; -fx-font-weight: bold;");
        lblStatus.setText(mensagem);
    }

    private void mostrarStatusInfo(String mensagem) {
        paneStatus.setStyle("-fx-background-color: #E3F2FD; -fx-border-color: #BBDEFB; -fx-border-width: 1 0 0 0;");
        lblStatus.setStyle("-fx-text-fill: #1565C0; -fx-font-weight: bold;");
        lblStatus.setText(mensagem);
    }
}