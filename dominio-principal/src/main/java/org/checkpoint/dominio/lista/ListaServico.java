package org.checkpoint.dominio.lista;

import org.checkpoint.dominio.jogo.JogoId;
import org.checkpoint.dominio.user.User;
import org.checkpoint.dominio.user.UserId;
import org.checkpoint.dominio.user.UserRepositorio;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

public class ListaServico {
    private final ListaJogosRepositorio listaJogosRepositorio;
    private final UserRepositorio userRepositorio;

    public ListaServico(ListaJogosRepositorio listaJogosRepositorio, UserRepositorio userRepositorio) {
        notNull(listaJogosRepositorio, "O repositório de listas não pode ser nulo");
        notNull(userRepositorio, "O repositório de usuários não pode ser nulo");
        this.listaJogosRepositorio = listaJogosRepositorio;
        this.userRepositorio = userRepositorio;
    }

    // =====================
    // Criação e duplicação
    // =====================
    public void createLista(User dono, String titulo, boolean isPrivate) {
        notNull(dono, "O dono da lista não pode ser nulo");
        notNull(titulo, "O título da lista não pode ser nulo");

        ListaJogos criada = this.listaJogosRepositorio.createList(dono.getUserId(), titulo, isPrivate);

        List<ListaId> listas = dono.getListas();
        if (listas == null) listas = new ArrayList<>();
        listas.add(criada.getId());
        dono.setListas(listas);
        userRepositorio.saveUser(dono);
    }

    public void duplicateLista(User novoDono, ListaId listaOrigemId, UserId donoOrigem) {
        notNull(novoDono, "O dono da nova lista não pode ser nulo");
        notNull(listaOrigemId, "O id da lista de origem não pode ser nulo");
        notNull(donoOrigem, "O dono da lista de origem não pode ser nulo");

        ListaJogos origem = this.listaJogosRepositorio.getList(listaOrigemId);
        notNull(origem, "Lista de origem não encontrada");

        if (!origem.getDonoId().equals(donoOrigem)) {
            throw new IllegalArgumentException("A lista não pertence ao usuário de origem informado");
        }

        String novoTitulo = "CÓPIA " + origem.getTitulo();

        ListaJogos copia = this.listaJogosRepositorio.createList(
                novoDono.getUserId(),
                novoTitulo,
                origem.getIsPrivate()
        );

        copia.setJogos(new ArrayList<>(origem.getJogos()));
        this.listaJogosRepositorio.saveList(copia);

        List<ListaId> listasDoUsuario = novoDono.getListas();
        if (listasDoUsuario == null) listasDoUsuario = new ArrayList<>();
        listasDoUsuario.add(copia.getId());
        novoDono.setListas(listasDoUsuario);

        userRepositorio.saveUser(novoDono);
    }

    // =====================
    // Atualização
    // =====================
    public void updateTitulo(User user, ListaId listaId, String novoTitulo) {
        notNull(user, "O usuario não pode ser nulo");
        notNull(listaId, "O id da lista não pode ser nulo");
        notNull(novoTitulo, "O novo título não pode ser nulo");

        ListaJogos listaJogos = this.listaJogosRepositorio.getList(listaId);
        notNull(listaJogos, "Lista não encontrada");

        boolean isUserOwnerOfList = listaJogos.getDonoId().getId() == user.getUserId().getId();
        isTrue(isUserOwnerOfList, "O usuário não é dono da lista");

        listaJogos.setTitulo(novoTitulo);
        this.listaJogosRepositorio.saveList(listaJogos);
    }

    public void updateJogos(User user, ListaId listaId, List<JogoId> novosJogos) {
        notNull(user, "O usuário não pode ser nulo");
        notNull(listaId, "O id da lista não pode ser nulo");
        notNull(novosJogos, "A nova lista de jogos não pode ser nula");

        ListaJogos listaJogos = this.listaJogosRepositorio.getList(listaId);
        notNull(listaJogos, "Lista não encontrada");

        boolean isUserOwnerOfList = listaJogos.getDonoId().getId() == user.getUserId().getId();
        isTrue(isUserOwnerOfList, "O usuário não é dono da lista");

        List<JogoId> jogosLista = new ArrayList<>(listaJogos.getJogos());

        // valida limite (antes de adicionar)
        int totalJogos = jogosLista.size() + novosJogos.size();
        isTrue(totalJogos <= 100, "Uma lista não pode conter mais de 100 jogos");

        // adiciona apenas jogos que ainda não estão na lista
        for (JogoId jogoId : novosJogos) {
            if (!jogosLista.contains(jogoId)) {
                jogosLista.add(jogoId);
            }
        }

        listaJogos.setJogos(jogosLista);
        this.listaJogosRepositorio.saveList(listaJogos);
    }

    public void togglePrivacidade(User user, ListaId listaId, Boolean publica) {
        notNull(user, "O usuario não pode ser nulo");
        notNull(listaId, "O id da lista não pode ser nulo");
        notNull(publica, "O parâmetro 'publica' não pode ser nulo");

        ListaJogos listaJogos = this.listaJogosRepositorio.getList(listaId);
        notNull(listaJogos, "Lista não encontrada");

        listaJogos.setIsPrivate(!listaJogos.getIsPrivate());
        this.listaJogosRepositorio.saveList(listaJogos);
    }

    // =====================
    // Curtidas
    // =====================
    public void toggleListaLike(User user, ListaId listaId) {
        notNull(user, "O usuário não pode ser nulo");
        notNull(listaId, "O id da lista não pode ser nulo");

        ListaJogos listaJogos = this.listaJogosRepositorio.getList(listaId);
        notNull(listaJogos, "Lista não encontrada");

        List<UserId> curtidas = listaJogos.getCurtidas();
        if (curtidas.contains(user.getUserId())) {
            curtidas.remove(user.getUserId());
        } else {
            curtidas.add(user.getUserId());
        }

        listaJogos.setCurtidas(curtidas);
        this.listaJogosRepositorio.saveList(listaJogos);
    }
}