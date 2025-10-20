package org.checkpoint.dominio.steps.Comentario;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.checkpoint.dominio.CheckpointFuncionalidade;
import org.checkpoint.dominio.comentario.Comentario;
import org.checkpoint.dominio.comentario.ComentarioId;
import org.checkpoint.dominio.jogo.Avaliacao;
import org.checkpoint.dominio.jogo.Jogo;
import org.checkpoint.dominio.jogo.JogoId;
import org.checkpoint.dominio.lista.ListaJogos;
import org.checkpoint.dominio.user.User;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ComentarioSteps extends CheckpointFuncionalidade {

    private final String nomeLari = "Lari";
    private final String nomeDavi = "Davi";
    private final String senhaLari = "Larisenha2%";
    private final String senhaDavi = "Davisenha2%";
    private final String emailLari = "lss2@cesar.school";
    private final String emailDavi = "druy@cesar.school";

    User lari;
    User davi;
    Jogo jogo;
    String notification;

    private ComentarioId comentarioo;
    private String novoConteudoDigitado;
    private ListaJogos lista;
    private String comentarioDigitado;

    @Given("que existe uma avaliação de {string} feita por {string}")
    public void queExisteUmaAvaliaçãoDeFeitaPor(String nomeJogo, String nome) {
        userServico.registerUser(emailDavi, senhaDavi, nome);
        davi = repository.getByEmail(emailDavi);

        JogoId jogoId = new JogoId(1);
        jogo = new Jogo(
                jogoId,
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

        jogoServico.submitAvaliacao(davi, jogo.getId(), 5.0, null);
    }

    @When("o usuário {string} escreve o comentário {string}")
    public void oUsuárioEscreveOComentário(String nome, String comentario) {
        userServico.registerUser(emailLari, senhaLari, nome);
        lari = repository.getByEmail(emailLari);

        try{
            comentarioServico.addComentarioAvaliacaoRaiz(lari, jogo.getAvaliacoes().get(0).getId(), comentario);
        } catch (IllegalArgumentException | NullPointerException e) {
            notification = e.getMessage();
        }
    }

    @Then("o sistema deve salvar o comentário como raiz vinculado à avaliação")
    public void oSistemaDeveSalvarOComentarioComoRaizVinculadoAAvaliacao() {
        Avaliacao avaliacao = jogo.getAvaliacoes().get(0);

        List<Comentario> comentariosRaiz = repository.listComentariosRaizByAvaliacaoAlvo(avaliacao.getId());

        Comentario comentario = comentariosRaiz.get(0);

        assertEquals(lari.getUserId(), comentario.getAutorId());
        assertEquals(avaliacao.getId(), comentario.getAvaliacaoAlvoId());
        assertNull(comentario.getComentarioPaiId());
        assertNull(comentario.getListaAlvoId());
    }

    @Then("o sistema notifica que o comentário deve ter no mínimo {int} caracteres")
    public void oSistemaNotificaQueOComentárioDeveTerNoMínimoCaracteres(int arg0) {
        assertEquals(notification, "O comentário deve ter no mínimo 5 caracteres");
    }

    @Then("o sistema notifica que o comentário deve ter no máximo {int} caracteres")
    public void oSistemaNotificaQueOComentárioDeveTerNoMáximoCaracteres(int arg0) {
        assertEquals(notification, "O comentário deve ter no máximo 500 caracteres");
    }

    @Given("que {string} tem um comentário na avaliação de {string} sobre {string}")
    public void queUsuarioTemComentarioNaAvaliacaoSobreJogo(String nomeAutorComentario, String nomeAutorAvaliacao, String nomeJogo) {
        userServico.registerUser(emailDavi, senhaDavi, nomeAutorAvaliacao);
        davi = repository.getByEmail(emailDavi);

        userServico.registerUser(emailLari, senhaLari, nomeAutorComentario);
        lari = repository.getByEmail(emailLari);

        JogoId jogoId = new JogoId(1);
        jogo = new Jogo(
                jogoId,
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

        jogoServico.submitAvaliacao(davi, jogo.getId(), 5.0, "Avaliação do Daviaaaaaaaaaaaaaaaaaa");

        String conteudoInicial = "Comentário inicial da Lari";
        comentarioServico.addComentarioAvaliacaoRaiz(lari, jogo.getAvaliacoes().get(0).getId(), conteudoInicial);

        List<Comentario> comentariosRaiz = repository.listComentariosRaizByAvaliacaoAlvo(jogo.getAvaliacoes().get(0).getId());
        Comentario primeiro = comentariosRaiz.get(0);
        comentarioo = primeiro.getId();
    }

    @When("{string} edita o comentário para {string}")
    public void usuarioEditaOComentarioPara(String nomeUsuario, String novoConteudo) {
        novoConteudoDigitado = novoConteudo;
        try {
            comentarioServico.editComentario(lari, comentarioo, novoConteudoDigitado);
        } catch (IllegalArgumentException | NullPointerException e) {
            notification = e.getMessage();
        }
    }

    @Then("o sistema atualiza o comentário com a nova versão")
    public void oSistemaAtualizaOComentarioComANovaVersao() {
        Comentario atualizado = repository.getComentarioById(comentarioo);
        assertEquals(novoConteudoDigitado, atualizado.getConteudo());
    }

    @When("{string} exclui o comentário")
    public void usuarioExcluiOComentario(String nomeUsuario) {
        try {
            comentarioServico.deleteComentario(lari, comentarioo);
        } catch (IllegalArgumentException | NullPointerException e) {
            notification = e.getMessage();
        }
    }

    @Then("o sistema remove o comentário da avaliação")
    public void oSistemaRemoveOComentarioDaAvaliacao() {
        var avaliacao = jogo.getAvaliacoes().get(0);
        List<Comentario> comentariosRestantes =
                repository.listComentariosRaizByAvaliacaoAlvo(avaliacao.getId());

        boolean existeAinda = comentariosRestantes.stream()
                .anyMatch(c -> c.getId().equals(comentarioo));

        assertFalse(existeAinda);
    }

    @When("{string} responde ao próprio comentário com {string}")
    public void usuarioRespondeAoProprioComentarioCom(String nomeUsuario, String resposta) {
        try {
            comentarioServico.replyComentario(lari, comentarioo, resposta);
        } catch (IllegalArgumentException | NullPointerException e) {
            notification = e.getMessage();
        }
    }

    @Then("o sistema exibe a resposta como filha do comentário original em estrutura encadeada")
    public void oSistemaExibeRespostaComoFilhaEmEstruturaEncadeada() {
        List<Comentario> filhos = repository.listComentariosByPai(comentarioo);
        Comentario resposta = filhos.stream()
                .filter(c -> c.getAutorId().equals(lari.getUserId()))
                .findFirst()
                .orElse(null);

        assertEquals(comentarioo, resposta.getComentarioPaiId());
        assertNull(resposta.getAvaliacaoAlvoId());
        assertNull(resposta.getListaAlvoId());
    }

    @Given("que existe a lista {string} de propriedade de {string}")
    public void queExisteAListaDePropriedadeDeQue(String tituloLista, String nomeDono) {
        userServico.registerUser(emailDavi, senhaDavi, nomeDono);
        davi = repository.getByEmail(emailDavi);

        userServico.registerUser(emailLari, senhaLari, nomeLari);
        lari = repository.getByEmail(emailLari);

        lista = repository.createList(davi.getUserId(), tituloLista, false);
    }

    @When("o usuário {string} escreve o comentário {string} na lista")
    public void oUsuarioEscreveOComentarioNaLista(String nomeUsuario, String comentario) {
        comentarioDigitado = comentario;

        try {
            comentarioServico.addComentarioListaRaiz(lari, lista.getId(), comentarioDigitado);
        } catch (IllegalArgumentException | NullPointerException e) {
            notification = e.getMessage();
        }
    }

    @Then("o sistema deve salvar o comentário como raiz vinculado à lista")
    public void oSistemaDeveSalvarOComentarioComoRaizVinculadoALista() {
        List<Comentario> comentariosRaiz = repository.listComentariosRaizByListaAlvo(lista.getId());

        Comentario salvo = comentariosRaiz.stream()
                .filter(c -> comentarioDigitado.equals(c.getConteudo()))
                .findFirst()
                .orElse(null);

        assertNotNull(salvo);
        assertEquals(lista.getId(), salvo.getListaAlvoId(), "Comentário deve estar vinculado à lista");
    }

    @Given("que {string} tem um comentário na lista {string}")
    public void queUsuarioTemUmComentarioNaLista(String nomeUsuario, String tituloLista) {
        userServico.registerUser(emailDavi, senhaDavi, nomeDavi);
        davi = repository.getByEmail(emailDavi);

        userServico.registerUser(emailLari, senhaLari, nomeUsuario);
        lari = repository.getByEmail(emailLari);

        lista = repository.createList(davi.getUserId(), tituloLista, false);
        assertNotNull(lista, "A lista deveria ter sido criada");

        String conteudoInicial = "Comentário inicial da Lari na lista";
        comentarioServico.addComentarioListaRaiz(lari, lista.getId(), conteudoInicial);

        List<Comentario> comentariosRaiz = repository.listComentariosRaizByListaAlvo(lista.getId());
        Comentario comentario = comentariosRaiz.get(0);
        comentarioo = comentario.getId();
    }
   
    @When("{string} edita o comentário para {string} na lista")
    public void usuarioEditaOComentarioParaNaLista(String nomeUsuario, String novoConteudo) {
        novoConteudoDigitado = novoConteudo;

        try {
            comentarioServico.editComentario(lari, comentarioo, novoConteudoDigitado);
        } catch (IllegalArgumentException | NullPointerException e) {
            notification = e.getMessage();
        }
    }

    @Then("o sistema atualiza o comentário com a nova versão da lista")
    public void oSistemaAtualizaOComentarioComNovaVersaoDaLista() {
        Comentario atualizado = repository.getComentarioById(comentarioo);

        assertEquals(novoConteudoDigitado, atualizado.getConteudo());
        assertEquals(lista.getId(), atualizado.getListaAlvoId());
    }

    @When("{string} exclui o comentário na lista")
    public void usuarioExcluiOComentarioNaLista(String nomeUsuario) {
        try {
            comentarioServico.deleteComentario(lari, comentarioo);
        } catch (IllegalArgumentException | NullPointerException e) {
            notification = e.getMessage();
        }
    }

    @Then("o sistema remove o comentário da lista")
    public void oSistemaRemoveOComentarioDaLista() {
        Comentario c = repository.getComentarioById(comentarioo);

        List<Comentario> comentariosRestantes =
                repository.listComentariosRaizByListaAlvo(lista.getId());

        boolean existeAinda = comentariosRestantes.stream()
                .anyMatch(com -> com.getId().equals(comentarioo));

        assertFalse(existeAinda, "Comentário excluído não deve mais aparecer na lista");
    }

    @When("{string} responde ao próprio comentário com {string} na lista")
    public void usuarioRespondeAoProprioComentarioComNaLista(String nomeUsuario, String resposta) {
        try {
            comentarioServico.replyComentario(lari, comentarioo, resposta);
        } catch (IllegalArgumentException | NullPointerException e) {
            notification = e.getMessage();
        }
    }

    @Then("o sistema exibe a resposta como filha do comentário original em estrutura encadeada da lista")
    public void oSistemaExibeARespostaComoFilhaDoComentarioOriginalEmEstruturaEncadeadaDaLista() {
        List<Comentario> respostas = repository.listComentariosByPai(comentarioo);

        Comentario resposta = respostas.stream()
                .filter(c -> c.getAutorId().equals(lari.getUserId()))
                .findFirst()
                .orElse(null);

        assertEquals(comentarioo, resposta.getComentarioPaiId());

        assertNull(resposta.getListaAlvoId());
    }
}
