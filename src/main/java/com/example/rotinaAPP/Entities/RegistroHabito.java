package com.example.rotinaAPP.Entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
public class RegistroHabito {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private boolean concluido;

    @ManyToOne
    @JoinColumn(name = "id")
    private Habito habito;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public boolean isConcluido() {
        return concluido;
    }

    public void setConcluido(boolean concluido) {
        this.concluido = concluido;
    }

    public Habito getHabito() {
        return habito;
    }

    public void setHabito(Habito habito) {
        this.habito = habito;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RegistroHabito that = (RegistroHabito) o;
        return concluido == that.concluido && Objects.equals(id, that.id) && Objects.equals(data, that.data) && Objects.equals(habito, that.habito);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, data, concluido, habito);
    }
}
