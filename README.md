🐆 README.md — TowerDefenseJava (Tower Defense Game)

Projeto concluído — Checkpoint 4 (LPOO 2025.2)

🎮 TowerDefenseJava — BitGuardians

Projeto desenvolvido para a disciplina Linguagem de Programação Orientada a Objetos (LPOO) — 2025.2
Universidade de Pernambuco (UPE)
Aluno: Gabriel Esteves dos Santos Silva

📘 Descrição do Projeto

BitGuardians é um jogo completo do gênero Tower Defense, no qual o jogador deve proteger sua base posicionando torres estrategicamente ao longo do caminho percorrido pelos inimigos.
O projeto aplica rigorosamente os pilares da Programação Orientada a Objetos:

Abstração

Encapsulamento

Herança

Polimorfismo

Tratamento de Exceções

O jogo conta com torres, projéteis, inimigos com diferentes comportamentos, economia, ondas progressivas e interface gráfica funcional.

🧩 Funcionalidades Implementadas (Checkpoints 1 → 4)
✔ Checkpoint 1 — Núcleo do jogo

Mapa baseado em grid 2D

Caminho fixo percorrido pelos inimigos

Movimentação automática dos inimigos

GameLoop baseado em ticks

Inimigos causando dano à base

Condição de derrota

✔ Checkpoint 2 — Torres, Disparo e Economia

Sistema de posicionamento de torres em células construíveis

Estratégias de mira e disparo automático

Projéteis com velocidade, direção e colisão

Economia com:

custo das torres

recompensa por inimigos derrotados

Gerenciamento de ondas (WaveManager)

✔ Checkpoint 3 — Upgrades e Variedade

Sistema de upgrades para torres

Efeitos de status:

Lentidão

Veneno / Dano por Tick

Novos tipos de inimigos com:

diferentes velocidades

resistências

recompensas

Herança e polimorfismo aplicados

✔ Checkpoint 4 — Versão Final

Interface gráfica completa (HUD, renderização, sprites)

7 ondas progressivas

Diferentes torres funcionais

Feedback visual para dano, morte, congelamento e veneno

Tela de vitória e derrota

Jogo totalmente jogável do início ao fim

🏛 Justificativa de Design (Arquitetura OO)

O projeto foi estruturado de forma modular e extensível, respeitando o princípio de Responsabilidade Única.
Destaques arquiteturais:

🔹 Inimigos

Inimigos é uma classe abstrata que define atributos essenciais (vida, velocidade, posição, animação).
Subclasses como Zumbi, Golem e Golemitas implementam comportamentos específicos.

🔹 Torres

Torre também é abstrata, permitindo criar torres com:

diferentes alcances

cadências

tipos de projétil

status effects

O polimorfismo garante que o GameLoop trate qualquer torre ou inimigo de forma uniforme.

🔹 GameLoop

Responsável por orquestrar:

movimento dos inimigos

disparo das torres

atualização das animações

spawn das ondas

efeitos de veneno, lentidão e congelamento

🔹 Separação por pacotes

model (lógica principal)

jogo (loop, controlador geral)

ui (interface gráfica)

assets (sprites e animações)

Essa divisão fortalece encapsulamento e manutenção.

▶️ Como Executar o Projeto
1. Clonar o repositório
git clone https://github.com/Gabriel-Esteves-0404/TowerDefenseJava.git

2. Compilar o projeto (Windows PowerShell)
Remove-Item -Recurse -Force .\bin\* -ErrorAction Ignore
$files = Get-ChildItem -Recurse -Filter *.java .\src\ | ForEach-Object { $_.FullName }
javac -d bin $files

3. Executar
java -cp bin app.Main


Dependendo da versão do projeto, o main pode estar em:

java -cp bin jogo.Main

🛠 Tecnologias Utilizadas

Java 17

VS Code

Git & GitHub

Swing (Interface Gráfica)

Sprites PNG para animações

🚀 Próximos Passos / Extensões Futuras

Melhorar efeitos visuais e animações

Inserir sons de disparo, hit e morte

Criar novas torres (Sniper, Bombarda, Laser)

Criar novo mapa com múltiplos caminhos

Sistema de dificuldade dinâmica

Tela de pausa e opções

📄 Licença

Projeto livre para fins educacionais e aprendizado.
© 2025 — Gabriel Esteves dos Santos Silva
