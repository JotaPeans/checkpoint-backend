package org.checkpoint.infraestrutura.persistencia.memoria;

import org.checkpoint.dominio.email.Token;
import org.checkpoint.dominio.email.VerificacaoEmail;
import org.checkpoint.dominio.email.VerificacaoEmailRepositorio;
import org.checkpoint.dominio.jogo.*;
import org.checkpoint.dominio.user.*;
import org.checkpoint.dominio.diario.*;
import org.checkpoint.dominio.lista.*;
import org.checkpoint.dominio.comentario.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.junit.platform.commons.util.Preconditions.notNull;

public class Repositorio implements
        UserRepositorio,
        JogoRepositorio,
        DiarioRepositorio,
        ListaJogosRepositorio,
        VerificacaoEmailRepositorio,
        ComentarioRepositorio {

    /*-----------------------------------------------------------------------*/
    // USERS
    private final Map<UserId, User> usersById = new HashMap<>();
    private final Map<String, User> usersByEmail = new HashMap<>();
    private final AtomicInteger userIdSequence = new AtomicInteger(1);

    public void resetUsers() {
        usersById.clear();
        usersByEmail.clear();
    }

    @Override
    public void saveUser(User user) {
        usersById.put(user.getUserId(), user);
        usersByEmail.put(user.getEmail(), user);
    }

    @Override
    public User getUser(UserId id) {
        return usersById.get(id);
    }

    @Override
    public User getByEmail(String email) {
        return usersByEmail.get(email);
    }

    @Override
    public User createUser(String email, String senha, String nome) {

        int novoIdInt = userIdSequence.getAndIncrement();
        UserId novoId = new UserId(novoIdInt);

        User novoUsuario = new User(
                novoId,
                nome,
                email,
                senha,
                null,
                null,
                false,
                false,
                null,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        usersById.put(novoUsuario.getUserId(), novoUsuario);
        usersByEmail.put(email, novoUsuario);

        return novoUsuario;
    }


    /*-----------------------------------------------------------------------*/
// JOGOS
    private final Map<JogoId, Jogo> jogosById = new HashMap<>();
    private final Map<AvaliacaoId, Avaliacao> avaliacoesById = new HashMap<>();
    private final Map<TagId, Tag> tagsById = new HashMap<>();
    private final Map<String, Tag> tagsByName = new HashMap<>();
    private final Map<JogoId, RequisitosDeSistema> requisitosByJogo = new HashMap<>();

    private final AtomicInteger jogoIdSequence = new AtomicInteger(1);
    private final AtomicInteger avaliacaoIdSequence = new AtomicInteger(1);
    private final AtomicInteger tagIdSequence = new AtomicInteger(1);

    @Override
    public void saveJogo(Jogo jogo) {
        jogosById.put(jogo.getId(), jogo);
    }

    @Override
    public Jogo getJogo(JogoId id) {
        return jogosById.get(id);
    }

    @Override
    public List<Jogo> listJogos() {
        return new ArrayList<>(jogosById.values());
    }

    @Override
    public Avaliacao getAvaliacaoById(AvaliacaoId id) {
        return avaliacoesById.get(id);
    }

    @Override
    public void saveAvaliacao(Avaliacao avaliacao) {
        avaliacoesById.put(avaliacao.getId(), avaliacao);
    }

    @Override
    public Avaliacao createAvaliacao(UserId autorId, JogoId jogoId, Double nota, String comentario) {
        AvaliacaoId novoId = new AvaliacaoId(avaliacaoIdSequence.getAndIncrement());
        Date data = new Date();
        List<UserId> curtidas = new ArrayList<>();

        Avaliacao avaliacao = new Avaliacao(novoId, autorId, jogoId, nota, comentario, data, curtidas);
        saveAvaliacao(avaliacao);
        return avaliacao;
    }

    @Override
    public List<Avaliacao> getAvaliacoesByJogoId(JogoId jogoId) {
        List<Avaliacao> result = new ArrayList<>();
        for (Avaliacao a : avaliacoesById.values()) {
            if (a.getJogoId().equals(jogoId)) {
                result.add(a);
            }
        }
        result.sort(Comparator.comparing(Avaliacao::getData).reversed());
        return result;
    }

    @Override
    public Tag createTag(String nome) {
        TagId novoId = new TagId(tagIdSequence.getAndIncrement());
        Tag tag = new Tag(novoId, nome, new ArrayList<>());
        tagsById.put(novoId, tag);
        tagsByName.put(nome.toLowerCase(), tag);
        return tag;
    }

    @Override
    public Tag getTagByName(String nome) {
        return tagsByName.get(nome.toLowerCase());
    }

    @Override
    public Tag getTagById(TagId tagId) {
        return tagsById.get(tagId);
    }

    @Override
    public void saveTag(Tag tag) {
        tagsById.put(tag.getId(), tag);
        tagsByName.put(tag.getNome().toLowerCase(), tag);
    }

    @Override
    public RequisitosDeSistema getRequisitosDeSistemaByJogoId(JogoId jogoId) {
        return requisitosByJogo.get(jogoId);
    }

    /*-----------------------------------------------------------------------*/
    // DIARIO
    private final Map<DiarioId, Diario> diariosById = new HashMap<>();
    private final Map<UserId, Diario> diariosByUser = new HashMap<>();

    private final Map<RegistroId, RegistroDiario> registrosById = new HashMap<>();
    private final Map<ConquistaId, Conquista> conquistasById = new HashMap<>();

    private final AtomicInteger registroIdSequence = new AtomicInteger(1);
    private final AtomicInteger conquistaIdSequence = new AtomicInteger(1);


    @Override
    public void saveDiario(Diario diario) {
        notNull(diario, "O diário não pode ser nulo");
        notNull(diario.getId(), "O diário deve ter um ID válido");
        notNull(diario.getDonoId(), "O diário deve ter um dono válido");

        diariosById.put(diario.getId(), diario);
        diariosByUser.put(diario.getDonoId(), diario);
    }

    @Override
    public Diario getDiario(DiarioId id) {
        notNull(id, "O ID do diário não pode ser nulo");
        return diariosById.get(id);
    }

    @Override
    public Diario getDiarioByDono(User user) {
        notNull(user, "O usuário não pode ser nulo");
        notNull(user.getUserId(), "O ID do usuário não pode ser nulo");
        return diariosByUser.get(user.getUserId());
    }

    @Override
    public RegistroDiario createRegistroDiario(JogoId jogo, Date dataInicio, Date dataTermino) {
        notNull(jogo, "O id do jogo não pode ser nulo");
        notNull(dataInicio, "A data de início não pode ser nula");

        RegistroId novoId = new RegistroId(registroIdSequence.getAndIncrement());

        RegistroDiario registro = new RegistroDiario(
                novoId,
                jogo,
                dataInicio,
                dataTermino,
                new ArrayList<ConquistaId>()
        );

        registrosById.put(novoId, registro);
        return registro;
    }

    @Override
    public RegistroDiario getRegistroDiario(RegistroId registroId) {
        notNull(registroId, "O ID do registro não pode ser nulo");
        return registrosById.get(registroId);
    }

    @Override
    public void saveRegistroDiario(RegistroDiario registroDiario) {
        notNull(registroDiario, "O registro não pode ser nulo");
        notNull(registroDiario.getId(), "O ID do registro não pode ser nulo");

        registrosById.put(registroDiario.getId(), registroDiario);
    }

    @Override
    public Conquista createConquista(String nome, Date dataDesbloqueada, boolean isUnloked) {
        notNull(nome, "O nome da conquista não pode ser nulo");
        notNull(dataDesbloqueada, "A data de desbloqueio não pode ser nula");

        ConquistaId novoId = new ConquistaId(conquistaIdSequence.getAndIncrement());

        // Ajuste este construtor se sua classe Conquista tiver assinatura diferente
        Conquista conquista = new Conquista(
                novoId,
                nome,
                dataDesbloqueada,
                isUnloked
        );

        conquistasById.put(novoId, conquista);
        return conquista;
    }

    @Override
    public Conquista getConquista(ConquistaId conquistaId) {
        notNull(conquistaId, "O ID da conquista não pode ser nulo");
        return conquistasById.get(conquistaId);
    }

    @Override
    public void saveConquista(Conquista conquista) {
        notNull(conquista, "A conquista não pode ser nula");
        notNull(conquista.getId(), "O ID da conquista não pode ser nulo");

        conquistasById.put(conquista.getId(), conquista);
    }
    /*-----------------------------------------------------------------------*/
    // LISTAS DE JOGOS
    private final Map<ListaId, ListaJogos> listasById = new HashMap<>();

    @Override
    public void saveList(ListaJogos lista) {
        listasById.put(lista.getId(), lista);
    }

    public ListaJogos getList(ListaId id) {
        return listasById.get(id);
    }

    @Override
    public ListaJogos createList(UserId donoId, String titulo, boolean isPrivate) {
        notNull(donoId, "O id do dono não pode ser nulo");
        notNull(titulo, "O título da lista não pode ser nulo");

        ListaId novoId = new ListaId(listasById.size() + 1); // simples gerador incremental
        ListaJogos novaLista = new ListaJogos(
                novoId,
                donoId,
                titulo,
                isPrivate,
                new ArrayList<>(),
                new ArrayList<>()
        );

        listasById.put(novoId, novaLista);
        return novaLista;
    }

    public ListaJogos getListByTituloAndDono(String titulo, UserId donoId) {
        for (ListaJogos lista : listasById.values()) {
            if (lista.getTitulo().equalsIgnoreCase(titulo) && lista.getDonoId().equals(donoId)) {
                return lista;
            }
        }
        return null;
    }

    /*-----------------------------------------------------------------------*/

    // COMENTÁRIOS
    private final Map<ComentarioId, Comentario> comentariosById = new HashMap<>();
    private final Map<ComentarioId, List<Comentario>> comentariosByPai = new HashMap<>();
    private final Map<AvaliacaoId, List<Comentario>> comentariosRaizByAvaliacao = new HashMap<>();
    private final Map<ListaId, List<Comentario>> comentariosRaizByLista = new HashMap<>();

    private final AtomicInteger comentarioIdSequence = new AtomicInteger(1);

    private void indexarComentario(Comentario c) {
        ComentarioId paiId = c.getComentarioPaiId();
        if (paiId != null) {
            comentariosByPai.computeIfAbsent(paiId, k -> new ArrayList<>()).add(c);
        } else {
            if (c.getAvaliacaoAlvoId() != null) {
                comentariosRaizByAvaliacao
                        .computeIfAbsent(c.getAvaliacaoAlvoId(), k -> new ArrayList<>())
                        .add(c);
            }
            if (c.getListaAlvoId() != null) {
                comentariosRaizByLista
                        .computeIfAbsent(c.getListaAlvoId(), k -> new ArrayList<>())
                        .add(c);
            }
        }
    }

    private void desindexarComentario(Comentario c) {
        ComentarioId paiId = c.getComentarioPaiId();
        if (paiId != null) {
            List<Comentario> filhos = comentariosByPai.get(paiId);
            if (filhos != null) filhos.removeIf(x -> x.getId().equals(c.getId()));
            if (filhos != null && filhos.isEmpty()) comentariosByPai.remove(paiId);
        } else {
            if (c.getAvaliacaoAlvoId() != null) {
                List<Comentario> lst = comentariosRaizByAvaliacao.get(c.getAvaliacaoAlvoId());
                if (lst != null) lst.removeIf(x -> x.getId().equals(c.getId()));
                if (lst != null && lst.isEmpty()) comentariosRaizByAvaliacao.remove(c.getAvaliacaoAlvoId());
            }
            if (c.getListaAlvoId() != null) {
                List<Comentario> lst = comentariosRaizByLista.get(c.getListaAlvoId());
                if (lst != null) lst.removeIf(x -> x.getId().equals(c.getId()));
                if (lst != null && lst.isEmpty()) comentariosRaizByLista.remove(c.getListaAlvoId());
            }
        }
    }

    @Override
    public void saveComentario(Comentario comentario) {
        notNull(comentario, "O comentário não pode ser nulo");
        notNull(comentario.getId(), "O comentário deve ter um ID válido");

        Comentario antigo = comentariosById.get(comentario.getId());
        if (antigo != null) {
            desindexarComentario(antigo);
        }

        comentariosById.put(comentario.getId(), comentario);
        indexarComentario(comentario);
    }

    @Override
    public void createComentario(UserId autorId, String conteudo, AvaliacaoId avaliacaoAlvoId,
                                 ListaId listaAlvoId, ComentarioId comentarioPaiId) {
        notNull(autorId, "O autor não pode ser nulo");
        notNull(conteudo, "O conteúdo do comentário não pode ser nulo");

        boolean ehResposta = comentarioPaiId != null;
        boolean temAlgumAlvo = avaliacaoAlvoId != null || listaAlvoId != null;

        if (!ehResposta && !temAlgumAlvo) {
            throw new IllegalArgumentException("Comentário raiz precisa de um alvo (avaliação ou lista).");
        }

        ComentarioId novoId = new ComentarioId(comentarioIdSequence.getAndIncrement());
        Date agora = new Date();

        List<UserId> curtidas = new ArrayList<>();

        Comentario comentario = new Comentario(
                novoId,
                autorId,
                conteudo,
                agora,
                avaliacaoAlvoId,
                listaAlvoId,
                comentarioPaiId,
                curtidas
        );

        saveComentario(comentario);
    }


    @Override
    public Comentario getComentarioById(ComentarioId id) {
        notNull(id, "O ID do comentário não pode ser nulo");
        return comentariosById.get(id);
    }

    @Override
    public List<Comentario> listComentariosByPai(ComentarioId id) {
        notNull(id, "O ID do comentário pai não pode ser nulo");
        List<Comentario> filhos = comentariosByPai.getOrDefault(id, Collections.emptyList());
        return filhos.stream()
                .sorted(Comparator.comparing(Comentario::getData))
                .collect(Collectors.toList());
    }

    @Override
    public List<Comentario> listComentariosRaizByAvaliacaoAlvo(AvaliacaoId avaliacaoAlvoId) {
        notNull(avaliacaoAlvoId, "O ID da avaliação alvo não pode ser nulo");
        List<Comentario> raiz = comentariosRaizByAvaliacao.getOrDefault(avaliacaoAlvoId, Collections.emptyList());
        return raiz.stream()
                .sorted(Comparator.comparing(Comentario::getData))
                .collect(Collectors.toList());
    }

    @Override
    public List<Comentario> listComentariosRaizByListaAlvo(ListaId listaAlvoId) {
        notNull(listaAlvoId, "O ID da lista alvo não pode ser nulo");
        List<Comentario> raiz = comentariosRaizByLista.getOrDefault(listaAlvoId, Collections.emptyList());
        return raiz.stream()
                .sorted(Comparator.comparing(Comentario::getData))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteComentario(ComentarioId comentarioId) {
        notNull(comentarioId, "O ID do comentário não pode ser nulo");
        Comentario c = comentariosById.remove(comentarioId);
        if (c == null) return;

        desindexarComentario(c);

        List<Comentario> filhos = comentariosByPai.remove(comentarioId);
        if (filhos != null) {
            for (Comentario f : new ArrayList<>(filhos)) {
                deleteComentario(f.getId());
            }
        }
    }


    /*-----------------------------------------------------------------------*/

    // VERIFICAÇÃO DE EMAIL
    private final Map<Token, VerificacaoEmail> tokens = new HashMap<>();

    @Override
    public void save(VerificacaoEmail verificacaoEmail) {
        tokens.put(verificacaoEmail.getToken(), verificacaoEmail);
    }

    @Override
    public VerificacaoEmail getByToken(Token token) {
        return tokens.get(token);
    }

    @Override
    public void deleteToken(Token token) {
        tokens.remove(token);
    }


    @Override
    public VerificacaoEmail createToken(Token token, UserId id) {
        // expira em 1 hora
        Date expira = new Date(System.currentTimeMillis() + 60 * 60 * 1000);
        VerificacaoEmail v = new VerificacaoEmail(token, id, expira);
        tokens.put(token, v);
        return v;
    }

    @Override
    public VerificacaoEmail getByUserId(UserId id) {
        for (VerificacaoEmail verificacao : tokens.values()) {
            if (verificacao.getUserId().equals(id)) return verificacao;
        }
        return null;
    }

    /*-----------------------------------------------------------------------*/
}