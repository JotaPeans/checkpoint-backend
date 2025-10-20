package org.checkpoint.dominio.jogo;

import org.checkpoint.dominio.user.UserId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang3.Validate.notNull;

public class Tag {
    private final TagId id;
    private String nome;
    private List<UserId> votos;

    public Tag(TagId id, String nome, List<UserId> votos) {
        notNull(id, "O id não pode ser nulo");
        notNull(nome, "O nome não pode ser nulo");
        notNull(votos, "A lista de votos não pode ser nula");

        this.id = id;

        setNome(nome);
        setVotos(votos);
    }

    public TagId getId() { return id; }

    public String getNome() { return nome; }

    public void setNome(String nome) { this.nome = nome; }

    public List<UserId> getVotos() { return votos; }

    public void setVotos(List<UserId> votos) { this.votos = votos; }

    public int getTotalVotos() {
        return votos == null ? 0 : votos.size();
    }

    public boolean addVoto(UserId userId) {
        if (votos == null) votos = new ArrayList<>();
        if (!votos.contains(userId)) {
            votos.add(userId);
            return true;
        }
        return false;
    }

    public boolean removeVoto(UserId userId) {
        if (votos == null) return false;
        return votos.remove(userId);
    }

    @Override
    public String toString() {
        return this.nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag)) return false;
        Tag tag = (Tag) o;
        return Objects.equals(id, tag.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
