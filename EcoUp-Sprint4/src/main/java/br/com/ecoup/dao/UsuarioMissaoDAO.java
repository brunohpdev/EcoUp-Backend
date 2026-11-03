package br.com.ecoup.dao;

import br.com.ecoup.infra.ConnectionFactory;
import br.com.ecoup.model.UsuarioMissao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioMissaoDAO {
    private final ConnectionFactory fabrica;
    public UsuarioMissaoDAO(ConnectionFactory fabrica) { this.fabrica = fabrica; }

    private UsuarioMissao montar(ResultSet rs) throws SQLException {
        java.time.LocalDate inicio = null;
        java.time.LocalDate fim = null;
        if (rs.getDate("data_inicio") != null) inicio = rs.getDate("data_inicio").toLocalDate();
        if (rs.getDate("data_fim") != null) fim = rs.getDate("data_fim").toLocalDate();
        return new UsuarioMissao(rs.getInt("id_usuario"), rs.getInt("id_missao"),
                rs.getString("titulo"), rs.getString("status"), inicio, fim);
    }

    private String consulta() {
        return "SELECT um.*, m.titulo FROM TB_USUARIO_MISSAO um " +
                "JOIN TB_MISSAO m ON um.id_missao = m.id_missao";
    }

    public UsuarioMissao read(int usuarioId, int missaoId) {
        String sql = consulta() + " WHERE um.id_usuario = ? AND um.id_missao = ?";
        try (Connection conexao = fabrica.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setInt(2, missaoId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return montar(rs);
            }
            return null;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public List<UsuarioMissao> readByUsuario(int usuarioId) {
        List<UsuarioMissao> lista = new ArrayList<UsuarioMissao>();
        String sql = consulta() + " WHERE um.id_usuario = ? ORDER BY um.id_missao";
        try (Connection conexao = fabrica.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(montar(rs));
            }
            return lista;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public List<UsuarioMissao> readConcluidas(int usuarioId) {
        List<UsuarioMissao> lista = new ArrayList<UsuarioMissao>();
        String sql = consulta() + " WHERE um.id_usuario = ? AND um.status = 'CONCLUIDA' ORDER BY um.data_fim DESC";
        try (Connection conexao = fabrica.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(montar(rs));
            }
            return lista;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void iniciar(int usuarioId, int missaoId) {
        String sql = "INSERT INTO TB_USUARIO_MISSAO (id_usuario, id_missao, status, data_inicio, data_fim) " +
                "VALUES (?, ?, 'EM_ANDAMENTO', SYSDATE, NULL)";
        executarDoisParametros(sql, usuarioId, missaoId);
    }

    public void concluir(int usuarioId, int missaoId, boolean existe) {
        String sql;
        if (existe) {
            sql = "UPDATE TB_USUARIO_MISSAO SET status = 'CONCLUIDA', " +
                    "data_inicio = NVL(data_inicio, SYSDATE), data_fim = SYSDATE " +
                    "WHERE id_usuario = ? AND id_missao = ?";
        } else {
            sql = "INSERT INTO TB_USUARIO_MISSAO (id_usuario, id_missao, status, data_inicio, data_fim) " +
                    "VALUES (?, ?, 'CONCLUIDA', SYSDATE, SYSDATE)";
        }
        executarDoisParametros(sql, usuarioId, missaoId);
    }

    public boolean cancelar(int usuarioId, int missaoId) {
        String sql = "UPDATE TB_USUARIO_MISSAO SET status = 'CANCELADA', data_fim = SYSDATE " +
                "WHERE id_usuario = ? AND id_missao = ? AND status <> 'CONCLUIDA'";
        return executarDoisParametros(sql, usuarioId, missaoId) > 0;
    }

    public boolean delete(int usuarioId, int missaoId) {
        String sql = "DELETE FROM TB_USUARIO_MISSAO WHERE id_usuario = ? AND id_missao = ?";
        return executarDoisParametros(sql, usuarioId, missaoId) > 0;
    }

    private int executarDoisParametros(String sql, int usuarioId, int missaoId) {
        try (Connection conexao = fabrica.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setInt(2, missaoId);
            return ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void deleteByUsuario(int usuarioId) {
        String sql = "DELETE FROM TB_USUARIO_MISSAO WHERE id_usuario = ?";
        executarUmParametro(sql, usuarioId);
    }

    public void deleteByMissao(int missaoId) {
        String sql = "DELETE FROM TB_USUARIO_MISSAO WHERE id_missao = ?";
        executarUmParametro(sql, missaoId);
    }

    private void executarUmParametro(String sql, int id) {
        try (Connection conexao = fabrica.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}
