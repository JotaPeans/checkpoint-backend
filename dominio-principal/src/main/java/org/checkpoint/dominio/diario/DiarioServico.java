package org.checkpoint.dominio.diario;

import org.checkpoint.dominio.jogo.JogoId;
import org.checkpoint.dominio.user.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.apache.commons.lang3.Validate.notNull;

public class DiarioServico {
    private final DiarioRepositorio diarioRepositorio;

    public DiarioServico(DiarioRepositorio diarioRepositorio) {
        notNull(diarioRepositorio, "O repositório de diários não pode ser nulo");

        this.diarioRepositorio = diarioRepositorio;
    }

    // =====================
    // Registros de jogos
    // =====================
    public void addRegistro(User user, JogoId jogoId, Date dataInicio, Date dataTermino) {
        notNull(user, "O usuario não pode ser nulo");
        notNull(jogoId, "O id do jogo não pode ser nulo");
        notNull(dataInicio, "A data de início não pode ser nula");

        if (dataTermino != null && dataTermino.before(dataInicio)) {
            throw new IllegalArgumentException("A data de término não pode ser anterior à de início");
        }

        Diario diario = this.diarioRepositorio.getDiario(user.getDiarioId());
        notNull(diario, "Diário não encontrado");

        RegistroDiario registroDiario = this.diarioRepositorio.createRegistroDiario(jogoId, dataInicio, dataTermino);
        List<RegistroId> registrosDiario = diario.getRegistros();

        registrosDiario.add(registroDiario.getId());
        diario.setRegistros(registrosDiario);

        this.diarioRepositorio.saveDiario(diario);
    }


    public void updateRegistro(RegistroId registroId, Date novaDataInicio, Date novaDataTermino) {
        notNull(registroId, "O id registro não pode ser nulo");
        notNull(novaDataInicio, "A nova data de início não pode ser nula");

        RegistroDiario registroDiario = this.diarioRepositorio.getRegistroDiario(registroId);

        notNull(registroDiario, "Registro não encontrado");

        registroDiario.setDataInicio(novaDataInicio);
        registroDiario.setDataTermino(novaDataTermino);

        this.diarioRepositorio.saveRegistroDiario(registroDiario);
    }

    public void removeRegistro(User user, RegistroId registroId) {
        notNull(user, "O usuario não pode ser nulo");
        notNull(registroId, "O id registro não pode ser nulo");

        Diario diario = this.diarioRepositorio.getDiario(user.getDiarioId());

        notNull(diario, "Diário não encontrado");

        RegistroDiario registroDiario = this.diarioRepositorio.getRegistroDiario(registroId);

        notNull(registroDiario, "Registro não encontrado");

        List<RegistroId> registrosDiario = diario.getRegistros();

        registrosDiario.remove(registroDiario.getId());
        diario.setRegistros(registrosDiario);

        this.diarioRepositorio.saveDiario(diario);
    }

    // =====================
    // Conquistas
    // =====================
    public void addConquista(RegistroId registroId, String nome, Date dataDesbloqueada, boolean concluida) {
        notNull(registroId, "O id do registro não pode ser nulo");
        notNull(nome, "O nome da conquista não pode ser nulo");
        notNull(dataDesbloqueada, "A data de desbloqueio não pode ser nula");

        RegistroDiario registroDiario = this.diarioRepositorio.getRegistroDiario(registroId);
        notNull(registroDiario, "Registro não encontrado");

        List<ConquistaId> conquistas = registroDiario.getConquistas();
        if (conquistas == null) {
            conquistas = new ArrayList<>();
        }

        for (ConquistaId cId : conquistas) {
            Conquista existente = this.diarioRepositorio.getConquista(cId);
            if (existente != null && existente.getNome() != null &&
                    existente.getNome().equalsIgnoreCase(nome)) {
                throw new IllegalArgumentException("A conquista já foi registrada");
            }
        }

        Conquista novaConquista = this.diarioRepositorio.createConquista(nome, dataDesbloqueada, concluida);

        conquistas.add(novaConquista.getId());
        registroDiario.setConquistas(conquistas);

        this.diarioRepositorio.saveRegistroDiario(registroDiario);
    }


    public void updateConquista(ConquistaId conquistaId, String novoNome, Date novaDataDesbloqueada, Boolean concluida) {
        notNull(conquistaId, "O id da conquista não pode ser nulo");

        Conquista conquista = this.diarioRepositorio.getConquista(conquistaId);

        notNull(conquista, "Conquista não encontrada");

        conquista.setNome(novoNome != null ? novoNome : conquista.getNome());
        conquista.setDataDesbloqueada(novaDataDesbloqueada != null ? novaDataDesbloqueada : conquista.getDataDesbloqueada());
        conquista.setConcluida(concluida != null ? concluida : conquista.getConcluida());

        this.diarioRepositorio.saveConquista(conquista);
    }

    public Double getPercentualConquistas(RegistroId registroId) {
        notNull(registroId, "O id do registro não pode ser nulo");

        RegistroDiario registroDiario = this.diarioRepositorio.getRegistroDiario(registroId);

        notNull(registroDiario, "Registro não encontrado");

        List<Conquista> conquistas = new ArrayList<Conquista>();

        for (ConquistaId conquistaId : registroDiario.getConquistas()) {
            Conquista conquista = this.diarioRepositorio.getConquista(conquistaId);
            conquistas.add(conquista);
        }

        if (conquistas.isEmpty()) {
            return 0.0;
        }

        long totalConcluidas = conquistas.stream()
                .filter(Conquista::getConcluida) // conta só as concluídas
                .count();

        return (totalConcluidas * 100.0) / conquistas.size();
    }
}
