package src.br.edu.ufersa.Locadora.model.entyties.ItemAcervo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ItemAcervo {
  private String titulo = "";
  private String criadoPor = "";
  private String genero = "";
  private String valor = "0.0"; //O valor será convertido de um double para uma String pelo setter
  private LocalDate dataDeLancamento = LocalDate.of(2000, 01, 01);
  private int qtdItens = 0;

  public String getTitulo() {
    return titulo;
  }
  public String getCriadoPor() {
    return criadoPor;
  }
  public String getGenero() {
    return genero;
  }
  public String getDataDeLancamento() {
    DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    String dataDeLancamentoFormatada = dataDeLancamento.format(formatador);
    return dataDeLancamentoFormatada;
  }
  public int getQtdItens() {
    return qtdItens;
  }
  public String getValor() {
    String valorFormatado = String.format("%.2f", valor); //Formata o valor
    return valorFormatado;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }
  public void setCriadoPor(String criadoPor) {
    this.criadoPor = criadoPor;
  }
  public void setGenero(String genero) {
    this.genero = genero;
  }
  public void setDataDeLancamento(String dataDeLancamentoFormatada) {
    DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    try { //Checa se a data de entrada está no formato correto. Se estiver, atualiza o valor do atributo
      LocalDate dataDeLancamento = LocalDate.parse(dataDeLancamentoFormatada, formatador);   
      this.dataDeLancamento = dataDeLancamento;
    }
    catch(IllegalArgumentException e) { //Se a data de entrada estiver no formato errado, uma mensagem de descrevendo o problema é exibida no terminal
      System.out.println("Data inválida. É possível que ela tenha sido escrita no formato errado. O formato correto é dd/MM/yyyy.");
      }
  }
  public void setQtdItens(int qtdItens) {
    this.qtdItens = qtdItens;
  }
  public void setValor(double valor) {
    String valorFormatado = String.format(Locale.ITALY, "%.2f", valor); //Transforma o valor de entrada double em um valor String no formato: 0,00. O formato númerico italiano é equivalente ao brasileiro
    this.valor = valorFormatado;
  }
} //Ainda falta implementar os métodos cadastrar, alterar e excluir. Também falta fazer um construtor. Talvez Cadastrar possa ser o próprio construtor. Alterar seriam os setters. Quanto ao método excluir, talvez um método que faça this = null funcione. Nesse caso, talvez seja melhor tirar os métodos cadastrar e alterar.
