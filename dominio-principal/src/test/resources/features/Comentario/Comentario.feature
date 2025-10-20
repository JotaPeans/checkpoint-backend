Feature: Comentario

    Scenario: Comentário com no mínimo 5 e no máximo 500 caracteres (avaliação)
      Given que existe uma avaliação de "Stardew Valley" feita por "Davi"
      When o usuário "Lari" escreve o comentário "Gostei muito do jogo"
      Then o sistema deve salvar o comentário como raiz vinculado à avaliação

    Scenario: Comentário com menos de 5 caracteres (avaliação)
      Given que existe uma avaliação de "Stardew Valley" feita por "Davi"
      When o usuário "Lari" escreve o comentário "Bleh"
      Then o sistema notifica que o comentário deve ter no mínimo 5 caracteres

    Scenario: Comentário com mais de 2000 caracteres (avaliação)
      Given que existe uma avaliação de "Stardew Valley" feita por "Davi"
      When o usuário "Lari" escreve o comentário "Concordo totalmente com todas as reviews sobre Stardew Valley. É impressionante como o jogo consegue transmitir uma sensação de paz e acolhimento em cada detalhe, seja no som relaxante da trilha, nas estações que mudam gradualmente ou nas interações simples com os moradores da vila. Eu também senti que, mesmo sendo um jogo de fazenda, ele vai muito além disso, oferecendo um senso de propósito e progressão que não pressiona o jogador. Essa liberdade de jogar no seu próprio ritmo é o que o torna tão especial."
      Then o sistema notifica que o comentário deve ter no máximo 500 caracteres

    Scenario: Editar próprio comentário (avaliação)
      Given que "Lari" tem um comentário na avaliação de "Davi" sobre "Stardew Valley"
      When "Lari" edita o comentário para "Gostei muito muito muito do jogo!"
      Then o sistema atualiza o comentário com a nova versão

    Scenario: Excluir comentário próprio (avaliação)
      Given que "Lari" tem um comentário na avaliação de "Davi" sobre "Stardew Valley"
      When "Lari" exclui o comentário
      Then o sistema remove o comentário da avaliação

    Scenario: Responder ao próprio comentário (avaliação)
      Given que "Lari" tem um comentário na avaliação de "Davi" sobre "Stardew Valley"
      When "Lari" responde ao próprio comentário com "Ainda concordo com minha opinião"
      Then o sistema exibe a resposta como filha do comentário original em estrutura encadeada

  Scenario: Comentário com no mínimo 5 e no máximo 500 caracteres (lista)
    Given que existe a lista "Favoritos do Davi" de propriedade de "Davi"
    When o usuário "Lari" escreve o comentário "Gostei muito da lista" na lista
    Then o sistema deve salvar o comentário como raiz vinculado à lista

  Scenario: Comentário com menos de 5 caracteres (lista)
    Given que existe a lista "Favoritos do Davi" de propriedade de "Davi"
    When o usuário "Lari" escreve o comentário "Bleh" na lista
    Then o sistema notifica que o comentário deve ter no mínimo 5 caracteres

  Scenario: Comentário com mais de 500 caracteres (lista)
    Given que existe a lista "Favoritos do Davi" de propriedade de "Davi"
    When o usuário "Lari" escreve o comentário "Concordo totalmente com todas as reviews sobre Stardew Valley. É impressionante como o jogo consegue transmitir uma sensação de paz e acolhimento em cada detalhe, seja no som relaxante da trilha, nas estações que mudam gradualmente ou nas interações simples com os moradores da vila. Eu também senti que, mesmo sendo um jogo de fazenda, ele vai muito além disso, oferecendo um senso de propósito e progressão que não pressiona o jogador. Essa liberdade de jogar no seu próprio ritmo é o que o torna tão especial." na lista
    Then o sistema notifica que o comentário deve ter no máximo 500 caracteres

  Scenario: Editar próprio comentário (lista)
    Given que "Lari" tem um comentário na lista "Favoritos do Davi"
    When "Lari" edita o comentário para "Gostei muito muito muito da lista!" na lista
    Then o sistema atualiza o comentário com a nova versão da lista

  Scenario: Excluir comentário próprio (lista)
    Given que "Lari" tem um comentário na lista "Favoritos do Davi"
    When "Lari" exclui o comentário na lista
    Then o sistema remove o comentário da lista

  Scenario: Responder ao próprio comentário (lista)
    Given que "Lari" tem um comentário na lista "Favoritos do Davi"
    When "Lari" responde ao próprio comentário com "Ainda concordo com minha opinião" na lista
    Then o sistema exibe a resposta como filha do comentário original em estrutura encadeada da lista
