package br.edu.ufersa.locadora.model.DAO;

import br.edu.ufersa.locadora.model.entities.Disco;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class DiscoDAO {
    private final static String URL = "jdbc:mysql://localhost/poo";
    private final static String USER = "poo";
    private final static String PASS = "AH443162ah";
    private static Connection con = null;

    public static Connection getConnection(){
        if(con == null){
            try{
                con = DriverManager.getConnection(URL,USER,PASS);
            }catch (SQLException e){e.printStackTrace();}
        }
        return con;
    }

    public void create(Disco disco) {
        con = getConnection();
        String sql = "INSERT INTO Discos (titulo, criadoPor, genero, valor, dataDeLancamento, qtdItens, duracao) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, disco.getTitulo());
            ps.setString(2, disco.getCriadoPor());
            ps.setString(3, disco.getGenero());
            ps.setDouble(4, disco.getValor());
            ps.setObject(5, disco.getDataDeLancamento());
            ps.setInt(6, disco.getQtdItens());
            ps.setInt(7, disco.getDuracao());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Disco> read() {
        con = getConnection();
        String sql = "SELECT * FROM Discos"; // Sem a cláusula WHERE, para trazer tudo
        List<Disco> discos = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // 1. Tratamento da data
                java.sql.Date dataBanco = rs.getDate("dataDeLancamento");
                String dataFormatada = (dataBanco != null) ? dataBanco.toString() : "";

                // 2. Conversão dos segundos do banco para Hh:Mm:Ss
                int duracaoTotalSegundos = rs.getInt("duracao");
                int horas = duracaoTotalSegundos / 3600;
                int minutos = (duracaoTotalSegundos % 3600) / 60;
                int segundos = duracaoTotalSegundos % 60;

                // 3. Instanciação correta com 10 parâmetros
                Disco disco = new Disco(
                        rs.getString("titulo"),
                        rs.getString("criadoPor"),
                        rs.getString("genero"),
                        rs.getDouble("valor"),
                        dataFormatada,
                        rs.getInt("qtdItens"),
                        true, // isDisco
                        horas,
                        minutos,
                        segundos
                );
                discos.add(disco);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return discos;
    }

    public Disco readByID(String ID) {
        String sql = "SELECT * FROM Discos WHERE ID = ?";
        Disco disco = null;

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, ID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // 1. Pegar a data do banco e converter para String
                    java.sql.Date dataBanco = rs.getDate("dataDeLancamento");
                    String dataFormatada = (dataBanco != null) ? dataBanco.toString() : "";
// Nota: Se a classe pai (ItemAcervo) exigir um formato específico como "dd/MM/yyyy",
// você precisará usar um SimpleDateFormat aqui em vez de apenas .toString().

// 2. Pegar a duração total em segundos do banco e converter de volta para horas, minutos e segundos
                    int duracaoTotalSegundos = rs.getInt("duracao");
                    int horas = duracaoTotalSegundos / 3600;
                    int minutos = (duracaoTotalSegundos % 3600) / 60;
                    int segundos = duracaoTotalSegundos % 60;

// 3. Instanciar o Disco passando os 10 parâmetros exigidos
                    disco = new Disco(
                            rs.getString("titulo"),      // titulo (String)
                            rs.getString("criadoPor"),   // criadoPor (String)
                            rs.getString("genero"),      // genero (String)
                            rs.getDouble("valor"),       // valor (double)
                            dataFormatada,               // dataDeLancamentoFormatada (String)
                            rs.getInt("qtdItens"),       // qtdItens (int)
                            true,                        // isDisco (boolean - passando true diretamente)
                            horas,                       // horas (int)
                            minutos,                     // minutos (int)
                            segundos                     // segundos (int)
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return disco;
    }

    public void update(Disco disco) {
        String sql = "UPDATE Discos SET titulo = ?, criadoPor = ?, genero = ?, valor = ?, dataDeLancamento = ?, qtdItens = ?, duracao = ? WHERE ID = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, disco.getTitulo());
            ps.setString(2, disco.getCriadoPor());
            ps.setString(3, disco.getGenero());
            ps.setDouble(4, disco.getValor());
            ps.setObject(5, disco.getDataDeLancamento());
            ps.setInt(6, disco.getQtdItens());
            ps.setInt(7, disco.getDuracao());
            ps.setString(8, disco.getID());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(String ID) {
        String sql = "DELETE FROM Discos WHERE ID = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, ID);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}