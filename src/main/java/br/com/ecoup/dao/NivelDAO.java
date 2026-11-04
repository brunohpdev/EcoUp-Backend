package br.com.ecoup.dao;

import br.com.ecoup.infra.ConnectionFactory;
import br.com.ecoup.model.Nivel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class NivelDAO {
    private final ConnectionFactory fabrica;
    public NivelDAO(ConnectionFactory fabrica) { this.fabrica = fabrica; }

    private Nivel montar(ResultSet rs) throws SQLException {
        return new Nivel(rs.getInt("id_nivel"), rs.getInt("nivel"),
                rs.getInt("pontuacao_necessaria"), rs.getString("descricao"));
    }

    public Nivel create(Nivel nivel) {
        String sql = "INSERT INTO TB_NIVEL (id_nivel, nivel, pontuacao_necessaria, descricao) VALUES (?, ?, ?, ?)";
        try (Connection conexao = fabrica.getConnection()) {
            int id;
            try (PreparedStatement seq = conexao.prepareStatement("SELECT SEQ_ECOUP_NIVEL.NEXTVAL FROM DUAL");
                 ResultSet rs = seq.executeQuery()) { rs.next(); id = rs.getInt(1); }
            try (PreparedStatement ps = conexao.prepareStatement(sql)) {
                ps.setInt(1, id);
                ps.setInt(2, nivel.getNivel());
                ps.setInt(3, nivel.getPontuacaoNecessaria());
                ps.setString(4, nivel.getDescricao());
                ps.executeUpdate();
            }
            nivel.setId(id);
            return nivel;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public Nivel read(int id) {
        String sql = "SELECT * FROM TB_NIVEL WHERE id_nivel = ?";
        try (Connection conexao = fabrica.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return montar(rs);
            }
            return null;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public List<Nivel> readAll() {
        List<Nivel> lista = new ArrayList<Nivel>();
        try (Connection conexao = fabrica.getConnection();
             PreparedStatement ps = conexao.prepareStatement("SELECT * FROM TB_NIVEL ORDER BY nivel");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(montar(rs));
            return lista;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public Nivel nivelParaPontos(int pontos) {
        String sql = "SELECT * FROM TB_NIVEL WHERE pontuacao_necessaria <= ? " +
                "ORDER BY nivel DESC FETCH FIRST 1 ROW ONLY";
        try (Connection conexao = fabrica.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, pontos);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return montar(rs);
            }
            return null;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public boolean update(Nivel nivel) {
        String sql = "UPDATE TB_NIVEL SET nivel = ?, pontuacao_necessaria = ?, descricao = ? WHERE id_nivel = ?";
        try (Connection conexao = fabrica.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, nivel.getNivel());
            ps.setInt(2, nivel.getPontuacaoNecessaria());
            ps.setString(3, nivel.getDescricao());
            ps.setInt(4, nivel.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public boolean delete(int id) {
        try (Connection conexao = fabrica.getConnection();
             PreparedStatement ps = conexao.prepareStatement("DELETE FROM TB_NIVEL WHERE id_nivel = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}
