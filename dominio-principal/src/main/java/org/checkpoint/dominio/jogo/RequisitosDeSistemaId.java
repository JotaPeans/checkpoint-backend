package org.checkpoint.dominio.jogo;

import java.util.Objects;

public class RequisitosDeSistemaId {
    private final int id;

    public RequisitosDeSistemaId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RequisitosDeSistemaId requisitosDeSistemaId) {
            return id == requisitosDeSistemaId.id;
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
