package br.com.ecoup.dao;

import br.com.ecoup.infra.ConnectionFactory;
import br.com.ecoup.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioDAO {
    private final ConnectionFactory fabrica;

    public UsuarioDAO(ConnectionFactory fabrica) { this.fabrica = fabrica; }

    private Usuario montarUsuario(ResultSet rs) throws SQLException {
        return new Usuario(rs.getInt("id_usuario"), rs.getString("nome"),
                rs.getString("email"), rs.getInt("pontuacao_atual"),
                rs.getTimestamp("data_cadastro").toLocalDateTime());
    }

    private int proximoId(Connection conexao) throws SQLException {
        String sql = "SELECT SEQ_ECOUP_USUARIO.NEXTVAL FROM DUAL";
        try (PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }

    public Usuario create(Usuario usuario, String senhaHash) {
        String sql = "INSERT INTO TB_USUARIO (id_usuario, nome, email, senha, pontuacao_atual, data_cadastro) " +
                "VALUES (?, ?, ?, ?, 0, SYSDATE)";
        try (Connection conexao = fabrica.getConnection()) {
            int id = proximoId(conexao);
            try (PreparedStatement ps = conexao.prepareStatement(sql)) {
                ps.setInt(1, id);
                ps.setString(2, usuario.getNome());
                ps.setString(3, usuario.getEmail());
                ps.setString(4, senhaHash);
                ps.executeUpdate();
            }
            usuario.setId(id);
            return read(id);
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public Usuario read(int id) {
        String sql = "SELECT * FROM TB_USUARIO WHERE id_usuario = ?";
        try (Connection conexao = fabrica.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return montarUsuario(rs);
            }
            return null;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public List<Usuario> readAll() {
        List<Usuario> usuarios = new ArrayList<Usuario>();
        String sql = "SELECT * FROM TB_USUARIO ORDER BY id_usuario";
        try (Connection conexao = fabrica.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) usuarios.add(montarUsuario(rs));
            return usuarios;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public boolean update(int id, String nome, String email) {
        String sql = "UPDATE TB_USUARIO SET nome = ?, email = ? WHERE id_usuario = ?";
        try (Connection conexao = fabrica.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, nome);
            ps.setString(2, email);
            ps.setInt(3, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void updateSenha(int id, String hash) {
        String sql = "UPDATE TB_USUARIO SET senha = ? WHERE id_usuario = ?";
        try (Connection conexao = fabrica.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, hash);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void atualizarPontos(int id, int pontos) {
        String sql = "UPDATE TB_USUARIO SET pontuacao_atual = ? WHERE id_usuario = ?";
        try (Connection conexao = fabrica.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, pontos);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM TB_USUARIO WHERE id_usuario = ?";
        try (Connection conexao = fabrica.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public Usuario buscarPorEmail(String email) {
        String sql = "SELECT * FROM TB_USUARIO WHERE email = ?";

        try (Connection conexao = fabrica.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return montarUsuario(rs);
                }
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String buscarSenhaPorEmail(String email) {
        String sql = "SELECT senha FROM TB_USUARIO WHERE email = ?";

        try (Connection conexao = fabrica.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("senha");
                }
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
