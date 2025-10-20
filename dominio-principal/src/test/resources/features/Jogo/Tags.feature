Feature: Tags – marcar jogos com palavras-chave

    Scenario: Adicionando até 20 tags
        Given que o usuário "Lari" esteja na página do jogo "Dark Souls"
        When ele adiciona as tags "Dificuldade elevada, RPG de ação, Atmosfera sombria" ao jogo "Dark Souls"
        Then as tags desse jogo serão atualizadas

    Scenario: Adicionando mais de 20 tags
        Given que o usuário "Lari" esteja na página do jogo "Dark Souls"
        When ele adiciona as tags "RPG de ação, Alta dificuldade, Atmosfera sombria, Dark Fantasy, Mundo interconectado, Chefes desafiadores, História enigmática, Exploração, Progressão de personagem, Combate estratégico, Rolagem de esquiva, Armas variadas, Magia, Construções de build, Sistema de estamina, Morte constante, Aprendizado por tentativa e erro, Design de nível complexo, Narrativa ambiental, Jogo cult, Experiência imersiva" ao jogo "Dark Souls"
        Then o sistema notifica que Não é permitido adicionar mais de 20 tags de uma vez.

    Scenario: Tags duplicadas
        Given que o usuário "Lari" tenha adicionado a tag "RPG de ação" ao jogo "Dark Souls"
        When ele adiciona as tags "Dificuldade elevada, RPG de ação" ao jogo "Dark Souls"
        Then o sistema ignora a duplicação e atualiza as tags

    Scenario: Remover tag de um jogo
        Given que o usuário "Lari" esteja na página do jogo "Dark Souls"
        And que o jogo "Dark Souls" possua as tags "RPG de ação, Dificuldade elevada"
        When ele remove a tag "Dificuldade elevada" do jogo "Dark Souls"
        Then a tag "Dificuldade elevada" não deve mais estar associada ao jogo

    Scenario: Tentar remover uma tag inexistente
        Given que o usuário "Lari" esteja na página do jogo "Dark Souls"
        And que o jogo "Dark Souls" possua as tags "RPG de ação"
        When ele remove a tag "Atmosfera sombria" do jogo "Dark Souls"
        Then o sistema notifica que Tag não encontrada

    Scenario: Visualizar as 5 tags mais utilizadas
        Given que o usuário "Lari" esteja na página do jogo "Dark Souls"
        Then as 5 tags mais utilizadas vão ser exibidas na seção de tags

    Scenario: Tags empatadas
        Given que as tags "RPG, Ação" tenham a mesma contagem de no jogo "Dark Souls"
        Then as tags empatadas são exibidas em ordem alfabética

    Scenario: Recalcular tags populares após adição
        Given que o jogo "Dark Souls" possui as tags "RPG de ação, Dificuldade elevada, Atmosfera sombria, Exploração e Chefes desafiadores"
        When o usuário "Lari" adiciona a tag "Dificuldade elevada" ao jogo
        Then o sistema recalcula e exibe automaticamente as 5 tags mais populares

    Scenario: Recalcular tags populares após remoção
        Given que o jogo "Dark Souls" possui as tags "RPG de ação, Dificuldade elevada, Atmosfera sombria, Exploração e Chefes desafiadores"
        When o usuário "JP" remove a tag "Dificuldade elevada" do jogo
        Then o sistema recalcula e exibe automaticamente as 5 tags mais populares