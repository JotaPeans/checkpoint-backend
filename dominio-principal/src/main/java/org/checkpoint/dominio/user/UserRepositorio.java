package org.checkpoint.dominio.user;

public interface UserRepositorio {
    void saveUser(User user);

    User getUser(UserId id);

    User getByEmail(String email);

    User createUser(String email, String senha, String nome);
}
