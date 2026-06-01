package br.edu.ufersa.locadora.model.DAO;

import src.main.java.br.edu.ufersa.locadora.model.entities.Disco;

import java.util.List;
import java.util.ArrayList;

public class DiscoDAO {
    public void create(Disco disco) {
        String query = "INSERT INTO Discos (titulo, criadoPor, genero, valor, dataDeLancamento, qtdItens, duracao) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection con = ConnectionFactory.getConnection(URL,USER,PASS); 
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, disco.getTitulo());
            ps.setString(2, disco.getCriadoPor());
            ps.setString(3, disco.getGenero());
            ps.setDouble(4, disco.getValor());
            ps.setObject(5, disco.getDataDeLancamento());
            ps.setInt(6, disco.getQtdItens());
            ps.setInt(7, disco.getDuracao());
            ps.executeUpdate();
        }
        catch (SQLException e) {e.printStackTrace();}
    }
    public List<Disco> read(String ID) {
        String query = "SELECT * FROM Discos WHERE ID = ?";
        List<Disco> discos = new ArrayList<>();
        
        try (Connection con = ConnectionFactory.getConnection(URL,USER,PASS); 
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                    Disco disco = new Disco(
                        rs.getString("titulo"),
                        rs.getString("criadoPor"),
                        rs.getString("genero"),
                        rs.getDouble("valor"),
                        rs.getDate("dataDeLancamento"),
                        rs.getInt("qtdItens"),
                        rs.getInt("duracao")
                        );
                    discos.add(disco);
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        return discos;
    }
    public Disco readByID(String ID) {
        String query = "SELECT * FROM Discos WHERE ID = ?";
        Disco disco = null;
        
        try (Connection con = ConnectionFactory.getConnection(URL,USER,PASS); 
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, ID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    disco = new Disco(
                        rs.getString("titulo"),
                        rs.getString("criadoPor"),
                        rs.getString("genero"),
                        rs.getDouble("valor"),
                        rs.getDate("dataDeLancamento"),
                        rs.getInt("qtdItens"),
                        rs.getInt("duracao")
                        );
                }
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        return disco;
    }
    public void update(Disco disco) {
        String query = "UPDATE Discos SET titulo = ?, criadoPor = ?, genero = ?, valor = ?, dataDeLancamento = ?, qtdItens = ?, duracao = ? WHERE ID = ?";
        
        try (Connection con = ConnectionFactory.getConnection(URL,USER,PASS); 
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, disco.getTitulo());
            ps.setString(2, disco.getCriadoPor());
            ps.setString(3, disco.getGenero());
            ps.setDouble(4, disco.getValor());
            ps.setObject(5, disco.getDataDeLancamento());
            ps.setInt(6, disco.getQtdItens());
            ps.setInt(7, disco.getDuracao());
            ps.setString(8, disco.getID());
            ps.executeUpdate();
        }
        catch (SQLException e) {e.printStackTrace();}
    }
    public void delete(String ID) {
        String query = "DELETE FROM Discos WHERE ID = ?";
        
        try (Connection con = ConnectionFactory.getConnection(URL,USER,PASS); 
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, ID);
            ps.executeUpdate();
        }
        catch (SQLException e) {e.printStackTrace();}
    }
}
