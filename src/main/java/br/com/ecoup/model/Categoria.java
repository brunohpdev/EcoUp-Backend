package br.com.ecoup.model;

public class Categoria {
    private int id;
    private String nomeCategoria;
    private String descricaoCategoria;

    public Categoria() { }
    public Categoria(int id, String nomeCategoria, String descricaoCategoria) {
        this.id = id;
        this.nomeCategoria = nomeCategoria;
        this.descricaoCategoria = descricaoCategoria;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNomeCategoria() { return nomeCategoria; }
    public void setNomeCategoria(String nomeCategoria) { this.nomeCategoria = nomeCategoria; }
    public String getDescricaoCategoria() { return descricaoCategoria; }
    public void setDescricaoCategoria(String descricaoCategoria) { this.descricaoCategoria = descricaoCategoria; }
}
