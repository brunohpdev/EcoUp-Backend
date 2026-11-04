package br.com.ecoup.dao;

import br.com.ecoup.infra.ConnectionFactory;
import br.com.ecoup.model.Avatar;
import java.sql.*;
import org.springframework.stereotype.Repository;

@Repository
public class AvatarDAO {
    private final ConnectionFactory fabrica;
    public AvatarDAO(ConnectionFactory fabrica) { this.fabrica = fabrica; }

    private Avatar montar(ResultSet rs) throws SQLException {
        return new Avatar(rs.getInt("id_avatar"), rs.getInt("id_usuario"),
                rs.getString("nome_avatar"), rs.getString("tipo_avatar"));
    }

    public Avatar create(Avatar avatar) {
        String sql = "INSERT INTO TB_AVATAR (id_avatar, id_usuario, nome_avatar, tipo_avatar) VALUES (?, ?, ?, ?)";
        try (Connection conexao = fabrica.getConnection()) {
            int id;
            try (PreparedStatement seq = conexao.prepareStatement("SELECT SEQ_ECOUP_AVATAR.NEXTVAL FROM DUAL");
                 ResultSet rs = seq.executeQuery()) { rs.next(); id = rs.getInt(1); }
            try (PreparedStatement ps = conexao.prepareStatement(sql)) {
                ps.setInt(1, id);
                ps.setInt(2, avatar.getIdUsuario());
                ps.setString(3, avatar.getNomeAvatar());
                ps.setString(4, avatar.getTipoAvatar());
                ps.executeUpdate();
            }
            avatar.setId(id);
            return avatar;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public Avatar read(int id) {
        String sql = "SELECT * FROM TB_AVATAR WHERE id_avatar = ?";
        try (Connection conexao = fabrica.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return montar(rs);
            }
            return null;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public Avatar readByUsuario(int usuarioId) {
        String sql = "SELECT * FROM TB_AVATAR WHERE id_usuario = ?";
        try (Connection conexao = fabrica.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return montar(rs);
            }
            return null;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public boolean update(Avatar avatar) {
        String sql = "UPDATE TB_AVATAR SET nome_avatar = ?, tipo_avatar = ? WHERE id_avatar = ?";
        try (Connection conexao = fabrica.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, avatar.getNomeAvatar());
            ps.setString(2, avatar.getTipoAvatar());
            ps.setInt(3, avatar.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public boolean delete(int id) {
        try (Connection conexao = fabrica.getConnection();
             PreparedStatement ps = conexao.prepareStatement("DELETE FROM TB_AVATAR WHERE id_avatar = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void deleteByUsuario(int usuarioId) {
        try (Connection conexao = fabrica.getConnection();
             PreparedStatement ps = conexao.prepareStatement("DELETE FROM TB_AVATAR WHERE id_usuario = ?")) {
            ps.setInt(1, usuarioId);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}
