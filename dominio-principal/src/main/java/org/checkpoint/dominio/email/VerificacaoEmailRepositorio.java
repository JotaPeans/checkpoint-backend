package org.checkpoint.dominio.email;

import org.checkpoint.dominio.user.UserId;

public interface VerificacaoEmailRepositorio {
    void save(VerificacaoEmail verificacaoEmail);

    VerificacaoEmail getByToken(Token token);

    void deleteToken(Token token);

    VerificacaoEmail create(Token token, UserId id);

    VerificacaoEmail getByUserId(UserId id);
}
