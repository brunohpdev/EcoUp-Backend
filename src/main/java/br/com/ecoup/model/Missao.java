package br.com.ecoup.model;

import java.time.LocalDate;

public class Missao {
    private int id;
    private int idCategoria;
    private String categoria;
    private String titulo;
    private String descricao;
    private int pontos;
    private String dificuldade;
    private LocalDate dataInicio;
    private LocalDate dataFim;

    public Missao() { }
    public Missao(int id, int idCategoria, String categoria, String titulo, String descricao,
                  int pontos, String dificuldade, LocalDate dataInicio, LocalDate dataFim) {
        this.id = id;
        this.idCategoria = idCategoria;
        this.categoria = categoria;
        this.titulo = titulo;
        this.descricao = descricao;
        this.pontos = pontos;
        this.dificuldade = dificuldade;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public int getPontos() { return pontos; }
    public void setPontos(int pontos) { this.pontos = pontos; }
    public String getDificuldade() { return dificuldade; }
    public void setDificuldade(String dificuldade) { this.dificuldade = dificuldade; }
    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }
    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }
}
