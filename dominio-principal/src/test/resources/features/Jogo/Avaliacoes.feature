Feature: Avaliações – atribuir nota (0,5 a 5 estrelas) e escrever uma crítica/opinião sobre o jogo.

  Scenario: Avaliação sem texto com nota válida
    Given que o usuário "Lari" esta na página do jogo "Minecraft"
    When ele atribui a nota 4.0
    Then a média geral do jogo deve ser atualizada

  Scenario: Avaliação sem texto com nota inválida
    Given que o usuário "Lari" esta na página do jogo "Minecraft"
    When ele atribui a nota 6.0
    Then o sistema notifica que A nota tem que estar entre 0.5 e 5

  Scenario: Avaliação com caracteres entre 20 e 2000 e nota atribuída
    Given que o usuário "Lari" esta na página do jogo "Minecraft"
    When ele atribui a nota 4.0 e a crítica "Minecraft é um jogo muito bom, super recomendo jogar!"
    Then a média geral do jogo deve ser atualizada
    And o comentário enviado

  Scenario: Avaliação com menos de 20 caracteres
    Given que o usuário "Lari" esta na página do jogo "Minecraft"
    When ele atribui a nota 4.0 e a crítica "Jogo massa"
    Then o sistema notifica que A crítica deve ter no mínimo 20 caracteres

  Scenario: Avaliação com mais de 2000 caracteres
    Given que o usuário "Lari" esta na página do jogo "Minecraft"
    When ele atribui a nota 4.0 e a crítica "O coração de Minecraft é a liberdade. No modo sobrevivência, você nasce em um mundo aleatório e precisa lidar com recursos escassos, fome, inimigos e exploração. O loop é simples, mas viciante: explorar → coletar → construir → sobreviver → repetir. A sensação de cavar fundo até encontrar diamantes, ou a tensão de ouvir o som de um Creeper atrás de você, são experiências únicas que todo jogador de Minecraft já viveu. Por outro lado, o modo criativo elimina restrições e abre as portas para a pura imaginação. Aqui, você pode construir desde uma casinha simples até réplicas em escala real da Torre Eiffel, da Estrela da Morte ou até mesmo recriar cidades inteiras. Muitos jogadores passam anos jogando apenas no criativo, sem nunca enfrentar um zumbi, porque o prazer está em dar vida a ideias. Além disso, há o vasto universo de servidores online, minigames e mods que transformam o jogo em algo totalmente novo. Em servidores, você pode participar de corridas, batalhas de construção, sobrevivência em equipe, ou até simulações de RPG. Cada servidor é praticamente um jogo novo, feito dentro do mesmo motor. Um dos pontos que mantém o jogador preso é a progressão natural. Você começa com nada, corta uma árvore, faz ferramentas de madeira, depois pedra, ferro, até chegar ao diamante e finalmente ao Nether e ao End. Esse arco de evolução cria um senso de conquista pessoal. Cada pequena vitória — encontrar carvão, domar um cavalo, derrotar o dragão do Ender — é marcante. E o mais interessante: não existe um caminho correto. Você pode simplesmente ignorar a luta contra o dragão e decidir viver como fazendeiro, explorador ou arquiteto. Jogar Minecraft é como voltar à infância, quando brincar era inventar histórias com blocos de montar ou imaginar aventuras em quintais e terrenos baldios. Ele desperta algo que poucos jogos conseguem: a sensação de que você pode ser qualquer coisa e criar qualquer mundo. E mesmo depois de anos, sempre que abro o jogo e ouço aquele piano suave da trilha, sinto a mesma empolgação de explorar algo novo. Minecraft não é apenas um jogo. É uma plataforma criativa, um espaço social e uma peça cultural que atravessa gerações. Ele pode ser um passatempo casual, uma ferramenta educacional ou até uma forma de arte. Seus gráficos simples escondem uma profundidade gigantesca, e sua longevidade prova que criatividade supera qualquer tecnologia passageira. Com seus pontos fortes muito acima dos fracos, Minecraft merece ser reconhecido como um dos jogos mais importantes da história. Não apenas pelo que oferece como jogabilidade, mas pelo impacto duradouro que teve na forma como entendemos o poder dos videogames."
    Then o sistema notifica que A crítica deve ter no máximo 2000 caracteres


  Scenario: Avaliação com caracteres entre 20 e 2000 e sem nota atribuída
    Given que o usuário "Lari" esta na página do jogo "Minecraft"
    When ele atribui a crítica "Minecraft é um jogo muito bom, super recomendo jogar!"
    Then o sistema notifica que A nota não pode ser nula

  Scenario: Editar avaliação
    Given que o usuário "Lari" inicia o processo de edição da avaliação no jogo "minecraft"
    When ele altera a nota para 5.0 e a crítica para "Minecraft é um jogo muito bom, super recomendo jogar o mais rápido possível!"
    Then a média geral do jogo deve ser atualizada
    And o sistema deve salvar a nova versão da avaliação