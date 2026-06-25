package br.edu.ufersa.locadora.model.DAO;

import br.edu.ufersa.locadora.model.entities.Disco;
import br.edu.ufersa.locadora.model.entities.ItemAcervo;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

public class DiscoDAO {
    private static final DateTimeFormatter FORMATADOR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final ConnectionFactory connectionFactory;

    public DiscoDAO(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void updateQuantidade(String id, int novaQuantidade) {
        String sql = "UPDATE Discos SET qtdItens = ? WHERE ID = ?";
        try (Connection con = connectionFactory.createConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, novaQuantidade);
            ps.setString(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void create(Disco disco) {
        String sql = "INSERT INTO Discos (ID, titulo, criadoPor, genero, valor, dataDeLancamento, qtdItens, isDisco, duracao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = connectionFactory.createConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, disco.getID());
            ps.setString(2, disco.getTitulo());
            ps.setString(3, disco.getCriadoPor());
            ps.setString(4, disco.getGenero());
            ps.setDouble(5, disco.getValor());
            ps.setDate(6, Date.valueOf(LocalDate.parse(disco.getDataDeLancamento(), FORMATADOR)));
            ps.setInt(7, disco.getQtdItens());
            ps.setBoolean(8, disco.getIsDisco());
            ps.setInt(9, disco.getDuracao());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir disco no banco: " + e.getMessage(), e);
        }
    }

    public List<Disco> read() {
        String sql = "SELECT * FROM Discos"; // Sem a cláusula WHERE, para trazer tudo
        List<Disco> discos = new ArrayList<>();

        try (Connection con = connectionFactory.createConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                discos.add(mapearDisco(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar discos: " + e.getMessage(), e);
        }
        return discos;
    }

    public Disco readByID(String ID) {
        String sql = "SELECT * FROM Discos WHERE ID = ?";
        Disco disco = null;

        try (Connection con = connectionFactory.createConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    disco = mapearDisco(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar disco por ID: " + e.getMessage(), e);
        }
        return disco;
    }

    public void update(Disco disco) {
        String sql = "UPDATE Discos SET titulo = ?, criadoPor = ?, genero = ?, valor = ?, dataDeLancamento = ?, qtdItens = ?, duracao = ? WHERE ID = ?";

        try (Connection con = connectionFactory.createConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, disco.getTitulo());
            ps.setString(2, disco.getCriadoPor());
            ps.setString(3, disco.getGenero());
            ps.setDouble(4, disco.getValor());
            ps.setDate(5, Date.valueOf(LocalDate.parse(disco.getDataDeLancamento(), FORMATADOR)));
            ps.setInt(6, disco.getQtdItens());
            ps.setInt(7, disco.getDuracao());
            ps.setString(8, disco.getID());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar disco: " + e.getMessage(), e);
        }
    }

    public void delete(String ID) {
        String sql = "DELETE FROM Discos WHERE ID = ?";

        try (Connection con = connectionFactory.createConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ID);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir disco: " + e.getMessage(), e);
        }
    }

    private Disco mapearDisco(ResultSet rs) throws SQLException {
        int duracaoTotalSegundos = rs.getInt("duracao");
        int horas = duracaoTotalSegundos / 3600;
        int minutos = (duracaoTotalSegundos % 3600) / 60;
        int segundos = duracaoTotalSegundos % 60;

        Disco disco = new Disco(
                rs.getString("titulo"),
                rs.getString("criadoPor"),
                rs.getString("genero"),
                rs.getDouble("valor"),
                formatarData(rs.getDate("dataDeLancamento")),
                rs.getInt("qtdItens"),
                rs.getBoolean("isDisco"),
                horas,
                minutos,
                segundos
        );
        definirId(disco, rs.getString("ID"));
        return disco;
    }

    private String formatarData(Date dataBanco) {
        if (dataBanco == null) {
            throw new RuntimeException("Data de lançamento ausente no registro de disco.");
        }
        return dataBanco.toLocalDate().format(FORMATADOR);
    }

    private void definirId(Disco disco, String id) {
        try {
            Field field = ItemAcervo.class.getDeclaredField("ID");
            field.setAccessible(true);
            field.set(disco, id);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Erro ao aplicar ID ao disco lido do banco.", e);
        }
    }
}