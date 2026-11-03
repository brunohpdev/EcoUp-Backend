package br.com.ecoup.model;

import java.time.LocalDate;

public class UsuarioNivel {
    private int idUsuario;
    private int idNivel;
    private int nivel;
    private LocalDate dataNivel;

    public UsuarioNivel() { }
    public UsuarioNivel(int idUsuario, int idNivel, int nivel, LocalDate dataNivel) {
        this.idUsuario = idUsuario;
        this.idNivel = idNivel;
        this.nivel = nivel;
        this.dataNivel = dataNivel;
    }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public int getIdNivel() { return idNivel; }
    public void setIdNivel(int idNivel) { this.idNivel = idNivel; }
    public int getNivel() { return nivel; }
    public void setNivel(int nivel) { this.nivel = nivel; }
    public LocalDate getDataNivel() { return dataNivel; }
    public void setDataNivel(LocalDate dataNivel) { this.dataNivel = dataNivel; }
}
