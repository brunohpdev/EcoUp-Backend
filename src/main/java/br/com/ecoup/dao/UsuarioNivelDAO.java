package br.com.ecoup.dao;

import br.com.ecoup.infra.ConnectionFactory;
import br.com.ecoup.model.UsuarioNivel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioNivelDAO {
    private final ConnectionFactory fabrica;
    public UsuarioNivelDAO(ConnectionFactory fabrica) { this.fabrica = fabrica; }

    public List<UsuarioNivel> readByUsuario(int usuarioId) {
        List<UsuarioNivel> lista = new ArrayList<UsuarioNivel>();
        String sql = "SELECT un.*, n.nivel FROM TB_USUARIO_NIVEL un " +
                "JOIN TB_NIVEL n ON n.id_nivel = un.id_nivel " +
                "WHERE un.id_usuario = ? ORDER BY n.nivel";
        try (Connection conexao = fabrica.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new UsuarioNivel(rs.getInt("id_usuario"), rs.getInt("id_nivel"),
                            rs.getInt("nivel"), rs.getDate("data_nivel").toLocalDate()));
                }
            }
            return lista;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void registrarSeNovo(int usuarioId, int nivelId) {
        String consulta = "SELECT COUNT(*) FROM TB_USUARIO_NIVEL WHERE id_usuario = ? AND id_nivel = ?";
        String inserir = "INSERT INTO TB_USUARIO_NIVEL (id_nivel, id_usuario, data_nivel) VALUES (?, ?, SYSDATE)";
        try (Connection conexao = fabrica.getConnection()) {
            int total = 0;
            try (PreparedStatement ps = conexao.prepareStatement(consulta)) {
                ps.setInt(1, usuarioId);
                ps.setInt(2, nivelId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) total = rs.getInt(1);
                }
            }
            if (total == 0) {
                try (PreparedStatement ps = conexao.prepareStatement(inserir)) {
                    ps.setInt(1, nivelId);
                    ps.setInt(2, usuarioId);
                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void deleteByUsuario(int usuarioId) {
        String sql = "DELETE FROM TB_USUARIO_NIVEL WHERE id_usuario = ?";
        try (Connection conexao = fabrica.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}
