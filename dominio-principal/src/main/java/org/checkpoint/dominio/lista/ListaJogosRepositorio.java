package org.checkpoint.dominio.lista;

import org.checkpoint.dominio.user.UserId;

public interface ListaJogosRepositorio {
    void saveList(ListaJogos lista);

    ListaJogos createList(UserId donoId, String titulo, boolean isPrivate);

    ListaJogos getList(ListaId id);

    public ListaJogos getListByTituloAndDono(String titulo, UserId donoId);
}
