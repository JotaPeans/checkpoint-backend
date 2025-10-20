package org.checkpoint.dominio.user;

import org.checkpoint.dominio.email.EmailSenderService;
import org.checkpoint.dominio.email.Token;
import org.checkpoint.dominio.email.VerificacaoEmail;
import org.checkpoint.dominio.jogo.Jogo;
import org.checkpoint.dominio.jogo.JogoId;

import static org.apache.commons.lang3.Validate.notNull;
import static org.apache.commons.lang3.Validate.isTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

        return "token jwt";
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

    public void addJogoFavorito(User user, Jogo jogo) {
        notNull(user, "O usuário não pode ser nulo");
        notNull(jogo, "O jogo não pode ser nulo");

        List<JogoId> jogosFavoritos = user.getJogosFavoritos();

        if (jogosFavoritos == null) {
            jogosFavoritos = new ArrayList<>();
        }

        if (jogosFavoritos.contains(jogo.getId())) {
            throw new IllegalArgumentException("O jogo já está entre os favoritos");
        }

        if (jogosFavoritos.size() >= 4) {
            throw new IllegalArgumentException("O limite de jogos favoritos é 4");
        }


        jogosFavoritos.add(jogo.getId());

        user.setJogosFavoritos(jogosFavoritos);
        this.userRepositorio.saveUser(user);
    }


    public void removeJogoFavorito(User user, Jogo jogo) {
        notNull(user, "O usuário não pode ser nulo");
        notNull(jogo, "O jogo não pode ser nulo");

        List<JogoId> jogosFavoritos = user.getJogosFavoritos();
        jogosFavoritos.remove(jogo.getId());

        user.setJogosFavoritos(jogosFavoritos);
        this.userRepositorio.saveUser(user);
    }

    public void reorderJogoFavorito(User user, List<JogoId> novaOrdem) {
        notNull(user, "O usuário não pode ser nulo");
        notNull(novaOrdem, "A nova ordem não pode ser nula");

        List<JogoId> favoritosAtuais = user.getJogosFavoritos();

        if (favoritosAtuais == null || favoritosAtuais.isEmpty()) {
            throw new IllegalArgumentException("O usuário não possui jogos favoritos para reordenar");
        }

        if (favoritosAtuais.size() != novaOrdem.size()) {
            throw new IllegalArgumentException("A nova ordem deve conter todos os jogos favoritos atuais");
        }

        if (!favoritosAtuais.containsAll(novaOrdem) || !novaOrdem.containsAll(favoritosAtuais)) {
            throw new IllegalArgumentException("A nova ordem deve conter exatamente os mesmos jogos favoritos");
        }

        user.setJogosFavoritos(new ArrayList<>(novaOrdem));
        this.userRepositorio.saveUser(user);
    }

        public void updateProfile(User user, String nome, String bio, List<RedeSocial> redesSociais) {
        notNull(user, "O usuário não pode ser nulo");
        notNull(nome, "O nome não pode ser nulo");
        notNull(redesSociais, "A lista de redes sociais não pode ser nula");

        user.setNome(nome);
        user.setBio(bio);
        user.setRedesSociais(redesSociais);
        this.userRepositorio.saveUser(user);
    }

    public void updateAvatar(User user, String avatarUrl) {
        notNull(user, "O usuário não pode ser nulo");
        notNull(avatarUrl, "A URL do avatar não pode ser nula");

        user.setAvatarUrl(avatarUrl);
        this.userRepositorio.saveUser(user);
    }

    public void togglePrivacidade(User user, Boolean isPrivate) {
        notNull(user, "O usuário não pode ser nulo");
        notNull(isPrivate, "O parâmetro 'isPrivate' não pode ser nulo");

        user.setIsPrivate(isPrivate);
        this.userRepositorio.saveUser(user);
    }

    // =====================
    // Seguidores
    // =====================
    public void toggleSeguir(User seguidor, User userAlvo) {
        notNull(seguidor, "O seguidor não pode ser nulo");
        notNull(userAlvo, "O usuário alvo não pode ser nulo");

        if (userAlvo.getIsPrivate()) {
            // Usuário é privado -> cria solicitação pendente
            List<UserId> solicitacoesPendentes = userAlvo.getSolicitacoesPendentes();

            if (solicitacoesPendentes.contains(seguidor.getUserId()) == false) {
                solicitacoesPendentes.add(seguidor.getUserId());
                userAlvo.setSolicitacoesPendentes(solicitacoesPendentes);
            }
        } else {
            // Usuário é público -> segue diretamente
            List<UserId> userAlvoSeguidores = userAlvo.getSeguidores();
            List<UserId> userSeguidorSeguindo = userAlvo.getSeguindo();

            if (userAlvoSeguidores.contains(seguidor.getUserId()) == false) {
                userAlvoSeguidores.add(seguidor.getUserId());
                userAlvo.setSeguidores(userAlvoSeguidores);


                userSeguidorSeguindo.add(userAlvo.getUserId());
                seguidor.setSeguindo(userSeguidorSeguindo);
            } else {
                // Já segue-> desfaz
                userAlvoSeguidores.remove(seguidor.getUserId());
                userAlvo.setSeguidores(userAlvoSeguidores);

                seguidor.getSeguindo().remove(userAlvo.getUserId());
                seguidor.setSeguindo(userSeguidorSeguindo);
            }
        }

        userRepositorio.saveUser(seguidor);
        userRepositorio.saveUser(userAlvo);
    }

    public void approveSeguidor(User dono, User solicitante) {
        notNull(dono, "O dono não pode ser nulo");
        notNull(solicitante, "O solicitante não pode ser nulo");

        List<UserId> solicitacoesPendentes = dono.getSolicitacoesPendentes();

        boolean temSolicitacaoPendente = solicitacoesPendentes.contains(solicitante.getUserId());

        isTrue(temSolicitacaoPendente, "Nenhuma solicitação pendente encontrada.");

        solicitacoesPendentes.remove(solicitante.getUserId());
        dono.setSolicitacoesPendentes(solicitacoesPendentes);

        dono.getSeguidores().add(solicitante.getUserId());

        List<UserId> pessoasSeguindoSolicitante = solicitante.getSeguindo();
        pessoasSeguindoSolicitante.add(dono.getUserId());
        solicitante.setSeguindo(pessoasSeguindoSolicitante);

        userRepositorio.saveUser(dono);
        userRepositorio.saveUser(solicitante);
    }

    public void rejectSeguidor(User dono, User solicitante) {
        notNull(dono, "O dono não pode ser nulo");
        notNull(solicitante, "O solicitante não pode ser nulo");

        List<UserId> solicitacoesPendentes = dono.getSolicitacoesPendentes();

        boolean temSolicitacaoPendente = solicitacoesPendentes.contains(solicitante.getUserId());

        isTrue(temSolicitacaoPendente, "Nenhuma solicitação pendente encontrada.");

        solicitacoesPendentes.remove(solicitante.getUserId());

        userRepositorio.saveUser(dono);
    }

    public String getInformacoes(User solicitante, User solicitado) {
        notNull(solicitante, "O solicitante não pode ser nulo");
        notNull(solicitado, "O usuário solicitado não pode ser nulo");

        if (!solicitado.getIsPrivate()) {
            return montarInformacoes(solicitado);
        }

        if (solicitante.getUserId().equals(solicitado.getUserId())) {
            return montarInformacoes(solicitado);
        }

        if (solicitado.getSeguidores() != null && solicitado.getSeguidores().contains(solicitante.getUserId())) {
            return montarInformacoes(solicitado);
        }

        return "Você não é seguidor desta pessoa.";
    }

    private String montarInformacoes(User u) {
        StringBuilder sb = new StringBuilder();
        sb.append("Nome: ").append(u.getNome()).append("\n");

        if (u.getEmail() != null)
            sb.append("Email: ").append(u.getEmail()).append("\n");

        if (u.getBio() != null)
            sb.append("Bio: ").append(u.getBio()).append("\n");

        sb.append("Privado: ").append(u.getIsPrivate() ? "Sim" : "Não").append("\n");

        if (u.getDiarioId() != null)
            sb.append("Diário: ").append(u.getDiarioId().toString()).append("\n");

        if (u.getRedesSociais() != null && !u.getRedesSociais().isEmpty())
            sb.append("Redes Sociais: ").append(u.getRedesSociais()).append("\n");

        if (u.getSeguindo() != null)
            sb.append("Seguindo: ").append(u.getSeguindo().size()).append("\n");

        if (u.getSeguidores() != null)
            sb.append("Seguidores: ").append(u.getSeguidores().size()).append("\n");

        if (u.getListas() != null && !u.getListas().isEmpty())
            sb.append("Listas: ").append(u.getListas()).append("\n");

        if (u.getJogosFavoritos() != null && !u.getJogosFavoritos().isEmpty())
            sb.append("Jogos favoritos: ").append(u.getJogosFavoritos()).append("\n");

        return sb.toString();
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
