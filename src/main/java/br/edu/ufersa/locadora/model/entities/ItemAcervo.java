package br.edu.ufersa.locadora.model.entities;

import java.util.UUID;
import java.util.Objects;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

abstract public class ItemAcervo {
  //Atributos:
  private String ID = null;
  private String titulo = "";
  private String criadoPor = "";
  private String genero = "";
  private double valor = 0.0;
  private LocalDate dataDeLancamento = null;
  private int qtdItens = 0;
  private Boolean isDisco = null; //Identifica se o item é um livro ou um disco, sendo livro "false" e disco "true"

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
  public double getValor() {
    return valor;
  }
  public String getValorFormatado() {
    return String.format("%.2f", valor);
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
    if (valor < 0) {
        throw new IllegalArgumentException("Valor não pode ser negativo.");
    }
    this.valor = valor;
}
  public void setDataDeLancamento(String dataDeLancamentoFormatada) {
    DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    try { //Checa se a data de entrada está no formato correto. Se estiver, atualiza o valor do atributo
      LocalDate dataDeLancamento = LocalDate.parse(dataDeLancamentoFormatada, formatador);   
      this.dataDeLancamento = Objects.requireNonNull(dataDeLancamento, "Valor não pode ser null.");
    }
    catch(IllegalArgumentException e) { //Se a data de entrada estiver no formato errado, uma mensagem de descrevendo o problema é exibida no terminal
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
  abstract public void excluir();
  
  //construtor:
  public ItemAcervo(String titulo, String criadoPor, String genero, double valor, String dataDeLancamentoFormatada, int qtdItens, boolean isDisco) {
    this.ID = UUID.randomUUID().toString(); //Gera uma identificação única convertida em String para o objeto. 
    this.titulo = Objects.requireNonNull(titulo, "Valor não pode ser null.");
    this.criadoPor = Objects.requireNonNull(criadoPor, "Valor não pode ser null.");
    this.genero = Objects.requireNonNull(genero, "Valor não pode ser null.");
if (valor < 0) {
    throw new IllegalArgumentException("Valor não pode ser negativo.");
}
this.valor = valor;
    DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    try { //Checa se a data de entrada está no formato correto. Se estiver, atualiza o valor do atributo
      LocalDate dataDeLancamento = LocalDate.parse(dataDeLancamentoFormatada, formatador);   
      this.dataDeLancamento = Objects.requireNonNull(dataDeLancamento, "Valor não pode ser null.");
    }
    catch(IllegalArgumentException e) { //Se a data de entrada estiver no formato errado, uma mensagem de descrevendo o problema é exibida no terminal
      throw e;
    }
    this.qtdItens = qtdItens;
    try {
      if (isDisco == false || isDisco == true) this.isDisco = isDisco; //Checa se "isDisco" não é nulo. Se não for, atribui um valor a "isDisco". "false" = livro; "true" = disco.  
    }
    catch (NullPointerException e) {
      throw e;
    }
  }
}
