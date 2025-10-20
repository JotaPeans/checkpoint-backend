Feature: Seguir usuários – acompanhar o que amigos, streamers ou críticos estão jogando e avaliando

  Scenario: Seguir usuário público
    Given que o usuário "Lari" não segue "Davi"
    And o perfil de Davi é "público"
    When "Lari" solicita seguir "Davi"
    Then o sistema registra imediatamente o follow

  Scenario: Seguir usuário privado
    Given que o usuário "Lari" não segue "Davi"
    And o perfil de Davi é "privado"
    When "Lari" solicita seguir "Davi"
    Then o sistema registra a solicitação como pendente

  Scenario: Dar unfollow
    Given que o usuário "Lari" já segue "Davi"
    When ele da unfollow
    Then o sistema atualiza os seguidores

  Scenario: Aprovar solicitação pendente
    Given que o usuário "Lari" enviou solicitação para seguir "Davi"
    And o perfil de Davi é "privado"
    When "Davi" aprova a solicitação
    Then o sistema adiciona "Lari" aos seguidores de "Davi"
    And remove a solicitação pendente

  Scenario: Rejeitar solicitação pendente
    Given que o usuário "Lari" enviou solicitação para seguir "Davi"
    And o perfil de Davi é "privado"
    When "Davi" rejeita a solicitação
    Then o sistema remove a solicitação pendente
    And "Lari" continua sem seguir "Davi"