package org.checkpoint.dominio.diario;

import java.util.Date;

import static org.apache.commons.lang3.Validate.notNull;

public class Conquista {
    private final ConquistaId id;

    private String nome;
    private Date dataDesbloqueada;
    private boolean concluida;

    public Conquista(ConquistaId id, String nome, Date dataDesbloqueada, boolean concluida) {
        notNull(id, "O id não pode ser nulo");
        notNull(nome, "O nome não pode ser nulo");
        notNull(dataDesbloqueada, "A data de desbloqueio da conquista não pode ser nula");

        this.id = id;

        setNome(nome);
        setDataDesbloqueada(dataDesbloqueada);
        setConcluida(concluida);
    }

    public ConquistaId getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getDataDesbloqueada() {
        return dataDesbloqueada;
    }

    public void setDataDesbloqueada(Date dataDesbloqueada) {
        this.dataDesbloqueada = dataDesbloqueada;
    }

    public boolean getConcluida() {
        return concluida;
    }

    public void setConcluida(boolean concluida) {
        this.concluida = concluida;
    }

    @Override
    public String toString() {
        return this.nome;
    }
}
