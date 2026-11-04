package br.com.ecoup.model;

public class Nivel {
    private int id;
    private int nivel;
    private int pontuacaoNecessaria;
    private String descricao;

    public Nivel() { }
    public Nivel(int id, int nivel, int pontuacaoNecessaria, String descricao) {
        this.id = id;
        this.nivel = nivel;
        this.pontuacaoNecessaria = pontuacaoNecessaria;
        this.descricao = descricao;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getNivel() { return nivel; }
    public void setNivel(int nivel) { this.nivel = nivel; }
    public int getPontuacaoNecessaria() { return pontuacaoNecessaria; }
    public void setPontuacaoNecessaria(int pontuacaoNecessaria) { this.pontuacaoNecessaria = pontuacaoNecessaria; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}
