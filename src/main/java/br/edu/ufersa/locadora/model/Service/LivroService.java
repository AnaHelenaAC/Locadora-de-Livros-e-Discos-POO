package br.edu.ufersa.locadora.model.Service;

import br.edu.ufersa.locadora.model.DAO.LivroDAO;
import br.edu.ufersa.locadora.model.entities.Livro;
import br.edu.ufersa.locadora.exceptions.LivroException;

import java.util.ArrayList;
import java.util.List;

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
    }
    public List<Livro> lerLivro() throws LivroException {
        if (livroDAO.read().isEmpty()) throw new LivroException("Nenhum livro foi encontrado no banco de dados.");
        else {
            return livroDAO.read();
        }
    }
    public Livro lerLivroPorID(String ID) throws LivroException {
        if (livroDAO.readByID(ID) == null) throw new LivroException("Nenhum livro foi com o ID utilizado (" + ID + ") foi encontrado no banco de dados.");
        else {
            return livroDAO.readByID(ID);
        }
    }
    public void atualizarLivro(Livro livro) throws LivroException {
        if (livro == null) throw new LivroException("Não é possível enviar um formulário vazio.");
        if (livroDAO.readByID(livro.getID()) == null) throw new LivroException("Nenhum livro foi com o ID utilizado (" + livro.getID() + ") foi encontrado no banco de dados.");
        if (livro.getTitulo().length() > 100 || livro.getCriadoPor().length() > 100 || livro.getGenero().length() > 100) throw new LivroException("Campos de texto do formulário (Título, Criado Por e Gênero) não podem conter mais de 100 caracteres.");
        if (livro.getValor() < 0 || livro.getQtdItens() < 0 || livro.getQtdPaginas() < 0) throw new LivroException("Os campos Valor, Quantidade de Itens e Quantidade de Páginas não podem ter valores negativos.");

        livroDAO.update(livro);
    }
    public void apagarLivro(String ID) throws LivroException {
        if (livroDAO.readByID(ID) == null) {
            throw new LivroException("Nenhum livro foi com o ID utilizado (" + ID + ") foi encontrado no banco de dados.");
        }
        livroDAO.delete(ID);
    }

    public List<Livro> buscarPor(String campo, String termo) throws LivroException {
        if (termo == null) {
            throw new LivroException("O termo de busca não pode ser nulo.");
        }

        String termoNormalizado = termo.trim().toLowerCase();
        List<Livro> resultado = new ArrayList<>();

        for (Livro livro : livroDAO.read()) {
            boolean corresponde;
            switch (campo == null ? "" : campo.trim().toLowerCase()) {
                case "autor", "criadopor" -> corresponde = livro.getCriadoPor() != null && livro.getCriadoPor().toLowerCase().contains(termoNormalizado);
                case "genero" -> corresponde = livro.getGenero() != null && livro.getGenero().toLowerCase().contains(termoNormalizado);
                case "titulo" -> corresponde = livro.getTitulo() != null && livro.getTitulo().toLowerCase().contains(termoNormalizado);
                default -> corresponde = livro.getTitulo() != null && livro.getTitulo().toLowerCase().contains(termoNormalizado);
            }

            if (corresponde) {
                resultado.add(livro);
            }
        }

        return resultado;
    }
}
