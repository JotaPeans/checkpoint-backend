Feature: Jogos favoritos no perfil – destacar até 4 jogos preferidos na página do usuário.

  Scenario: Adicionando 2 jogos destacados
    Given que o usuário "Lari" tenha os jogos "Hollow Knight" e "Stardew Valley" como favoritos
    When ele favorita o jogo "Minecraft" e "Dark Souls"
    Then o sistema deve deve atualizar os jogos favoritos no perfil

  Scenario: Adicionando o 5 jogo favorito
    Given que o usuário "Lari" tenha os jogos "Hollow Knight", "Stardew Valley", "Minecraft", "Dark Souls" como favoritos
    When ele tenta adicionar o jogo "Celeste"
    Then o sistema deve notificar que O limite de jogos favoritos é 4

  Scenario: Reordenar jogos favoritos
    Given que o usuário "Lari" tenha os jogos "Hollow Knight", "Stardew Valley", "Minecraft", "Dark Souls" como favoritos
    When ele coloca "Stardew Valley" para na primeira posição
    Then o sistema deve atualizar a ordem dos jogos favoritos