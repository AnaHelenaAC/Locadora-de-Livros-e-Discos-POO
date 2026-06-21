package br.edu.ufersa.locadora.model.DAO;

import br.edu.ufersa.locadora.model.entities.Livro;
import br.edu.ufersa.locadora.model.entities.ItemAcervo;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LivroDAO {
    private static final DateTimeFormatter FORMATADOR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public void create(Livro livro) {
        String query = "INSERT INTO Livros (ID, titulo, criadoPor, genero, valor, dataDeLancamento, qtdItens, isDisco, qtdPaginas) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, livro.getID());
            ps.setString(2, livro.getTitulo());
            ps.setString(3, livro.getCriadoPor());
            ps.setString(4, livro.getGenero());
            ps.setDouble(5, livro.getValor());
            ps.setDate(6, Date.valueOf(LocalDate.parse(livro.getDataDeLancamento(), FORMATADOR)));
            ps.setInt(7, livro.getQtdItens());
            ps.setBoolean(8, livro.getIsDisco());
            ps.setInt(9, livro.getQtdPaginas());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir livro no banco: " + e.getMessage(), e);
        }
    }

    public List<Livro> read() {
        String query = "SELECT * FROM Livros";
        List<Livro> livros = new ArrayList<>();

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Livro livro = mapearLivro(rs);
                livros.add(livro);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar livros: " + e.getMessage(), e);
        }
        return livros;
    }

    public Livro readByID(String ID) {
        String query = "SELECT * FROM Livros WHERE ID = ?";
        Livro livro = null;

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, ID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    livro = mapearLivro(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar livro por ID: " + e.getMessage(), e);
        }
        return livro;
    }

    public void update(Livro livro) {
        String query = "UPDATE Livros SET titulo = ?, criadoPor = ?, genero = ?, valor = ?, dataDeLancamento = ?, qtdItens = ?, qtdPaginas = ? WHERE ID = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, livro.getTitulo());
            ps.setString(2, livro.getCriadoPor());
            ps.setString(3, livro.getGenero());
            ps.setDouble(4, livro.getValor());
            ps.setDate(5, Date.valueOf(LocalDate.parse(livro.getDataDeLancamento(), FORMATADOR)));
            ps.setInt(6, livro.getQtdItens());
            ps.setInt(7, livro.getQtdPaginas());
            ps.setString(8, livro.getID());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar livro: " + e.getMessage(), e);
        }
    }

    public void delete(String ID) {
        String query = "DELETE FROM Livros WHERE ID = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, ID);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir livro: " + e.getMessage(), e);
        }
    }

    private Livro mapearLivro(ResultSet rs) throws SQLException {
        Livro livro = new Livro(
                rs.getString("titulo"),
                rs.getString("criadoPor"),
                rs.getString("genero"),
                rs.getDouble("valor"),
                formatarData(rs.getDate("dataDeLancamento")),
                rs.getInt("qtdItens"),
                rs.getBoolean("isDisco"),
                rs.getInt("qtdPaginas")
        );
        definirId(livro, rs.getString("ID"));
        return livro;
    }

    private String formatarData(Date dataBanco) {
        if (dataBanco == null) {
            throw new RuntimeException("Data de lançamento ausente no registro de livro.");
        }
        return dataBanco.toLocalDate().format(FORMATADOR);
    }

    private void definirId(Livro livro, String id) {
        try {
            Field field = ItemAcervo.class.getDeclaredField("ID");
            field.setAccessible(true);
            field.set(livro, id);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Erro ao aplicar ID ao livro lido do banco.", e);
        }
    }
}