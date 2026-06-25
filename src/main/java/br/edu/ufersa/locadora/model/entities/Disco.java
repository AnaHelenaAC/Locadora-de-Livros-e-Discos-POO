package br.edu.ufersa.locadora.model.entities;

public class Disco extends ItemAcervo {
  //Atributos:
  private int duracao = 0;
  
  //getters:
  public int getDuracao() { //Recebe duração em segundos
    return duracao;
  }
  public void setDuracao(int duracao) {
    setDuracaoSegundos(duracao);
  }
  public String getDuracaoString() { //Recebe duração em String no formato HH:mm:ss
    String duracaoString = "";
    int horas = duracao / 3600;
    int resto = duracao % 3600;
    int minutos = resto / 60;
    int segundos = resto % 60;
    String horasString = String.format("%02d", horas);
    String minutosString = String.format("%02d", minutos);
    String segundosString = String.format("%02d", segundos);
    if (horas == 0 && minutos == 0 && segundos == 0) duracaoString = "Não registrada ou nenhuma.";
    else if (horas == 0) duracaoString = minutosString + ":" + segundosString;
    else duracaoString = horasString + ":" + minutosString + ":" + segundosString;
    return duracaoString;
  }
  
  //setter:
  public void setDuracao(int horas, int minutos, int segundos) {
    if (horas < 0 || minutos > 59 || minutos < 0 || segundos > 59 || segundos < 0) throw new IllegalArgumentException("Algum dos valores, dentre marcadores de horas, minutos e segundos está errado. Pode ser que algum deles seja menor que 0 ou que \"minutos\" ou \"segundos\" seja maior que 59.");
    int duracao = 0;
    duracao += horas * 3600;
    duracao += minutos * 60;
    duracao += segundos;
    this.duracao = duracao;
  }

  public void setDuracaoSegundos(int duracaoSegundos) {
    if (duracaoSegundos < 0) throw new IllegalArgumentException("A duração não pode ser negativa.");
    this.duracao = duracaoSegundos;
  }
  
  //construtor:
  public Disco(String titulo, String criadoPor, String genero, double valor, String dataDeLancamentoFormatada, int qtdItens, boolean isDisco, int horas, int minutos, int segundos) {
    super(titulo, criadoPor, genero, valor, dataDeLancamentoFormatada, qtdItens, isDisco);
    if (horas < 0 || minutos > 59 || minutos < 0 || segundos > 59 || segundos < 0) throw new IllegalArgumentException("Algum dos valores, dentre marcadores de horas, minutos e segundos está errado. Pode ser que algum deles seja menor que 0 ou que \"minutos\" ou \"segundos\" seja maior que 59.");
    int duracao = 0;
    duracao += horas * 3600;
    duracao += minutos * 60;
    duracao += segundos;
    this.duracao = duracao;
  }
}
