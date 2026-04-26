package br.edu.ufersa.locadora.model.entities;

import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Objects;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.IllegalFormatConversionException;

public class ItemAcervo {
  //Atributos:
  private static List<ItemAcervo> ItensDoAcervo = new ArrayList<>(); //Lista de itens do Acervo
  private String ID = null;
  private String titulo = "";
  private String criadoPor = "";
  private String genero = "";
  private String valor = null; //O valor será convertido de um double para uma String pelo setter
  private LocalDate dataDeLancamento = null;
  private int qtdItens = 0;
  private Boolean isDisco = null; //Identifica se o item é um livro ou um disco, sendo livro "false" e disco "true"
  
  //Métodos:

  //getters:
  public String getID() {
    return ID;
  }
  public String getTitulo() {
    return titulo;
  }
  public String getCriadoPor() {
    return criadoPor;
  }
  public String getGenero() {
    return genero;
  }
  public String getValor() {
    String valorFormatado = String.format("%.2f", valor); //Formata o valor
    return valorFormatado;
  }
  public String getDataDeLancamento() {
    DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String dataDeLancamentoFormatada = dataDeLancamento.format(formatador);
    return dataDeLancamentoFormatada;
  }
  public int getQtdItens() {
    return qtdItens;
  }
  public boolean getIsDisco() {
    return isDisco; //false" = livro; "true" = disco.
  }
  
  //setters:
  public void setTitulo(String titulo) {
    this.titulo = Objects.requireNonNull(titulo, "Valor não pode ser null.");
  }
  public void setCriadoPor(String criadoPor) {
    this.criadoPor = Objects.requireNonNull(criadoPor, "Valor não pode ser null.");
  }
  public void setGenero(String genero) {
    this.genero = Objects.requireNonNull(genero, "Valor não pode ser null.");
  }
  public void setValor(double valor) {
    String valorFormatado = null;
    try {
        valorFormatado = String.format(Locale.ITALY, "%.2f", valor); //Transforma o valor de entrada double em um valor String no formato: 0,00. O formato númerico italiano é equivalente ao brasileiro
    }
    catch (IllegalFormatConversionException e) {
        System.out.println("Tentou transformar um valor null em String.");
        throw e;
    }
    this.valor = Objects.requireNonNull(valorFormatado, "Valor não pode ser null.");
  }
  public void setDataDeLancamento(String dataDeLancamentoFormatada) {
    DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    try { //Checa se a data de entrada está no formato correto. Se estiver, atualiza o valor do atributo
      LocalDate dataDeLancamento = LocalDate.parse(dataDeLancamentoFormatada, formatador);   
      this.dataDeLancamento = Objects.requireNonNull(dataDeLancamento, "Valor não pode ser null.");
    }
    catch(IllegalArgumentException e) { //Se a data de entrada estiver no formato errado, uma mensagem de descrevendo o problema é exibida no terminal
      System.out.println("Data inválida. É possível que ela tenha sido escrita no formato errado. O formato correto é dd/MM/yyyy.");
      throw e;
    }
  }
  public void setQtdItens(int qtdItens) {
    this.qtdItens = qtdItens;
  }
  
  //toggle:
  public void toggleIsDisco() {
    try {
      if (isDisco == false || isDisco == true) isDisco = !isDisco; //Checa se "isDisco" não é nulo. Se não for, inverte o valor de "isDisco", transformando um disco em livro ou um livro em disco. "false" = livro; "true" = disco.  
    }
    catch (NullPointerException e) {
      System.out.println("Valor de isDisco = null");
      throw e;
    }
  }
  
  //Método de exclusão de objeto:
  public void excluir() {
    ItensDoAcervo.remove(this); //Remove o objeto da lista de itens do acervo
  }
  
  //construtor:
  public ItemAcervo(String titulo, String criadoPor, String genero, double valor, String dataDeLancamentoFormatada, int qtdItens, boolean isDisco) {
    ItensDoAcervo.add(this); //Adiciona o objeto criado à lista de itens do acervo
    this.ID = UUID.randomUUID().toString(); //Gera uma identificação única convertida em String para o objeto. 
    this.titulo = Objects.requireNonNull(titulo, "Valor não pode ser null.");
    this.criadoPor = Objects.requireNonNull(criadoPor, "Valor não pode ser null.");
    this.genero = Objects.requireNonNull(genero, "Valor não pode ser null.");
    String valorFormatado = null;
    try {
        valorFormatado = String.format(Locale.ITALY, "%.2f", valor); //Transforma o valor de entrada double em um valor String no formato: 0,00. O formato númerico italiano é equivalente ao brasileiro
    }
    catch (IllegalFormatConversionException e) {
        System.out.println("Tentou transformar um valor null em String.");
        throw e;
    }
    this.valor = Objects.requireNonNull(valorFormatado, "Valor não pode ser null.");
    DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    try { //Checa se a data de entrada está no formato correto. Se estiver, atualiza o valor do atributo
      LocalDate dataDeLancamento = LocalDate.parse(dataDeLancamentoFormatada, formatador);   
      this.dataDeLancamento = Objects.requireNonNull(dataDeLancamento, "Valor não pode ser null.");
    }
    catch(IllegalArgumentException e) { //Se a data de entrada estiver no formato errado, uma mensagem de descrevendo o problema é exibida no terminal
      System.out.println("Data inválida. É possível que ela tenha sido escrita no formato errado. O formato correto é dd/MM/yyyy.");
      throw e;
    }
    this.qtdItens = qtdItens;
    try {
      if (isDisco == false || isDisco == true) this.isDisco = isDisco; //Checa se "isDisco" não é nulo. Se não for, atribui um valor a "isDisco". "false" = livro; "true" = disco.  
    }
    catch (NullPointerException e) {
      System.out.println("Valor de isDisco = null");
      throw e;
    }
  }
}