package br.edu.ufersa.locadora.model.entities;

import br.edu.ufersa.Locadora.model.entities.ItemAcervo;

import java.util.List;
import java.util.ArrayList;

public class Disco extends ItemAcervo {
  //Atributos:
  private static List<Disco> discos = new ArrayList<>(); //Lista de discos
  private String duracao = "Não registrada ou nenhuma.";
  //getter:
  public String getDuracao() {
    return duracao;
  }
  //setter:
  public void setDuracao(int horas, int minutos, int segundos) {
    if (horas < 0 || minutos > 59 || minutos < 0 || segundos > 59 || segundos < 0) throw new IllegalArgumentException("Algum dos valores, dentre marcadores de horas, minutos e segundos está errado. Pode ser que algum deles seja menor que 0 ou que \"minutos\" ou \"segundos\" seja maior que 59.");
    String duracao = null;
    String horasString = String.format("%02d", horas);
    String minutosString = String.format("%02d", minutos);
    String segundosString = String.format("%02d", segundos);
    if (horas == 0 && minutos == 0 && segundos == 0) duracao = "Não registrada ou nenhuma.";
    else if (horas == 0) duracao = minutosString + ":" + segundosString;
    else duracao = horasString + ":" + minutosString + ":" + segundosString;
    this.duracao = duracao;
  }
  //construtor:
  public Disco(String titulo, String criadoPor, String genero, double valor, String dataDeLancamentoFormatada, int qtdItens, boolean isDisco, int horas, int minutos, int segundos) {
    super(titulo, criadoPor, genero, valor, dataDeLancamentoFormatada, qtdItens, isDisco);
    if (horas < 0 || minutos > 59 || minutos < 0 || segundos > 59 || segundos < 0) throw new IllegalArgumentException("Algum dos valores, dentre marcadores de horas, minutos e segundos está errado. Pode ser que algum deles seja menor que 0 ou que \"minutos\" ou \"segundos\" seja maior que 59.");
    discos.add(this); //Adiciona o objeto criado à lista de discos
    String duracao = null;
    String horasString = String.format("%02d", horas);
    String minutosString = String.format("%02d", minutos);
    String segundosString = String.format("%02d", segundos);
    if (horas == 0 && minutos == 0 && segundos == 0) duracao = "Não registrada ou nenhuma.";
    else if (horas == 0) duracao = minutosString + ":" + segundosString;
    else duracao = horasString + ":" + minutosString + ":" + segundosString;
    this.duracao = duracao;
  }
}
