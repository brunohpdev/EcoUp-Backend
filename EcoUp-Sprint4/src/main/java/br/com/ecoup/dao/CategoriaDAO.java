package br.com.ecoup.dao;

import br.com.ecoup.infra.ConnectionFactory;
import br.com.ecoup.model.Categoria;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CategoriaDAO {
    private final ConnectionFactory fabrica;
    public CategoriaDAO(ConnectionFactory fabrica) { this.fabrica = fabrica; }

    private Categoria montar(ResultSet rs) throws SQLException {
        return new Categoria(rs.getInt("id_categoria"), rs.getString("nome_categoria"),
                rs.getString("descricao_categoria"));
    }

    public Categoria create(Categoria categoria) {
        String sql = "INSERT INTO TB_CATEGORIA (id_categoria, nome_categoria, descricao_categoria) VALUES (?, ?, ?)";
        try (Connection conexao = fabrica.getConnection()) {
            int id;
            try (PreparedStatement seq = conexao.prepareStatement("SELECT SEQ_ECOUP_CATEGORIA.NEXTVAL FROM DUAL");
                 ResultSet rs = seq.executeQuery()) { rs.next(); id = rs.getInt(1); }
            try (PreparedStatement ps = conexao.prepareStatement(sql)) {
                ps.setInt(1, id);
                ps.setString(2, categoria.getNomeCategoria());
                ps.setString(3, categoria.getDescricaoCategoria());
                ps.executeUpdate();
            }
            categoria.setId(id);
            return categoria;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public Categoria read(int id) {
        String sql = "SELECT * FROM TB_CATEGORIA WHERE id_categoria = ?";
        try (Connection conexao = fabrica.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return montar(rs);
            }
            return null;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public List<Categoria> readAll() {
        List<Categoria> lista = new ArrayList<Categoria>();
        try (Connection conexao = fabrica.getConnection();
             PreparedStatement ps = conexao.prepareStatement("SELECT * FROM TB_CATEGORIA ORDER BY id_categoria");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(montar(rs));
            return lista;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public boolean update(Categoria categoria) {
        String sql = "UPDATE TB_CATEGORIA SET nome_categoria = ?, descricao_categoria = ? WHERE id_categoria = ?";
        try (Connection conexao = fabrica.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, categoria.getNomeCategoria());
            ps.setString(2, categoria.getDescricaoCategoria());
            ps.setInt(3, categoria.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public boolean delete(int id) {
        try (Connection conexao = fabrica.getConnection();
             PreparedStatement ps = conexao.prepareStatement("DELETE FROM TB_CATEGORIA WHERE id_categoria = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}
