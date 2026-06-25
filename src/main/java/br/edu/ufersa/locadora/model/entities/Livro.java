package br.edu.ufersa.locadora.model.entities;

public class Livro extends ItemAcervo {
  //Atributos:
  private int qtdPaginas = 0;
  
  //getter:
  public int getQtdPaginas() {
    return qtdPaginas;
  }
  
  //setter:
  public void setQtdPaginas(int qtdPaginas) {
    if (qtdPaginas < 0) throw new IllegalArgumentException("A quantidade de páginas não pode ser negativa.");
    this.qtdPaginas = qtdPaginas;
  }
  
  //construtor:
  public Livro(String titulo, String criadoPor, String genero, double valor, String dataDeLancamentoFormatada, int qtdItens, boolean isDisco, int qtdPaginas) {
    super(titulo, criadoPor, genero, valor, dataDeLancamentoFormatada, qtdItens, isDisco);
    if (qtdPaginas < 0) throw new IllegalArgumentException("A quantidade de páginas não pode ser negativa.");
    this.qtdPaginas = qtdPaginas;
  }
}
