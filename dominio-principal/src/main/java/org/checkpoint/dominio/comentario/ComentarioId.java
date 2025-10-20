package org.checkpoint.dominio.comentario;

import java.util.Objects;

public class ComentarioId {
    private final int id;

    public ComentarioId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ComentarioId comentarioId) {
            return id == comentarioId.id;
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
