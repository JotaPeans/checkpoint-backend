package org.checkpoint.dominio.steps;

import org.checkpoint.dominio.jogo.Jogo;
import org.checkpoint.dominio.jogo.JogoId;
import org.checkpoint.infraestrutura.persistencia.memoria.Repositorio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

public class Common {

    public List<String> extrairLista(String s) {
        if (s == null) return List.of();

        s = s.trim();

        // Aceita formato com ou sem colchetes
        if (s.startsWith("[") && s.endsWith("]") && s.length() >= 2) {
            s = s.substring(1, s.length() - 1);
        }

        if (s.isEmpty()) return List.of();

        // Divide, remove espaços e duplicatas, preservando ordem
        return new ArrayList<>(Arrays.stream(s.split(","))
                .map(String::trim)
                .filter(x -> !x.isEmpty())
                .collect(Collectors.toCollection(LinkedHashSet::new)));
    }

    public List<Jogo> criarJogos(List<String> nomesJogos, Repositorio repository) {
        List<Jogo> jogosCriados = new ArrayList<>();

        int contador = 1;
        for (String nomeJogo : nomesJogos) {
            JogoId jogoId = new JogoId(contador++);

            Jogo jogo = new Jogo(
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
            jogosCriados.add(jogo);
        }

        return jogosCriados;
    }
}