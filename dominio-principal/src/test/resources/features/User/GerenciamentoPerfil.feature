Feature: Gerenciamento de perfil – editar informações

  Scenario: Mudança de avatar
    Given que o usuário "Lari" inicia a atualização do avatar enviando a url "imagemUrl/yugdjkgasdasjkds/uiasddas/"
    Then o sistema deve atualizar o avatar, substituindo a imagem anterior

  Scenario: Definir perfil como privado
    Given que o usuário "Lari" tem o perfil "privado"
    Then apenas seguidores aprovados podem visualizar as informações do perfil

  Scenario: Definir perfil como publico
    Given que o usuário "Lari" tem o perfil "publico"
    Then qualquer seguidor pode visualizar as informações do perfil

  Scenario: Solicitação de seguidor pendente
    Given que o usuário "Lari" tem o perfil "privado"
    When o usuário "JP" solicita para seguir o perfil
    Then a solicitação deve ficar pendente até aprovação ou rejeição manual

  Scenario: Aceitar solicitação de seguidor
    Given que existe uma solicitação pendente do usuário "JP" para "Lari"
    When Lari "aceita" a solicitação
    Then "JP" passa a ter acesso às informações do perfil

  Scenario: Rejeitar solicitação de seguidor
    Given que existe uma solicitação pendente do usuário "JP" para "Lari"
    When Lari "rejeita" a solicitação
    Then "JP" não terá acesso às informações do perfil

  Scenario: Mudança de bio
    Given Given que o usuário "Lari" inicia a atualização da bio para "Gamer desde 2000"
    Then a bio do usuário Lari deve ser "Gamer desde 2000"

  Scenario: Mudança de redes sociais
    Given Given que o usuário "Lari" inicia a atualização das redes sociais para "@lariss2" na plataforma "Twitter"
    Then as redes sociais do usuário Lari devem conter "@lariss2"