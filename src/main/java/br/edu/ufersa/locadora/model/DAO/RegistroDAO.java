package br.edu.ufersa.locadora.model.DAO;

import br.edu.ufersa.locadora.exceptions.RegistroException;
import br.edu.ufersa.locadora.model.entities.Registro;
import br.edu.ufersa.locadora.model.entities.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class RegistroDAO {

    public Registro Create(Registro entity) {
        String sql = "INSERT INTO tb_registro (faturamento_total, id_gerente_logado) VALUES (?, ?)";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDouble(1, entity.getFaturamentoTotal());

            if (entity.getGerenteLogado() != null) {
                stmt.setLong(2, entity.getGerenteLogado().getId());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.execute();

            try (ResultSet rset = stmt.getGeneratedKeys()) {
                if (rset.next()) {
                    entity.setIdRegistro(rset.getLong(1));
                }
            }
        } catch (SQLException exe) {
            throw new RuntimeException("Erro ao inserir o registro no banco de dados: " + exe.getMessage(), exe);
        }

        return entity;
    }

    public List<Registro> Read(String param) {
        String sql = "SELECT * FROM tb_registro WHERE id_registro = ?";
        List<Registro> lista = new ArrayList<>();

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setLong(1, Long.parseLong(param));
            try (ResultSet rset = stmt.executeQuery()) {
                while (rset.next()) {
                    Registro reg = new Registro();
                    reg.setIdRegistro(rset.getLong("id_registro"));
                    try {
                        reg.setFaturamentoTotal(rset.getDouble("faturamento_total"));
                    } catch (RegistroException e) {
                        throw new RuntimeException("Erro ao definir faturamento: " + e.getMessage(), e);
                    }

                    long idGerente = rset.getLong("id_gerente_logado");
                    if (!rset.wasNull()) {
                        UsuarioDAO usuarioDAO = new UsuarioDAO();
                        Usuario gerente = usuarioDAO.ReadPorId(idGerente);
                        if (gerente != null) {
                            try {
                                reg.setGerenteLogado(gerente);
                            } catch (RegistroException e) {
                                throw new RuntimeException("Erro ao definir gerente no registro: " + e.getMessage(), e);
                            }
                        }
                    }

                    lista.add(reg);
                }
            }
        } catch (SQLException exe) {
            throw new RuntimeException("Erro ao buscar o registro no banco de dados: " + exe.getMessage(), exe);
        }

        return lista;
    }

    public boolean Update(Registro entity) {
        String sql = "UPDATE tb_registro SET faturamento_total = ?, id_gerente_logado = ? WHERE id_registro = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setDouble(1, entity.getFaturamentoTotal());

            if (entity.getGerenteLogado() != null) {
                stmt.setLong(2, entity.getGerenteLogado().getId()); // CORRIGIDO: getId()
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.setLong(3, entity.getIdRegistro());

            return stmt.executeUpdate() > 0;
        } catch (SQLException exe) {
            throw new RuntimeException("Erro ao atualizar o registro no banco de dados: " + exe.getMessage(), exe);
        }
    }

    public boolean Delete(Registro entity) {
        String sql = "DELETE FROM tb_registro WHERE id_registro = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setLong(1, entity.getIdRegistro());

            return stmt.executeUpdate() > 0;
        } catch (SQLException exe) {
            throw new RuntimeException("Erro ao deletar o registro no banco de dados: " + exe.getMessage(), exe);
        }
    }
}