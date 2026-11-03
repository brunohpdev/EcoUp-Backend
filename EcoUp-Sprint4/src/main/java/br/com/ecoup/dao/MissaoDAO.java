package br.com.ecoup.dao;

import br.com.ecoup.infra.ConnectionFactory;
import br.com.ecoup.model.Missao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class MissaoDAO {
    private final ConnectionFactory fabrica;
    public MissaoDAO(ConnectionFactory fabrica) { this.fabrica = fabrica; }

    private Missao montar(ResultSet rs) throws SQLException {
        Date fim = rs.getDate("data_fim");
        java.time.LocalDate dataFim = null;
        if (fim != null) dataFim = fim.toLocalDate();
        return new Missao(rs.getInt("id_missao"), rs.getInt("id_categoria"),
                rs.getString("nome_categoria"), rs.getString("titulo"),
                rs.getString("descricao"), rs.getInt("pontos_recompensa"),
                rs.getString("dificuldade"), rs.getDate("data_inicio").toLocalDate(), dataFim);
    }

    private String consulta() {
        return "SELECT m.*, c.nome_categoria FROM TB_MISSAO m " +
                "JOIN TB_CATEGORIA c ON c.id_categoria = m.id_categoria";
    }

    public Missao create(Missao missao) {
        String sql = "INSERT INTO TB_MISSAO (id_missao, id_categoria, titulo, descricao, pontos_recompensa, " +
                "dificuldade, data_inicio, data_fim) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conexao = fabrica.getConnection()) {
            int id;
            try (PreparedStatement seq = conexao.prepareStatement("SELECT SEQ_ECOUP_MISSAO.NEXTVAL FROM DUAL");
                 ResultSet rs = seq.executeQuery()) { rs.next(); id = rs.getInt(1); }
            try (PreparedStatement ps = conexao.prepareStatement(sql)) {
                ps.setInt(1, id);
                ps.setInt(2, missao.getIdCategoria());
                ps.setString(3, missao.getTitulo());
                ps.setString(4, missao.getDescricao());
                ps.setInt(5, missao.getPontos());
                ps.setString(6, missao.getDificuldade());
                ps.setDate(7, Date.valueOf(missao.getDataInicio()));
                if (missao.getDataFim() == null) ps.setNull(8, Types.DATE);
                else ps.setDate(8, Date.valueOf(missao.getDataFim()));
                ps.executeUpdate();
            }
            missao.setId(id);
            return read(id);
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public Missao read(int id) {
        String sql = consulta() + " WHERE m.id_missao = ?";
        try (Connection conexao = fabrica.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return montar(rs);
            }
            return null;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public List<Missao> readAll() {
        List<Missao> lista = new ArrayList<Missao>();
        try (Connection conexao = fabrica.getConnection();
             PreparedStatement ps = conexao.prepareStatement(consulta() + " ORDER BY m.id_missao");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(montar(rs));
            return lista;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public boolean update(Missao missao) {
        String sql = "UPDATE TB_MISSAO SET id_categoria = ?, titulo = ?, descricao = ?, pontos_recompensa = ?, " +
                "dificuldade = ?, data_inicio = ?, data_fim = ? WHERE id_missao = ?";
        try (Connection conexao = fabrica.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, missao.getIdCategoria());
            ps.setString(2, missao.getTitulo());
            ps.setString(3, missao.getDescricao());
            ps.setInt(4, missao.getPontos());
            ps.setString(5, missao.getDificuldade());
            ps.setDate(6, Date.valueOf(missao.getDataInicio()));
            if (missao.getDataFim() == null) ps.setNull(7, Types.DATE);
            else ps.setDate(7, Date.valueOf(missao.getDataFim()));
            ps.setInt(8, missao.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public boolean delete(int id) {
        try (Connection conexao = fabrica.getConnection();
             PreparedStatement ps = conexao.prepareStatement("DELETE FROM TB_MISSAO WHERE id_missao = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}
