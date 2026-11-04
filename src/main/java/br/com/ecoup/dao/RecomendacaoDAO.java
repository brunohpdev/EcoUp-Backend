package br.com.ecoup.dao;

import br.com.ecoup.infra.ConnectionFactory;
import br.com.ecoup.model.Recomendacao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class RecomendacaoDAO {
    private final ConnectionFactory fabrica;
    public RecomendacaoDAO(ConnectionFactory fabrica) { this.fabrica = fabrica; }

    private Recomendacao montar(ResultSet rs) throws SQLException {
        return new Recomendacao(rs.getInt("id_recomendacao"), rs.getInt("id_usuario"),
                rs.getInt("id_missao"), rs.getString("mensagem"),
                rs.getDate("data_recomendacao").toLocalDate());
    }

    public Recomendacao create(Recomendacao recomendacao) {
        String sql = "INSERT INTO TB_RECOMENDACAO (id_recomendacao, id_usuario, id_missao, mensagem, data_recomendacao) " +
                "VALUES (?, ?, ?, ?, SYSDATE)";
        try (Connection conexao = fabrica.getConnection()) {
            int id;
            try (PreparedStatement seq = conexao.prepareStatement("SELECT SEQ_ECOUP_RECOMENDACAO.NEXTVAL FROM DUAL");
                 ResultSet rs = seq.executeQuery()) { rs.next(); id = rs.getInt(1); }
            try (PreparedStatement ps = conexao.prepareStatement(sql)) {
                ps.setInt(1, id);
                ps.setInt(2, recomendacao.getIdUsuario());
                ps.setInt(3, recomendacao.getIdMissao());
                ps.setString(4, recomendacao.getMensagem());
                ps.executeUpdate();
            }
            recomendacao.setId(id);
            return read(id);
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public Recomendacao read(int id) {
        try (Connection conexao = fabrica.getConnection();
             PreparedStatement ps = conexao.prepareStatement("SELECT * FROM TB_RECOMENDACAO WHERE id_recomendacao = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return montar(rs);
            }
            return null;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public List<Recomendacao> readByUsuario(int usuarioId) {
        List<Recomendacao> lista = new ArrayList<Recomendacao>();
        String sql = "SELECT * FROM TB_RECOMENDACAO WHERE id_usuario = ? ORDER BY id_recomendacao";
        try (Connection conexao = fabrica.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(montar(rs));
            }
            return lista;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public boolean update(Recomendacao recomendacao) {
        String sql = "UPDATE TB_RECOMENDACAO SET id_missao = ?, mensagem = ? WHERE id_recomendacao = ?";
        try (Connection conexao = fabrica.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, recomendacao.getIdMissao());
            ps.setString(2, recomendacao.getMensagem());
            ps.setInt(3, recomendacao.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public boolean delete(int id) {
        try (Connection conexao = fabrica.getConnection();
             PreparedStatement ps = conexao.prepareStatement("DELETE FROM TB_RECOMENDACAO WHERE id_recomendacao = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void deleteByUsuario(int usuarioId) {
        try (Connection conexao = fabrica.getConnection();
             PreparedStatement ps = conexao.prepareStatement("DELETE FROM TB_RECOMENDACAO WHERE id_usuario = ?")) {
            ps.setInt(1, usuarioId);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void deleteByMissao(int missaoId) {
        try (Connection conexao = fabrica.getConnection();
             PreparedStatement ps = conexao.prepareStatement("DELETE FROM TB_RECOMENDACAO WHERE id_missao = ?")) {
            ps.setInt(1, missaoId);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}
