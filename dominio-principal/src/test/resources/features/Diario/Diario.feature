Feature: Diario - registrar os jogos jogados, associando data de início/conclusão ou tempo de jogo

  Scenario: Datas válidas e calcular tempo
    Given que o usuário "Lari" inicia o registro do jogo "Portal 2"
    When ele informa a data de início "01/09/2025" e a data de término "10/09/2025"
    Then o sistema deve salvar o registro

  Scenario: Data de término anterior à data de início
    Given que o usuário "Lari" inicia o registro do jogo "Portal 2"
    When ele informa a data de início "10/09/2025" e a data de término "05/09/2025"
    Then o sistema notifica que A data de término não pode ser anterior à de início

  Scenario: Registrar apenas início do jogo
    Given que o usuário "Lari" inicia o registro do jogo "Portal 2"
    When ele informa apenas a data de início "01/09/2025"
    Then o sistema deve salvar o registro

  Scenario: Registrar apenas o término do jogo
    Given que o usuário "Lari" inicia o registro do jogo "Portal 2"
    When ele informa a data de término "05/09/2025"
    Then o sistema notifica que A data de início não pode ser nula

  Scenario: Registrar nova conquista
    Given que o usuário "Lari" acessa o diário
    When ele registra a conquista "Mascarado" com data "12/09/2025", status "concluída" e o jogo  "Hollow Knight "
    Then o sistema deve salvar a conquista vinculada ao jogo

  Scenario: Conquista sem nome
    Given que o usuário "Lari" acessa o diário
    When ele registra uma conquista com data "12/09/2025", status "concluída" e o jogo "Hollow Knight"
    Then o sistema notifica que O nome da conquista não pode ser nulo

  Scenario: Conquista sem data
    Given que o usuário "Lari" acessa o diário
    When ele registra a conquista "Mascarado", status "concluída" e o jogo "Hollow Knight"
    Then o sistema notifica que A data de desbloqueio não pode ser nula

  Scenario: Conquista duplicada
    Given que o usuário "Lari" já registrou a conquista "Mascarado" no jogo "Hollow Knight "
    When ele tenta registrar novamente a conquista "Mascarado" para o mesmo jogo
    Then o sistema notifica que A conquista já foi registrada

  Scenario: Calcular percentual de conquistas concluídas
    Given que o usuário "Lari" registrou 11 conquistas como "em andamento" no jogo "Hollow Knight"
    When o usuário registra 10 conquistas como "concluídas" no jogo
    Then o sistema deve calcular e mostrar que "47,62%" das conquistas foram concluídas
