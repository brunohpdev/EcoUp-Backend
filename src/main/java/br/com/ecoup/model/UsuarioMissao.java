package br.com.ecoup.model;

import java.time.LocalDate;

public class UsuarioMissao {
    private int idUsuario;
    private int idMissao;
    private String tituloMissao;
    private String status;
    private LocalDate dataInicio;
    private LocalDate dataFim;

    public UsuarioMissao() { }
    public UsuarioMissao(int idUsuario, int idMissao, String tituloMissao, String status,
                         LocalDate dataInicio, LocalDate dataFim) {
        this.idUsuario = idUsuario;
        this.idMissao = idMissao;
        this.tituloMissao = tituloMissao;
        this.status = status;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public int getIdMissao() { return idMissao; }
    public void setIdMissao(int idMissao) { this.idMissao = idMissao; }
    public String getTituloMissao() { return tituloMissao; }
    public void setTituloMissao(String tituloMissao) { this.tituloMissao = tituloMissao; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }
    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }
}
