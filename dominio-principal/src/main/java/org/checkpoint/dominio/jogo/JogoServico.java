package org.checkpoint.dominio.jogo;

import org.checkpoint.dominio.user.User;
import org.checkpoint.dominio.user.UserId;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.Validate.notNull;

public class JogoServico {
    private final JogoRepositorio jogoRepositorio;

    public JogoServico(JogoRepositorio jogoRepositorio) {
        notNull(jogoRepositorio, "O repositório de diários não pode ser nulo");

        this.jogoRepositorio = jogoRepositorio;
    }

    // =====================
    // Jogo
    // =====================
    public Jogo getJogo(JogoId jogoId) {
        notNull(jogoId, "O id do jogo não pode ser nulo");

        Jogo jogo = this.jogoRepositorio.getJogo(jogoId);

        notNull(jogo, "Jogo não encontrado");

        return jogo;
    }

    public List<Jogo> listJogos() {
        return this.jogoRepositorio.listJogos();
    }

    public RequisitosDeSistema getRequisitosDeSistema(JogoId jogoId) {
        notNull(jogoId, "O id do jogo não pode ser nulo");

        return this.jogoRepositorio.getRequisitosDeSistemaByJogoId(jogoId);
    }

    // =====================
    // Avaliações
    // =====================
    public void submitAvaliacao(User autor, JogoId jogoId, Double nota, String critica) {
        notNull(autor, "O autor não pode ser nulo");
        notNull(jogoId, "O id jogo não pode ser nulo");
        notNull(nota, "A nota não pode ser nula");

        Jogo jogo = this.jogoRepositorio.getJogo(jogoId);

        notNull(jogo, "Jogo não encontrado");

        if (nota < 0.5 || nota > 5.0) {
            throw new IllegalArgumentException("A nota tem que estar entre 0.5 e 5");
        }

        if (critica != null) {
            int tamanho = critica.trim().length();

            if (tamanho < 20) {
                throw new IllegalArgumentException("A crítica deve ter no mínimo 20 caracteres");
            }

            if (tamanho > 2000) {
                throw new IllegalArgumentException("A crítica deve ter no máximo 2000 caracteres");
            }
        }

        Avaliacao avaliacao = this.jogoRepositorio.createAvaliacao(autor.getUserId(), jogoId, nota, critica);

        List<Avaliacao> jogoAvaliacoes = jogo.getAvaliacoes();
        jogoAvaliacoes.add(avaliacao);

        jogo.setAvaliacoes(jogoAvaliacoes);

        if (jogoAvaliacoes.isEmpty() == false) {
            double soma = 0.0;
            for (Avaliacao a : jogoAvaliacoes) {
                soma += a.getNota();
            }
            double media = soma / jogoAvaliacoes.size();
            jogo.setNota(media);
        } else {
            jogo.setNota(0.0);
        }

        this.jogoRepositorio.saveJogo(jogo);
    }

    public void editAvaliacao(AvaliacaoId avaliacaoId, Double novaNota, String novaCritica) {
        notNull(avaliacaoId, "O id avaliação não pode ser nulo");
        notNull(novaNota, "A nova nota não pode ser nula");

        if (novaNota < 0.5 || novaNota > 5.0) {
            throw new IllegalArgumentException("A nota tem que estar entre 0.5 e 5");
        }

        if (novaCritica != null) {
            int tamanho = novaCritica.trim().length();

            if (tamanho < 20) {
                throw new IllegalArgumentException("A crítica deve ter no mínimo 20 caracteres");
            }

            if (tamanho > 2000) {
                throw new IllegalArgumentException("A crítica deve ter no máximo 2000 caracteres");
            }
        }

        Avaliacao avaliacao = this.jogoRepositorio.getAvaliacaoById(avaliacaoId);
        notNull(avaliacao, "Avaliação não encontrada");

        avaliacao.setComentario(novaCritica);
        avaliacao.setNota(novaNota);
        this.jogoRepositorio.saveAvaliacao(avaliacao);

        JogoId jogoId = avaliacao.getJogoId();
        Jogo jogo = this.jogoRepositorio.getJogo(jogoId);
        notNull(jogo, "Jogo não encontrado");

        List<Avaliacao> avaliacoes = this.jogoRepositorio.getAvaliacoesByJogoId(jogoId);
        jogo.setAvaliacoes(avaliacoes);

        if (avaliacoes != null && !avaliacoes.isEmpty()) {
            double soma = 0.0;
            for (Avaliacao a : avaliacoes) {
                soma += a.getNota();
            }
            double media = soma / avaliacoes.size();
            jogo.setNota(media);
        } else {
            jogo.setNota(0.0);
        }

        this.jogoRepositorio.saveJogo(jogo);
    }

    // =====================
    // Tags
    // =====================
    public void addTagsToGame(JogoId jogoId, UserId userId, List<String> tags) {
        notNull(jogoId, "O id do jogo não pode ser nulo");
        notNull(userId, "O id do usuário não pode ser nulo");
        notNull(tags, "A lista de tags não pode ser nula");

        if (tags.size() > 20) {
            throw new IllegalArgumentException("Não é permitido adicionar mais de 20 tags de uma vez.");
        }

        Jogo jogo = this.jogoRepositorio.getJogo(jogoId);
        notNull(jogo, "Jogo não encontrado");

        List<TagId> tagsDoJogo = new ArrayList<>(jogo.getTags());

        for (String nomeTag : tags) {
            if (nomeTag == null || nomeTag.trim().isEmpty()) continue;

            String nomeNormalizado = nomeTag.trim();

            Tag tag = this.jogoRepositorio.getTagByName(nomeNormalizado);
            if (tag == null) {
                tag = this.jogoRepositorio.createTag(nomeNormalizado);
            }

            boolean novoVoto = tag.addVoto(userId);
            if (novoVoto) {
                this.jogoRepositorio.saveTag(tag);
            }

            if (!tagsDoJogo.contains(tag.getId())) {
                tagsDoJogo.add(tag.getId());
            }
        }

        jogo.setTags(tagsDoJogo);
        this.jogoRepositorio.saveJogo(jogo);
    }

    public void removeTagFromGame(JogoId jogoId, String tagNome, UserId userId) {
        notNull(jogoId, "O id do jogo não pode ser nulo");
        notNull(tagNome, "O nome da tag não pode ser nulo");
        notNull(userId, "O id do usuário não pode ser nulo");

        Tag tag = this.jogoRepositorio.getTagByName(tagNome);
        notNull(tag, "Tag não encontrada");

        Jogo jogo = this.jogoRepositorio.getJogo(jogoId);
        notNull(jogo, "Jogo não encontrado");

        List<TagId> tagsDoJogo = new ArrayList<>(jogo.getTags());

        if (!tagsDoJogo.contains(tag.getId())) {
            throw new IllegalArgumentException("Essa tag não está associada ao jogo.");
        }

        boolean removeu = tag.removeVoto(userId);
        if (removeu) {
            this.jogoRepositorio.saveTag(tag);
        }

        if (tag.getTotalVotos() == 0) {
            tagsDoJogo.remove(tag.getId());
            jogo.setTags(tagsDoJogo);
            this.jogoRepositorio.saveJogo(jogo);
        }
    }

    public List<Tag> getTopTags(JogoId jogoId) {
        notNull(jogoId, "O id do jogo não pode ser nulo");

        Jogo jogo = this.jogoRepositorio.getJogo(jogoId);
        notNull(jogo, "Jogo não encontrado");

        List<Tag> jogoTags = new ArrayList<>();

        for (TagId tagId : jogo.getTags()) {
            Tag tag = this.jogoRepositorio.getTagById(tagId);
            jogoTags.add(tag);
        }

        int limiteTags = 5;

        return jogoTags.stream()
                .sorted(
                        Comparator.comparingInt(Tag::getTotalVotos).reversed()
                                .thenComparing(Tag::getNome, String.CASE_INSENSITIVE_ORDER)
                )
                .limit(5)
                .toList();
    }
}