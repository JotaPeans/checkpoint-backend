package org.checkpoint.dominio.comentario;

import org.checkpoint.dominio.jogo.AvaliacaoId;
import org.checkpoint.dominio.lista.ListaId;
import org.checkpoint.dominio.user.UserId;

import java.util.List;

public interface ComentarioRepositorio {
    void saveComentario(Comentario comentario);

    void createComentario(UserId autorId, String conteudo, AvaliacaoId avaliacaoAlvoId, ListaId listaAlvoId, ComentarioId comentarioPaiId);

    Comentario getComentarioById(ComentarioId id);

    List<Comentario> listComentariosByPai(ComentarioId id);

    List<Comentario> listComentariosRaizByAvaliacaoAlvo(AvaliacaoId avaliacaoAlvoId);

    List<Comentario> listComentariosRaizByListaAlvo(ListaId listaAlvoId);

    void deleteComentario(ComentarioId comentarioId);
}
