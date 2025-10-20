package org.checkpoint.dominio.user;

import java.util.Objects;

public class RedeSocial {
    private final String plataforma;
    private final String username;

    public RedeSocial(String plataforma, String username) {
        this.plataforma = plataforma;
        this.username = username;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RedeSocial redeSocial) {
            return Objects.equals(this.plataforma, redeSocial.plataforma) && Objects.equals(this.username, redeSocial.username);
        }
        return false;
    }

    public String getPlataforma() {
        return this.plataforma;
    }

    public String getUsername() {
        return this.username;
    }

    @Override
    public String toString() {
        return this.username;
    }
}
