package org.checkpoint.dominio.diario;

import java.util.Objects;

public class RegistroId {
    private final int id;

    public RegistroId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RegistroId registroId) {
            return id == registroId.id;
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
