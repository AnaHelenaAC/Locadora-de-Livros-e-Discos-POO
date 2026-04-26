package src.br.edu.ufersa.Locadora.model.entities.Disco;

import src.br.edu.ufersa.Locadora.model.entities.ItemAcervo;

public class Disco extends ItemAcervo {
  //Atributo:
  private String duracao = "Não registrada ou nenhuma.";

  //Métodos:

  //getter:
  public String getDuracao() {
    return duracao;
  }
  //setter:
  public void setDuracao(int horas, int minutos, int segundos) {
    String duracao = null;
    String horasString = String.valueOf(horas);
    String minutosString = String.valueOf(minutos);
    String segundosString = String.valueOf(segundos);
    if (horas == 0 && minutos == 0 && segundos == 0) duracao = "Não registrada ou nenhuma.";
    else if (horas == 0) duracao = minutosString + ":" + segundosString;
    else duracao = horasString + ":" + minutosString + ":" + segundosString;
    this.duracao = duracao;
  }
  //construtor:
  public Disco(String titulo, String criadoPor, String genero, double valor, String dataDeLancamentoFormatada, int qtdItens, boolean isDisco, int horas, int minutos, int segundos) {
    super(titulo, criadoPor, genero, valor, dataDeLancamentoFormatada, qtdItens, isDisco);
    String duracao = null;
    String horasString = String.valueOf(horas);
    String minutosString = String.valueOf(minutos);
    String segundosString = String.valueOf(segundos);
    if (horas == 0 && minutos == 0 && segundos == 0) duracao = "Não registrada ou nenhuma.";
    else if (horas == 0) duracao = minutosString + ":" + segundosString;
    else duracao = horasString + ":" + minutosString + ":" + segundosString;
    this.duracao = duracao;
  }
}
