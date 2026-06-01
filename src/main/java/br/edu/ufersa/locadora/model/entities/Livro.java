package br.edu.ufersa.locadora.model.entities;

import br.edu.ufersa.locadora.model.entities.ItemAcervo;

import java.util.List;
import java.util.ArrayList;

public class Livro extends ItemAcervo {
  //Atributos:
  private static List<Livro> livros = new ArrayList<>(); //Lista de livros
  private int qtdPaginas = 0;
  
  //getter:
  public int getQtdPaginas() {
    return qtdPaginas;
  }
  
  //setter:
  public void setQtdPaginas(int qtdPaginas) {
    if (qtdPaginas < 0) throw new IllegalArgumentException("A quantidade de páginas não pode ser negativa.");
    if (qtdPaginas == 0) System.out.println("A quantidade de páginas está registrada como 0. É possível que a quantidade de páginas não tenha sido registrada");
    this.qtdPaginas = qtdPaginas;
  }

  //Método de exclusão de objeto:
  public void excluir() {
    livros.remove(this);
  }
  
  //construtor:
  public Livro(String titulo, String criadoPor, String genero, double valor, String dataDeLancamentoFormatada, int qtdItens, boolean isDisco, int qtdPaginas) {
    super(titulo, criadoPor, genero, valor, dataDeLancamentoFormatada, qtdItens, isDisco);
    if (qtdPaginas < 0) throw new IllegalArgumentException("A quantidade de páginas não pode ser negativa.");
    if (qtdPaginas == 0) System.out.println("A quantidade de páginas está registrada como 0. É possível que a quantidade de páginas não tenha sido registrada");
    this.qtdPaginas = qtdPaginas;
    livros.add(this); //Adiciona o objeto criado à lista de livros
  }
}
