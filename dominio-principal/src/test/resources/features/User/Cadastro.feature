Feature: Cadastro de usuário – criar conta com e-mail/senha

  Scenario: E-mail não cadastrado
    Given que não existe um usuário com o e-mail "lss2@cesar.school" no sistema
    When o usuário termina o processo de cadastro
    Then o sistema notifica que O email de verificação com o token foi enviado para o email

  Scenario: E-mail já cadastrado
    Given que o e-mail "lss2@cesar.school" já esteja cadastrado no sistema
    When o usuário termina o processo de cadastro
    Then o sistema notifica que O e-mail já está cadastrado

  Scenario: Senha válida
    Given que não existe um usuário com o e-mail "lss2@cesar.school" no sistema
    When a senha "Senha123!" é fornecida pelo usuario
    Then o sistema notifica que O email de verificação com o token foi enviado para o email

  Scenario: Senha inválida
    Given que não existe um usuário com o e-mail "lss2@cesar.school" no sistema
    When a senha "aa" é fornecida pelo usuario
    Then o sistema notifica que A senha deve conter, no mínimo, pelo menos uma letra minúscula, pelo menos uma letra maiúscula, pelo menos um número, pelo menos um caracter especial, e pelo menos 8 digitos

  Scenario: E-mail válido
    When o email "lss2@cesar.school" é fornecido no cadastro
    Then o sistema notifica que O email de verificação com o token foi enviado para o email

  Scenario: E-mail inválido
    When o email "aaa@@" é fornecido no cadastro
    Then o sistema notifica que O e-mail está fora do padrão

  Scenario: Conta verificada
    Given que o usuário com email "lss2@cesar.school" forneceu o token de verificacao dentro de 1 hora depois do envio
    When o usuário tenta fazer login
    Then o sistema retorna o token de autenticação do usuário

  Scenario: Conta não verificada
    Given que o usuário com email "lss2@cesar.school" não forneceu o token de verificacao
    When o usuário tenta fazer login
    Then o sistema notifica que A conta não foi verificada

  Scenario: Token expirado
    Given que o usuário com email "lss2@cesar.school" fornece o token de verificacao depois de 1 hora
    Then o sistema notifica que O token está expirado

  Scenario: Cadastro sem email
    Given que o usuario tenta fazer o cadastro com a senha "senha123!" e nome "Lari"
    Then o sistema notifica que O email não pode ser nulo

  Scenario: Cadastro sem senha
    Given que o usuario tenta fazer o cadastro com o email "lss2@cesar.school" e nome "Lari"
    Then o sistema notifica que A senha não pode ser nula

  Scenario: Cadastro sem nome
    Given que o usuario tenta fazer o cadastro com o email "lss2@cesar.school" e senha "senha123!"
    Then o sistema notifica que O nome não pode ser nulo