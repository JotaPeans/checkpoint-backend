package org.checkpoint.dominio.steps.User;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.checkpoint.dominio.CheckpointFuncionalidade;
import org.checkpoint.dominio.jogo.Jogo;
import org.checkpoint.dominio.jogo.JogoId;
import org.checkpoint.dominio.user.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JogosFavoritosSteps extends CheckpointFuncionalidade {

    private String nomeLari = "Lari";
    private String senhaLari = "Larisenha2%";
    private String emailLari = "lss2@cesar.school";
    private String notification;

    private User lari;

    private List<JogoId> jogosAntigos;

    private JogoId jogo1Id;
    private JogoId jogo2Id;
    private JogoId jogo3Id;
    private JogoId jogo4Id;
    private JogoId jogo5Id;

    @Given("que o usuário {string} tenha os jogos {string} e {string} como favoritos")
    public void queOUsuárioTenhaOsJogosEComoFavoritos(String nome, String jogo1nome, String jogo2nome) {
        userServico.registerUser(emailLari, senhaLari, nomeLari);
        lari = repository.getByEmail(emailLari);

        jogo1Id = new JogoId(1);
        jogo2Id = new JogoId(2);

        Jogo jogo1 = new Jogo(
                jogo1Id,
                jogo1nome,
                "DevStudio",
                "https://example.com/" + jogo1nome.toLowerCase() + ".jpg",
                "Descrição curta de " + jogo1nome,
                "Uma descrição longa e detalhada de " + jogo1nome,
                0,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        Jogo jogo2 = new Jogo(
                jogo2Id,
                jogo2nome,
                "DevStudio",
                "https://example.com/" + jogo2nome.toLowerCase() + ".jpg",
                "Descrição curta de " + jogo2nome,
                "Uma descrição longa e detalhada de " + jogo2nome,
                0,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        repository.saveJogo(jogo1);
        repository.saveJogo(jogo2);

        userServico.addJogoFavorito(lari, jogo1);
        userServico.addJogoFavorito(lari, jogo2);

        jogosAntigos = new ArrayList<>(lari.getJogosFavoritos());
    }

    @When("ele favorita o jogo {string} e {string}")
    public void eleFavoritaOJogoE(String jogo3nome, String jogo4nome) {
        jogo3Id = new JogoId(3);
        jogo4Id = new JogoId(4);

        Jogo jogo3 = new Jogo(
                jogo3Id,
                jogo3nome,
                "DevStudio",
                "https://example.com/" + jogo3nome.toLowerCase() + ".jpg",
                "Descrição curta de " + jogo3nome,
                "Uma descrição longa e detalhada de " + jogo3nome,
                0,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        Jogo jogo4 = new Jogo(
                jogo4Id,
                jogo4nome,
                "DevStudio",
                "https://example.com/" + jogo4nome.toLowerCase() + ".jpg",
                "Descrição curta de " + jogo4nome,
                "Uma descrição longa e detalhada de " + jogo4nome,
                0,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        repository.saveJogo(jogo3);
        repository.saveJogo(jogo4);

        userServico.addJogoFavorito(lari, jogo3);
        userServico.addJogoFavorito(lari, jogo4);
    }

    @Then("o sistema deve deve atualizar os jogos favoritos no perfil")
    public void oSistemaDeveDeveAtualizarOsJogosFavoritosNoPerfil() {
        List<JogoId> atuais = lari.getJogosFavoritos();

        assertNotEquals(jogosAntigos, atuais);

        assertEquals(4, atuais.size());
    }

    @Given("que o usuário {string} tenha os jogos {string}, {string}, {string}, {string} como favoritos")
    public void queOUsuárioTenhaOsJogosComoFavoritos(String nome, String jogo1nome, String jogo2nome, String jogo3nome, String jogo4nome) {
        userServico.registerUser(emailLari, senhaLari, nomeLari);
        lari = repository.getByEmail(emailLari);

        jogo1Id = new JogoId(1);
        jogo2Id = new JogoId(2);
        jogo3Id = new JogoId(3);
        jogo4Id = new JogoId(4);

        Jogo jogo1 = new Jogo(
                jogo1Id,
                jogo1nome,
                "DevStudio",
                "https://example.com/" + jogo1nome.toLowerCase() + ".jpg",
                "Descrição curta de " + jogo1nome,
                "Uma descrição longa e detalhada de " + jogo1nome,
                0,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        Jogo jogo2 = new Jogo(
                jogo2Id,
                jogo2nome,
                "DevStudio",
                "https://example.com/" + jogo2nome.toLowerCase() + ".jpg",
                "Descrição curta de " + jogo2nome,
                "Uma descrição longa e detalhada de " + jogo2nome,
                0,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        Jogo jogo3 = new Jogo(
                jogo3Id,
                jogo3nome,
                "DevStudio",
                "https://example.com/" + jogo3nome.toLowerCase() + ".jpg",
                "Descrição curta de " + jogo3nome,
                "Uma descrição longa e detalhada de " + jogo3nome,
                0,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        Jogo jogo4 = new Jogo(
                jogo4Id,
                jogo4nome,
                "DevStudio",
                "https://example.com/" + jogo4nome.toLowerCase() + ".jpg",
                "Descrição curta de " + jogo4nome,
                "Uma descrição longa e detalhada de " + jogo4nome,
                0,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        repository.saveJogo(jogo1);
        repository.saveJogo(jogo2);
        repository.saveJogo(jogo3);
        repository.saveJogo(jogo4);

        userServico.addJogoFavorito(lari, jogo1);
        userServico.addJogoFavorito(lari, jogo2);
        userServico.addJogoFavorito(lari, jogo3);
        userServico.addJogoFavorito(lari, jogo4);
    }

    @When("ele tenta adicionar o jogo {string}")
    public void eleTentaAdicionarOJogo(String jogo5nome) {
        try {
            jogo5Id = new JogoId(5);

            Jogo jogo5 = new Jogo(
                    jogo5Id,
                    jogo5nome,
                    "DevStudio",
                    "https://example.com/" + jogo5nome.toLowerCase() + ".jpg",
                    "Descrição curta de " + jogo5nome,
                    "Uma descrição longa e detalhada de " + jogo5nome,
                    0,
                    new ArrayList<>(),
                    new ArrayList<>(),
                    new ArrayList<>(),
                    new ArrayList<>(),
                    new ArrayList<>()
            );

            repository.saveJogo(jogo5);

            userServico.addJogoFavorito(lari, jogo5);

        } catch (IllegalArgumentException | NullPointerException e) {
            notification = e.getMessage();
        }
    }

    @Then("o sistema deve notificar que O limite de jogos favoritos é {int}")
    public void oSistemaDeveNotificarQueOLimiteDeJogosFavoritosÉ(int arg0) {
        assertEquals(notification, "O limite de jogos favoritos é 4");
    }

    @When("ele coloca {string} para na primeira posição")
    public void eleColocaParaNaPrimeiraPosicao(String nomeJogo) {
        List<JogoId> favoritosAtuais = new ArrayList<>(lari.getJogosFavoritos());
        List<JogoId> novaOrdem = new ArrayList<>();

        JogoId jogoId = favoritosAtuais.stream()
                .filter(id -> repository.getJogo(id).getNome().equals(nomeJogo))
                .findFirst()
                .orElseThrow();

        novaOrdem.add(jogoId);
        favoritosAtuais.remove(jogoId);
        novaOrdem.addAll(favoritosAtuais);

        userServico.reorderJogoFavorito(lari, novaOrdem);
    }

    @Then("o sistema deve atualizar a ordem dos jogos favoritos")
    public void oSistemaDeveAtualizarAOrdemDosJogosFavoritos() {
        List<JogoId> favoritos = lari.getJogosFavoritos();
        Jogo primeiro = repository.getJogo(favoritos.get(0));
        assertEquals("Stardew Valley", primeiro.getNome());
    }

}