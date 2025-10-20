package org.checkpoint.dominio.lista;

import java.util.Objects;

public class ListaId {
    private final int id;

    public ListaId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ListaId listaId) {
            return id == listaId.id;
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
