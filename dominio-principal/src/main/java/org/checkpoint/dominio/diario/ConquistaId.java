package org.checkpoint.dominio.diario;

import java.util.Objects;

public class ConquistaId {
    private final int id;

    public ConquistaId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ConquistaId conquistaId) {
            return id == conquistaId.id;
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
