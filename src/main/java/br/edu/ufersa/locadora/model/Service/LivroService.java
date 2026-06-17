package br.edu.ufersa.locadora.model.Service;

import br.edu.ufersa.locadora.model.DAO.LivroDAO;
import br.edu.ufersa.locadora.model.entities.Livro;
import br.edu.ufersa.locadora.exceptions.LivroException;

import java.util.List;
import java.util.ArrayList;

public class LivroService {
    private final LivroDAO livroDAO;

    public LivroService(LivroDAO LivroDAO) {
        this.livroDAO = LivroDAO;
    }

    public void adicionarLivro(Livro livro) throws LivroException {
        if (livro == null) throw new LivroException("Não é possível enviar um formulário vazio.");
        if (livro.getTitulo().length() > 100 || livro.getCriadoPor().length() > 100 || livro.getGenero().length() > 100) throw new LivroException("Campos de texto do formulário (Título, Criado Por e Gênero) não podem conter mais de 100 caracteres.");
        if (livro.getValor() < 0 || livro.getQtdItens() < 0 || livro.getQtdPaginas() < 0) throw new LivroException("Os campos Valor, Quantidade de Itens e Quantidade de Páginas não podem ter valores negativos.");

        livroDAO.create(livro);
        System.out.println(livro.getTitulo() + " adicionado com sucesso.");
    }
    public List<Livro> lerLivro() throws LivroException {
        if (livroDAO.read().isEmpty()) throw new LivroException("Nenhum livro foi encontrado no banco de dados.");
        else {
            System.out.println("Livro(s) encontrado(s).");
            return livroDAO.read();
        }
    }
    public Livro lerLivroPorID(String ID) throws LivroException {
        if (livroDAO.readByID(ID) == null) throw new LivroException("Nenhum livro foi com o ID utilizado (" + ID + ") foi encontrado no banco de dados.");
        else {
            System.out.println("Livro encontrado.");
            return livroDAO.readByID(ID);
        }
    }
    public void atualizarLivro(Livro livro) throws LivroException {
        if (livro == null) throw new LivroException("Não é possível enviar um formulário vazio.");
        if (livroDAO.readByID(livro.getID()) == null) throw new LivroException("Nenhum livro foi com o ID utilizado (" + livro.getID() + ") foi encontrado no banco de dados.");
        if (livro.getTitulo().length() > 100 || livro.getCriadoPor().length() > 100 || livro.getGenero().length() > 100) throw new LivroException("Campos de texto do formulário (Título, Criado Por e Gênero) não podem conter mais de 100 caracteres.");
        if (livro.getValor() < 0 || livro.getQtdItens() < 0 || livro.getQtdPaginas() < 0) throw new LivroException("Os campos Valor, Quantidade de Itens e Quantidade de Páginas não podem ter valores negativos.");

        livroDAO.update(livro);
        System.out.println(livro.getTitulo() + " alterado com sucesso.");
    }
    public void apagarLivro(String ID) throws LivroException {
        if (livroDAO.readByID(ID) == null) {
            throw new LivroException("Nenhum livro foi com o ID utilizado (" + ID + ") foi encontrado no banco de dados.");
        }
        livroDAO.delete(ID);
        System.out.println("Livro apagado.");
    }
}
