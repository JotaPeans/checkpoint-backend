Feature: Listas – criar listas temáticas de jogos, wishlists e coleção de jogos mais jogados.

  Scenario: Lista privada
    Given que o usuário "Lari" cria a lista "Jogos clickers favoritos" com o jogo "Idle slayer" e "Cookie clicker" e modo "privado"
    Then o sistema deve salvar a lista

  Scenario: Lista publica
    Given que o usuário "Lari" cria a lista "Jogos favoritos" com o jogo "Portal 2" e "Peak" e modo "publico"
    Then o sistema deve salvar a lista

  Scenario: Lista com mais de 100 jogos
    Given que o usuário "Lari" tenta criar a lista "120 jogos" com os jogos "Stardew Valley, Stray, Hollow Knight, Hades, Outer Wilds, GRIS, Overcooked 2, Lethal Company, Valorant, Cyberpunk 2077, Minecraft, Grand Theft Auto V, The Witcher 3: Wild Hunt, Portal 2, Counter-Strike: Global Offensive, Tomb Raider (2013), Portal, Left 4 Dead 2, The Elder Scrolls V: Skyrim, Red Dead Redemption 2, Elden Ring, Sekiro: Shadows Die Twice, Dark Souls, Dark Souls II, Dark Souls III, Demon’s Souls (Remake), Bloodborne, Ghost of Tsushima, Horizon Zero Dawn, Horizon Forbidden West, God of War (2018), God of War: Ragnarok, Uncharted 4: A Thief’s End, Uncharted: The Lost Legacy, The Last of Us, The Last of Us Part II, Death Stranding, Metal Gear Solid V: The Phantom Pain, Metal Gear Solid 3: Snake Eater, Resident Evil 2 (Remake), Resident Evil 3 (Remake), Resident Evil 4 (Remake), Resident Evil 7: Biohazard, Resident Evil Village, Silent Hill 2, Silent Hill 4: The Room, Alan Wake, Alan Wake II, Control, Quantum Break, Assassin’s Creed Origins, Assassin’s Creed Odyssey, Assassin’s Creed Valhalla, Assassin’s Creed IV: Black Flag, Assassin’s Creed II, Assassin’s Creed Brotherhood, Far Cry 3, Far Cry 4, Far Cry 5, Far Cry 6, Watch Dogs, Watch Dogs 2, Watch Dogs Legion, Dishonored, Dishonored 2, Prey (2017), DOOM (2016), DOOM Eternal, Wolfenstein: The New Order, Wolfenstein II: The New Colossus, Wolfenstein: Youngblood, Call of Duty: Modern Warfare (2019), Call of Duty: Modern Warfare II (2022), Call of Duty: Black Ops Cold War, Call of Duty: Warzone, Battlefield 1, Battlefield V, Battlefield 2042, Apex Legends, Fortnite, PUBG: Battlegrounds, League of Legends, Dota 2, Team Fortress 2, Among Us, Fall Guys, Rocket League, Destiny 2, Overwatch, Overwatch 2, Diablo III, Diablo IV, StarCraft II, World of Warcraft, Hearthstone, Genshin Impact, Honkai: Star Rail, Monster Hunter: World, Monster Hunter Rise, Bayonetta, Bayonetta 2, Bayonetta 3, Splatoon 2, Splatoon 3, Super Mario Odyssey, Super Mario Bros. Wonder, Super Mario 64, Super Mario Sunshine, Super Mario Galaxy, Mario Kart 8 Deluxe, Super Smash Bros. Ultimate, The Legend of Zelda: Breath of the Wild, The Legend of Zelda: Tears of the Kingdom, The Legend of Zelda: Ocarina of Time, The Legend of Zelda: Majora’s Mask, Xenoblade Chronicles, Xenoblade Chronicles 2, Xenoblade Chronicles 3, Fire Emblem: Three Houses, Fire Emblem Engage"
    Then o sistema notifica que Uma lista não pode conter mais de 100 jogos

  Scenario: Duplicar lista pública
    Given que o usuário "Lari" duplica a lista "Jogos legais" do usuario "Davi"
    Then a lista "CÓPIA Jogos legais" aparece nas listas do usuário

  Scenario: Duplicar lista pessoal
    Given que o usuário "Lari" duplica a sua lista "Jogos legais"
    Then a lista "CÓPIA Jogos legais" aparece nas listas do usuário

  Scenario: Editar lista duplicada
    Given que o usuário "Lari" tem a lista duplicada "CÓPIA Jogos legais"
    When o nome é editado para "Meus jogos legais" e o jogo "Terraria" é adicionado
    Then a lista é atualizada