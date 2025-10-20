package org.checkpoint.dominio.diario;

import java.util.Objects;

public class DiarioId {
    private final int id;

    public DiarioId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DiarioId diarioId) {
            return id == diarioId.id;
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
