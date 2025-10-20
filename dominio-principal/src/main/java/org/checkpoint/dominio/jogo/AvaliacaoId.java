package org.checkpoint.dominio.jogo;

import java.util.Objects;

public class AvaliacaoId {
    private final int id;

    public AvaliacaoId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AvaliacaoId reviewId) {
            return id == reviewId.id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return Integer.toString(id);
    }
}
