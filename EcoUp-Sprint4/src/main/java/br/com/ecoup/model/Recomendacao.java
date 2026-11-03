package br.com.ecoup.model;

import java.time.LocalDate;

public class Recomendacao {
    private int id;
    private int idUsuario;
    private int idMissao;
    private String mensagem;
    private LocalDate dataRecomendacao;

    public Recomendacao() { }
    public Recomendacao(int id, int idUsuario, int idMissao, String mensagem, LocalDate dataRecomendacao) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idMissao = idMissao;
        this.mensagem = mensagem;
        this.dataRecomendacao = dataRecomendacao;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public int getIdMissao() { return idMissao; }
    public void setIdMissao(int idMissao) { this.idMissao = idMissao; }
    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
    public LocalDate getDataRecomendacao() { return dataRecomendacao; }
    public void setDataRecomendacao(LocalDate dataRecomendacao) { this.dataRecomendacao = dataRecomendacao; }
}
