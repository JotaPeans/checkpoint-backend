package org.checkpoint.dominio.steps.Jogo;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.checkpoint.dominio.CheckpointFuncionalidade;
import org.checkpoint.dominio.jogo.Avaliacao;
import org.checkpoint.dominio.jogo.AvaliacaoId;
import org.checkpoint.dominio.user.User;
import org.checkpoint.dominio.jogo.Jogo;
import org.checkpoint.dominio.jogo.JogoId;
import org.checkpoint.dominio.lista.ListaJogos;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class LikeSteps extends CheckpointFuncionalidade {

    private final String nomeLari = "Lari";
    private final String nomeDavi = "Davi";
    private final String senhaLari = "Larisenha2%";
    private final String senhaDavi = "Davisenha2%";
    private final String emailLari = "lss2@cesar.school";
    private final String emailDavi = "druy@cesar.school";

    User lari;
    User davi;
    Jogo jogo;
    ListaJogos lista;
    AvaliacaoId avaliacaoId;
    String notification;

    @Given("que a lista {string} do usuário {string} {string} foi curtida pelo usuário {string}")
    public void queAListaDoUsuárioFoiCurtidaPeloUsuário(String nomeLista, String nomeDono, String status, String nome) {
        userServico.registerUser(emailLari, senhaLari, nome);
        lari = repository.getByEmail(emailLari);

        userServico.registerUser(emailDavi, senhaDavi, nomeDono);
        davi = repository.getByEmail(emailDavi);

        listaServico.createLista(davi, nomeLista, false);

        if ("já".equalsIgnoreCase(status)) {
            listaServico.toggleListaLike(lari, davi.getListas().get(0));
        }
    }

    @When("o usuário {string} curte a lista")
    public void oUsuárioCurteALista(String nome) {
        listaServico.toggleListaLike(lari, davi.getListas().get(0));
    }

    @Then("o sistema deve registrar o like na lista")
    public void oSistemaDeveRegistrarOLikeNaLista() {
        lista = repository.getList(davi.getListas().get(0));
        assertNotNull(lista);

        assertTrue(lista.getCurtidas().contains(lari.getUserId()));
    }

    @When("o usuário {string} curte novamente a lista")
    public void oUsuárioCurteNovamenteALista(String arg0) {
        listaServico.toggleListaLike(lari, davi.getListas().get(0));
    }

    @Then("o sistema deve remover o like da lista")
    public void oSistemaDeveRemoverOLikeDaLista() {
        lista = repository.getList(davi.getListas().get(0));

        assertFalse(lista.getCurtidas().contains(lari.getUserId()));
    }

    @Given("que a lista {string} do usuário {string} tenha {int} curtidas")
    public void queAListaDoUsuárioTenhaCurtidas(String nomeLista, String nome, int qtd) {
        userServico.registerUser(emailLari, senhaLari, nomeLari);
        lari = repository.getByEmail(emailLari);

        userServico.registerUser(emailDavi, senhaDavi, nome);
        davi = repository.getByEmail(emailDavi);

        listaServico.createLista(davi, nomeLista, false);

        if (qtd == 1) {
            listaServico.toggleListaLike(lari, davi.getListas().get(0));
        }
    }

    @Then("a contagem de curtidas na lista deve ser atualizada para {int}")
    public void aContagemDeCurtidasNaListaDeveSerAtualizadaPara(int quantidadeEsperada) {
        lista = repository.getList(davi.getListas().get(0));
        assertNotNull(lista);

        int curtidasAtuais = lista.getCurtidas().size();

        assertEquals(quantidadeEsperada,curtidasAtuais);
    }

    @Given("que a review do usuário {string} do jogo {string} não foi curtida pelo usuário {string}")
    public void queAReviewNaoFoiCurtida(String nomeAutorReview, String nomeJogo, String nomeCurioso) {
        userServico.registerUser(emailDavi, senhaDavi, nomeAutorReview);
        davi = repository.getByEmail(emailDavi);

        userServico.registerUser(emailLari, senhaLari, nomeCurioso);
        lari = repository.getByEmail(emailLari);

        jogo = new Jogo(
                new JogoId(1),
                nomeJogo,
                "DevStudio",
                "https://example.com/" + nomeJogo.toLowerCase() + ".jpg",
                "Descrição curta de " + nomeJogo,
                "Uma descrição longa de " + nomeJogo,
                0,
                new ArrayList<>(), // capturas
                new ArrayList<>(), // curtidas
                new ArrayList<>(), // tags
                new ArrayList<>(), // requisitos
                new ArrayList<>()  // Avaliações
        );
        repository.saveJogo(jogo);

        jogoServico.submitAvaliacao(davi, jogo.getId(), 4.5, "Review do Daviiiiiiiiiiiiiiiiiiiiiiiiii");
        Avaliacao avaliacao = jogo.getAvaliacoes().get(0);
        avaliacaoId = avaliacao.getId();
    }

    @When("o usuário {string} curte a review")
    public void oUsuarioCurteAReview(String nomeUsuario) {
        jogoServico.toggleAvaliacaoLike(lari, avaliacaoId);
    }

    @Then("o sistema deve registrar o like na review")
    public void oSistemaDeveRegistrarOLikeNaReview() {
        Avaliacao atualizada = repository.getAvaliacaoById(avaliacaoId);
        assertTrue(atualizada.getCurtidas().contains(lari.getUserId()));
    }

    @Given("que a review do usuário {string} do jogo {string} já foi curtida pelo usuário {string}")
    public void queAReviewJaFoiCurtida(String nomeAutorReview, String nomeJogo, String nomeQueCurte) {
        userServico.registerUser(emailDavi, senhaDavi, nomeAutorReview);
        davi = repository.getByEmail(emailDavi);

        userServico.registerUser(emailLari, senhaLari, nomeQueCurte);
        lari = repository.getByEmail(emailLari);

        jogo = new Jogo(
                new JogoId(1),
                nomeJogo,
                "DevStudio",
                "https://example.com/" + nomeJogo.toLowerCase() + ".jpg",
                "Descrição curta de " + nomeJogo,
                "Uma descrição longa de " + nomeJogo,
                0,
                new java.util.ArrayList<>(),
                new java.util.ArrayList<>(),
                new java.util.ArrayList<>(),
                new java.util.ArrayList<>(),
                new ArrayList<>()
        );
        repository.saveJogo(jogo);

        jogoServico.submitAvaliacao(davi, jogo.getId(), 5.0, "Review do Daviiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
        var avaliacao = jogo.getAvaliacoes().get(0);
        avaliacaoId = avaliacao.getId();
        jogoServico.toggleAvaliacaoLike(lari, avaliacaoId);
    }

    @When("o usuário {string} curte novamente a review")
    public void oUsuarioCurteNovamenteAReview(String nomeUsuario) {
        jogoServico.toggleAvaliacaoLike(lari, avaliacaoId);
    }

    @Then("o sistema deve remover o like da review")
    public void oSistemaDeveRemoverOLikeDaReview() {
        var atualizada = repository.getAvaliacaoById(avaliacaoId);
        assertFalse(atualizada.getCurtidas().contains(lari.getUserId()));
    }

    @Given("que a review do usuário {string} do jogo {string} possui {int} curtidas")
    public void queAReviewDoUsuárioDoJogoPossuiCurtidas(String nomeAutorReview, String nomeJogo, int qtd) {
        userServico.registerUser(emailLari, senhaLari, nomeLari);
        lari = repository.getByEmail(emailLari);

        userServico.registerUser(emailDavi, senhaDavi, nomeAutorReview);
        davi = repository.getByEmail(emailDavi);

        jogo = new Jogo(
                new JogoId(1),
                nomeJogo,
                "DevStudio",
                "https://example.com/" + nomeJogo.toLowerCase() + ".jpg",
                "Descrição curta de " + nomeJogo,
                "Uma descrição longa e detalhada de " + nomeJogo,
                0,
                new ArrayList<>(), // capturas
                new ArrayList<>(), // curtidas
                new ArrayList<>(), // tags
                new ArrayList<>(), // requisitos
                new ArrayList<>()  // Avaliações
        );
        repository.saveJogo(jogo);

        jogoServico.submitAvaliacao(davi, jogo.getId(), 5.0, "Review do Daviiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
        Avaliacao avaliacao = jogo.getAvaliacoes().get(0);
        avaliacaoId = avaliacao.getId();

        if (qtd == 1) {
            jogoServico.toggleAvaliacaoLike(lari, avaliacaoId);
        }
    }

    @Then("a contagem de curtidas na review deve ser atualizada para {int}")
    public void aContagemDeCurtidasNaReviewDeveSerAtualizadaPara(int quantidadeEsperada) {
        Avaliacao avaliacao = repository.getAvaliacaoById(avaliacaoId);

        int curtidasAtuais = avaliacao.getCurtidas().size();
        assertEquals(quantidadeEsperada, curtidasAtuais);
    }
}