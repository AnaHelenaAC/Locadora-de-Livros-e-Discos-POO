<?xml version="1.0" encoding="UTF-8"?>
        <?import javafx.scene.layout.*?>
        <?import javafx.scene.control.*?>
        <?import javafx.scene.shape.*?>
        <?import javafx.geometry.*?>

<!--
Tela de Acervo — Cultura Viva
Paleta: #2E1A47 roxo-escuro | #F8EED1 creme | #F2D888 dourado
            #D1203A crimson     | #3D2460 roxo-card
Salvar em: src/main/resources/br/edu/ufersa/locadora/view/acervo.fxml
-->
<BorderPane xmlns="http://javafx.com/javafx/17"
xmlns:fx="http://javafx.com/fxml/1"
fx:controller="br.edu.ufersa.locadora.controllers.AcervoController"
prefWidth="780" prefHeight="560"
style="-fx-background-color: #F8EED1;">

    <!-- ══════════════════════════════════════════════════════
BARRA SUPERIOR — roxo escuro com logo e navegação
    ══════════════════════════════════════════════════════ -->
    <top>
        <HBox alignment="CENTER_LEFT" spacing="0"
style="-fx-background-color: #2E1A47; -fx-min-height: 56px; -fx-pref-height: 56px;">
            <padding><Insets left="16" right="0" top="0" bottom="0"/></padding>

            <!-- Logo -->
            <Label text="🎵"
style="-fx-font-size: 24px; -fx-text-fill: #F2D888; -fx-padding: 0 4 0 0;"/>
            <Label text="Cultura Viva"
style="-fx-text-fill: #F2D888; -fx-font-family: Georgia;
        -fx-font-size: 22px; -fx-font-weight: bold; -fx-padding: 0 24 0 4;"/>

            <!-- Separador preenchendo espaço -->
            <Region HBox.hgrow="ALWAYS"/>

            <!-- Botão Acervo (ativo — fundo levemente mais claro) -->
            <Button fx:id="btnNavAcervo" text="⊙ ▤   Acervo"
onAction="#navegarAcervo"
style="-fx-background-color: rgba(255,255,255,0.15);
        -fx-text-fill: #F2D888; -fx-font-size: 13px; -fx-font-weight: bold;
                           -fx-padding: 18 22 16 22; -fx-background-radius: 0;
        -fx-border-color: transparent transparent #F2D888 transparent;
                           -fx-border-width: 0 0 2 0; -fx-cursor: hand;"/>

            <!-- Botão Relatório -->
            <Button fx:id="btnNavRelatorio" text="📋   Relatório"
onAction="#navegarRelatorio"
style="-fx-background-color: transparent; -fx-text-fill: #C8B8E8;
        -fx-font-size: 13px; -fx-font-weight: bold;
                           -fx-padding: 18 22 16 22; -fx-background-radius: 0;
        -fx-cursor: hand;"/>

            <!-- Botão Cadastros -->
            <Button fx:id="btnNavCadastros" text="👤   Cadastros"
onAction="#navegarCadastros"
style="-fx-background-color: transparent; -fx-text-fill: #C8B8E8;
        -fx-font-size: 13px; -fx-font-weight: bold;
                           -fx-padding: 18 22 16 22; -fx-background-radius: 0;
        -fx-cursor: hand;"/>
        </HBox>
    </top>

    <!-- ══════════════════════════════════════════════════════
CONTEÚDO CENTRAL — área creme com as duas colunas
    ══════════════════════════════════════════════════════ -->
    <center>
        <!-- Container geral com padding e cantos arredondados na área de cards -->
        <HBox spacing="16"
style="-fx-background-color: #F8EED1; -fx-padding: 16 16 0 16;">

            <!-- ─────────────────────────────────────────────
SEÇÃO LIVROS
            ───────────────────────────────────────────── -->
            <VBox HBox.hgrow="ALWAYS" spacing="0"
style="-fx-background-color: #F0E4C0;
        -fx-background-radius: 14 14 0 0;
        -fx-border-color: #D8C89A;
                         -fx-border-radius: 14 14 0 0;
        -fx-border-width: 1;">

                <!-- Cabeçalho roxo arredondado no topo -->
                <HBox alignment="CENTER_LEFT" spacing="10"
style="-fx-background-color: #2E1A47;
        -fx-background-radius: 12 12 0 0;
        -fx-padding: 12 20 12 20;">
                    <Label text="▤"
style="-fx-text-fill: #F2D888; -fx-font-size: 18px;"/>
                    <Label text="Livros"
style="-fx-text-fill: #F2D888; -fx-font-family: Georgia;
        -fx-font-size: 18px; -fx-font-weight: bold;"/>
                </HBox>

                <!-- Grade de cards com scroll -->
                <ScrollPane fx:id="scrollLivros"
fitToWidth="true"
hbarPolicy="NEVER"
vbarPolicy="AS_NEEDED"
VBox.vgrow="ALWAYS"
style="-fx-background: transparent;
        -fx-background-color: transparent;
                                   -fx-border-color: transparent;">
                    <FlowPane fx:id="painelLivros"
hgap="10" vgap="10"
style="-fx-padding: 12; -fx-background-color: transparent;"/>
                </ScrollPane>
            </VBox>

            <!-- ─────────────────────────────────────────────
SEÇÃO DISCOS
            ───────────────────────────────────────────── -->
            <VBox HBox.hgrow="ALWAYS" spacing="0"
style="-fx-background-color: #F0E4C0;
        -fx-background-radius: 14 14 0 0;
        -fx-border-color: #D8C89A;
                         -fx-border-radius: 14 14 0 0;
        -fx-border-width: 1;">

                <!-- Cabeçalho roxo -->
                <HBox alignment="CENTER_LEFT" spacing="10"
style="-fx-background-color: #2E1A47;
        -fx-background-radius: 12 12 0 0;
        -fx-padding: 12 20 12 20;">
                    <Label text="⊙"
style="-fx-text-fill: #F2D888; -fx-font-size: 18px;"/>
                    <Label text="Discos"
style="-fx-text-fill: #F2D888; -fx-font-family: Georgia;
        -fx-font-size: 18px; -fx-font-weight: bold;"/>
                </HBox>

                <ScrollPane fx:id="scrollDiscos"
fitToWidth="true"
hbarPolicy="NEVER"
vbarPolicy="AS_NEEDED"
VBox.vgrow="ALWAYS"
style="-fx-background: transparent;
        -fx-background-color: transparent;
                                   -fx-border-color: transparent;">
                    <FlowPane fx:id="painelDiscos"
hgap="10" vgap="10"
style="-fx-padding: 12; -fx-background-color: transparent;"/>
                </ScrollPane>
            </VBox>

            <!-- ─────────────────────────────────────────────
PAINEL FORMULÁRIO — oculto por padrão,
aparece ao clicar em Adicionar ou Editar
            ───────────────────────────────────────────── -->
            <VBox fx:id="painelForm"
spacing="8"
visible="false" managed="false"
style="-fx-background-color: #FFFFFF;
        -fx-background-radius: 12;
        -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.22), 16, 0, 0, 4);
        -fx-padding: 20 20 20 20;
        -fx-pref-width: 300; -fx-max-width: 300;">

                <Label fx:id="lblFormTitulo" text="Novo Livro"
style="-fx-text-fill: #2E1A47; -fx-font-family: Georgia;
        -fx-font-size: 15px; -fx-font-weight: bold;
                              -fx-padding: 0 0 8 0;"/>

                <!-- Escolha Livro / Disco -->
                <HBox spacing="16" alignment="CENTER_LEFT">
                    <RadioButton fx:id="rbLivro" text="Livro" selected="true"
onAction="#onTipoSelecionado"
style="-fx-text-fill: #2E1A47; -fx-font-size: 12px;"/>
                    <RadioButton fx:id="rbDisco" text="Disco"
onAction="#onTipoSelecionado"
style="-fx-text-fill: #2E1A47; -fx-font-size: 12px;"/>
                </HBox>

                <Label text="Título:" style="-fx-text-fill: #555; -fx-font-size: 11px;"/>
                <TextField fx:id="tfTitulo" promptText="Ex: Dom Casmurro"
style="-fx-background-color: #F4F0F0; -fx-background-radius: 6;
        -fx-border-color: #D8D0D0; -fx-border-radius: 6;
        -fx-padding: 7 10 7 10; -fx-font-size: 12px;"/>

                <Label text="Autor / Banda:" style="-fx-text-fill: #555; -fx-font-size: 11px;"/>
                <TextField fx:id="tfCriadoPor" promptText="Ex: Machado de Assis"
style="-fx-background-color: #F4F0F0; -fx-background-radius: 6;
        -fx-border-color: #D8D0D0; -fx-border-radius: 6;
        -fx-padding: 7 10 7 10; -fx-font-size: 12px;"/>

                <Label text="Gênero / Estilo:" style="-fx-text-fill: #555; -fx-font-size: 11px;"/>
                <TextField fx:id="tfGenero" promptText="Ex: Romance, MPB..."
style="-fx-background-color: #F4F0F0; -fx-background-radius: 6;
        -fx-border-color: #D8D0D0; -fx-border-radius: 6;
        -fx-padding: 7 10 7 10; -fx-font-size: 12px;"/>

                <Label text="Data (dd/MM/yyyy):" style="-fx-text-fill: #555; -fx-font-size: 11px;"/>
                <TextField fx:id="tfData" promptText="01/01/2000"
style="-fx-background-color: #F4F0F0; -fx-background-radius: 6;
        -fx-border-color: #D8D0D0; -fx-border-radius: 6;
        -fx-padding: 7 10 7 10; -fx-font-size: 12px;"/>

                <Label text="Qtd. exemplares:" style="-fx-text-fill: #555; -fx-font-size: 11px;"/>
                <TextField fx:id="tfQtd" promptText="0"
style="-fx-background-color: #F4F0F0; -fx-background-radius: 6;
        -fx-border-color: #D8D0D0; -fx-border-radius: 6;
        -fx-padding: 7 10 7 10; -fx-font-size: 12px;"/>

                <Label text="Valor diária (R$):" style="-fx-text-fill: #555; -fx-font-size: 11px;"/>
                <TextField fx:id="tfValor" promptText="0.00"
style="-fx-background-color: #F4F0F0; -fx-background-radius: 6;
        -fx-border-color: #D8D0D0; -fx-border-radius: 6;
        -fx-padding: 7 10 7 10; -fx-font-size: 12px;"/>

                <Label fx:id="lblCampoExtra" text="Qtd. de páginas:"
style="-fx-text-fill: #555; -fx-font-size: 11px;"/>
                <TextField fx:id="tfCampoExtra" promptText="0"
style="-fx-background-color: #F4F0F0; -fx-background-radius: 6;
        -fx-border-color: #D8D0D0; -fx-border-radius: 6;
        -fx-padding: 7 10 7 10; -fx-font-size: 12px;"/>

                <Label fx:id="lblFormMsg" text="" wrapText="true"
style="-fx-text-fill: #C0392B; -fx-font-size: 11px;"/>

                <HBox spacing="8" alignment="CENTER_LEFT" style="-fx-padding: 6 0 0 0;">
                    <Button text="Salvar" onAction="#salvarItem"
style="-fx-background-color: #2E1A47; -fx-text-fill: #F2D888;
        -fx-font-weight: bold; -fx-background-radius: 8;
        -fx-padding: 8 20 8 20; -fx-cursor: hand; -fx-font-size: 12px;"/>
                    <Button text="Cancelar" onAction="#fecharForm"
style="-fx-background-color: #E0D8C8; -fx-text-fill: #555;
        -fx-background-radius: 8; -fx-padding: 8 14 8 14;
        -fx-cursor: hand; -fx-font-size: 12px;"/>
                </HBox>
            </VBox>

        </HBox>
    </center>

    <!-- ══════════════════════════════════════════════════════
BARRA INFERIOR — pesquisa, logout, adicionar
    ══════════════════════════════════════════════════════ -->
    <bottom>
        <HBox alignment="CENTER_LEFT" spacing="10"
style="-fx-background-color: #2E1A47;
        -fx-pref-height: 50px; -fx-min-height: 50px;
                     -fx-padding: 0 14 0 14;">

            <!-- Ícone de filtro -->
            <Label text="▼"
style="-fx-text-fill: #F8EED1; -fx-font-size: 13px;"/>

            <!-- Campo de pesquisa -->
            <TextField fx:id="tfPesquisa"
promptText="Pesquisar"
onAction="#pesquisar"
style="-fx-background-color: #F8EED1;
        -fx-background-radius: 20;
        -fx-border-color: transparent;
                              -fx-padding: 7 14 7 14;
        -fx-font-size: 13px;
                              -fx-text-fill: #2E1A47;
                              -fx-prompt-text-fill: #7A6A5A;
                              -fx-pref-width: 170px;"/>

            <!-- Ícone de lupa -->
            <Label text="🔍"
style="-fx-text-fill: #7A6A5A; -fx-font-size: 13px;
        -fx-translate-x: -36px; -fx-mouse-transparent: true;"/>

            <Region HBox.hgrow="ALWAYS"/>

            <!-- Botão Logout -->
            <Button text="Logout ➜"
onAction="#handleLogout"
style="-fx-background-color: #D1203A;
        -fx-text-fill: white;
                           -fx-font-size: 13px; -fx-font-weight: bold;
                           -fx-background-radius: 20;
        -fx-padding: 8 20 8 20;
        -fx-cursor: hand;"/>

            <!-- Botão Adicionar -->
            <Button fx:id="btnAdicionar"
text="Adicionar novo livro ＋"
onAction="#abrirFormNovo"
style="-fx-background-color: #D1203A;
        -fx-text-fill: white;
                           -fx-font-size: 13px; -fx-font-weight: bold;
                           -fx-background-radius: 20;
        -fx-padding: 8 20 8 20;
        -fx-cursor: hand;"/>
        </HBox>
    </bottom>

</BorderPane>
