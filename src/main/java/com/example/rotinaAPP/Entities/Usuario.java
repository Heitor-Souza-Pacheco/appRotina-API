package com.example.rotinaAPP.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private String fusoHorario;

    @Column(nullable = false)
    private LocalTime horarioReset;

    private LocalTime horarioNotificacao;

    private String fcmToken;

    @JsonIgnore
    @OneToMany(mappedBy = "usuario")
    private List<Habito> habitos;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getFusoHorario() {
        return fusoHorario;
    }

    public void setFusoHorario(String fusoHorario) {
        this.fusoHorario = fusoHorario;
    }

    public LocalTime getHorarioReset() {
        return horarioReset;
    }

    public void setHorarioReset(LocalTime horarioReset) {
        this.horarioReset = horarioReset;
    }

    public LocalTime getHorarioNotificacao() {
        return horarioNotificacao;
    }

    public void setHorarioNotificacao(LocalTime horarioNotificacao) {
        this.horarioNotificacao = horarioNotificacao;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
