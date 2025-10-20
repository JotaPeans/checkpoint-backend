package org.checkpoint.dominio.email;

import org.checkpoint.dominio.user.UserId;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.apache.commons.lang3.Validate.notNull;

public class EmailServico implements EmailSenderService {
    private final VerificacaoEmailRepositorio verificacaoEmailRepositorio;

    public EmailServico(VerificacaoEmailRepositorio verificacaoEmailRepositorio) {
        notNull(verificacaoEmailRepositorio, "O repositório de verificacao de email não pode ser nulo");

        this.verificacaoEmailRepositorio = verificacaoEmailRepositorio;
    }

    public void sendEmail(String recipient, String subject, String body) {
        System.out.println("Email enviado");
    }

    public String generateVerificationToken(String email, UserId userId) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(email.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            Token token = new Token(hexString.toString());
            this.verificacaoEmailRepositorio.createToken(token, userId);

            return token.getToken();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao gerar hash: SHA-256", e);
        }
    }

    public void sendVerificationEmail(String email, String token) {
        System.out.println("Email de verificacao enviado");
    }

    public VerificacaoEmail getVerificacaoEmailByToken (Token token) {
        return this.verificacaoEmailRepositorio.getByToken(token);
    }

}
