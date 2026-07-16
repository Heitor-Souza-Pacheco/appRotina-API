package com.example.rotinaAPP.Entities;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity
public class Habito {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private boolean ativo = true;

    @ManyToOne
    @JoinColumn(name = "id")
    private Usuario usuario;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Habito habito = (Habito) o;
        return ativo == habito.ativo && Objects.equals(id, habito.id) && Objects.equals(titulo, habito.titulo) && Objects.equals(descricao, habito.descricao) && Objects.equals(usuario, habito.usuario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titulo, descricao, ativo, usuario);
    }
}
