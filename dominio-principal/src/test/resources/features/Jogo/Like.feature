Feature: Likes – marcar jogos, reviews ou listas como curtidos para mostrar apreço

  Scenario: Curtir review
    Given que a review do usuário "Davi" do jogo "Stardew Valley" não foi curtida pelo usuário "Lari"
    When o usuário "Lari" curte a review
    Then o sistema deve registrar o like na review

  Scenario: Descurtir review
    Given que a review do usuário "Davi" do jogo "Stardew Valley" já foi curtida pelo usuário "Lari"
    When o usuário "Lari" curte novamente a review
    Then o sistema deve remover o like da review

  Scenario: Aumentar total de curtidas da review
    Given  que a review do usuário "Davi" do jogo "Stardew Valley" possui 0 curtidas
    When o usuário "Lari" curte a review
    Then a contagem de curtidas na review deve ser atualizada para 1

  Scenario: Diminuir total de curtidas da review
    Given que a review do usuário "Davi" do jogo "Stardew Valley" possui 1 curtidas
    When o usuário "Lari" curte novamente a review
    Then a contagem de curtidas na review deve ser atualizada para 0

  Scenario: Curtir lista
    Given que a lista "Jogos que recomendo" do usuário "Davi" "não" foi curtida pelo usuário "Lari"
    When o usuário "Lari" curte a lista
    Then o sistema deve registrar o like na lista

  Scenario: Descurtir lista
    Given que a lista "Jogos que recomendo" do usuário "Davi" "já" foi curtida pelo usuário "Lari"
    When o usuário "Lari" curte novamente a lista
    Then o sistema deve remover o like da lista

  Scenario: Aumentar total de curtidas da lista
    Given que a lista "Jogos que recomendo" do usuário "Davi" tenha 0 curtidas
    When o usuário "Lari" curte a lista
    Then a contagem de curtidas na lista deve ser atualizada para 1

  Scenario: Diminuir total de curtidas da lista
    Given que a lista "Jogos que recomendo" do usuário "Davi" tenha 1 curtidas
    When o usuário "Lari" curte novamente a lista
    Then a contagem de curtidas na lista deve ser atualizada para 0