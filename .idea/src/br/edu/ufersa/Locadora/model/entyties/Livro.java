package src.br.edu.ufersa.Locadora.model.entities.Livro;

import src.br.edu.ufersa.Locadora.model.entities.ItemAcervo;

public class Livro extends ItemAcervo {
  //Atributo:
  private int qtdPaginas = 0;

  //Métodos:

  //getter:
  public int getQtdPaginas() {
    return qtdPaginas;
  }
  //setter:
  public void setQtdPaginas(int qtdPaginas) {
    this.qtdPaginas = qtdPaginas;
  }
  //construtor:
  public Livro(String titulo, String criadoPor, String genero, double valor, String dataDeLancamentoFormatada, int qtdItens, boolean isDisco, int qtdPaginas) {
    super(titulo, criadoPor, genero, valor, dataDeLancamentoFormatada, qtdItens, isDisco);
    this.qtdPaginas = qtdPaginas;
  }
}
