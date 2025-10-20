package org.checkpoint.dominio.user;

import org.checkpoint.dominio.email.EmailSenderService;
import org.checkpoint.dominio.email.Token;
import org.checkpoint.dominio.email.VerificacaoEmail;

import static org.apache.commons.lang3.Validate.notNull;
import static org.apache.commons.lang3.Validate.isTrue;

import java.util.Date;
import java.util.regex.Pattern;

public class UserServico {
    private final UserRepositorio userRepositorio;
    private final EmailSenderService emailSenderService;

    public UserServico(UserRepositorio userRepositorio, EmailSenderService emailSenderService) {
        notNull(userRepositorio, "O repositório de usuários não pode ser nulo");
        notNull(emailSenderService, "O servico de email não pode ser nulo");

        this.userRepositorio = userRepositorio;
        this.emailSenderService = emailSenderService;
    }

    public String login(String email, String senha) {
        notNull(email, "O email não pode ser nulo");
        notNull(senha, "A senha não pode ser nula");

        User user = this.userRepositorio.getByEmail(email);

        isTrue(user != null && user.getSenha().equals(senha), "E-mail ou senha inválidos");

        isTrue(user.isEmailVerificado(), "A conta não foi verificada");

        return "O usuario logou no sistema";
    }

    public String registerUser(String email, String senha, String nome) {
        notNull(email, "O email não pode ser nulo");
        notNull(senha, "A senha não pode ser nula");
        notNull(nome, "O nome não pode ser nulo");

        // lógica de registro
        String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

        boolean emailValido = EMAIL_PATTERN.matcher(email).matches();
        isTrue(emailValido, "O e-mail está fora do padrão");

        String PASSWORD_REGEX =
                "^(?=.*[a-z])" +        // pelo menos uma letra minúscula
                        "(?=.*[A-Z])" +        // pelo menos uma letra maiúscula
                        "(?=.*\\d)" +          // pelo menos um número
                        "(?=.*[@$!%*?&^#()\\[\\]{}._+\\-=<>])" + // pelo menos um caractere especial
                        ".{8,}$";              // pelo menos 8 caracteres

        Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);
        boolean senhaValida = PASSWORD_PATTERN.matcher(senha).matches();
        isTrue(
                senhaValida,
                "A senha deve conter, no mínimo, " +
                        "pelo menos uma letra minúscula, pelo menos uma letra maiúscula, " +
                        "pelo menos um número, pelo menos um caracter especial, e pelo menos 8 digitos"
        );

        boolean isEmailAlreadyInUse = this.isEmailAlreadyInUse(email);
        isTrue(!isEmailAlreadyInUse, "O e-mail já está cadastrado");

        User user = userRepositorio.createUser(email, senha, nome);

        String token = this.emailSenderService.generateVerificationToken(email, user.getUserId());
        this.emailSenderService.sendVerificationEmail(email, token);

        return "O email de verificação com o token foi enviado para o email";
    }

    public void verifyUserEmail(Token token) {
        notNull(token, "O token não pode ser nulo");

        VerificacaoEmail verificacaoEmail = this.emailSenderService.getVerificacaoEmailByToken(token);

        notNull(verificacaoEmail, "Verificação de email não encontrada");

        Date agora = new Date();
        isTrue(verificacaoEmail.getDataExpiracao().after(agora), "O token está expirado");

        User user = this.userRepositorio.getUser(verificacaoEmail.getUserId());

        notNull(user, "Usuário para verificação de email não encontrado");

        user.setEmailVerificado(true);

        this.userRepositorio.saveUser(user);
    }

    // =====================
    // Validações
    // =====================
    public Boolean isEmailAlreadyInUse(String email) {
        notNull(email, "O email não pode ser nulo");

        User user = this.userRepositorio.getByEmail(email);
        return user != null;
    }
}
