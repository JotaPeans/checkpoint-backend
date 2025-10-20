package org.checkpoint.dominio.jogo;

import java.util.Objects;

public class JogoId {
    private final int id;

    public JogoId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof JogoId jogoId) {
            return id == jogoId.id;
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
