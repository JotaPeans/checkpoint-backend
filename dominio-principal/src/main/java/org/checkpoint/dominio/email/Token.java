package org.checkpoint.dominio.email;

import java.util.Objects;

public class Token {
    private final String token;

    public Token(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Token tokenData) {
            return token.equals(tokenData.token);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }

    @Override
    public String toString() {
        return token;
    }
}
