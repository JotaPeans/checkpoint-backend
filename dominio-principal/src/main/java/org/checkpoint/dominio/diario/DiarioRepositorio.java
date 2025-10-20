package org.checkpoint.dominio.diario;

import org.checkpoint.dominio.jogo.JogoId;
import org.checkpoint.dominio.user.User;

import java.util.Date;

public interface DiarioRepositorio {
    void saveDiario(Diario diario);

    Diario getDiario(DiarioId id);

    Diario getDiarioByDono(User user);

    RegistroDiario createRegistroDiario(JogoId jogo, Date dataInicio, Date dataTermino);
    RegistroDiario getRegistroDiario(RegistroId registroId);
    void saveRegistroDiario(RegistroDiario registroDiario);

    Conquista createConquista(String nome, Date dataDesbloqueada, boolean isUnloked);
    Conquista getConquista(ConquistaId conquistaId);
    void saveConquista(Conquista conquista);
}
