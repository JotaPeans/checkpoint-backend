package org.checkpoint.dominio.email;

import org.checkpoint.dominio.user.UserId;

import java.util.Date;

public class VerificacaoEmail {
    private final Token token;
    private UserId userId;
    private Date dataExpiracao;

    public VerificacaoEmail(Token token, UserId userId, Date dataExpiracao) {
        this.token = token;

        setUserId(userId);
        setDataExpiracao(dataExpiracao);
    }

    public Token getToken() {
        return token;
    }

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    public Date getDataExpiracao() {
        return dataExpiracao;
    }

    public void setDataExpiracao(Date dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    } // utilizar essa função

    @Override
    public String toString() {
        return this.token.toString();
    }
}
