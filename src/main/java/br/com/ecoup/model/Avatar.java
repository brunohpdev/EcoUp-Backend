package br.com.ecoup.model;

public class Avatar {
    private int id;
    private int idUsuario;
    private String nomeAvatar;
    private String tipoAvatar;

    public Avatar() { }
    public Avatar(int id, int idUsuario, String nomeAvatar, String tipoAvatar) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.nomeAvatar = nomeAvatar;
        this.tipoAvatar = tipoAvatar;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public String getNomeAvatar() { return nomeAvatar; }
    public void setNomeAvatar(String nomeAvatar) { this.nomeAvatar = nomeAvatar; }
    public String getTipoAvatar() { return tipoAvatar; }
    public void setTipoAvatar(String tipoAvatar) { this.tipoAvatar = tipoAvatar; }
}
