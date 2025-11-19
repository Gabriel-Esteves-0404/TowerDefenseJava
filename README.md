TowerDefenseJava

Projeto desenvolvido na disciplina Linguagem de Programação Orientada a Objetos (LPOO) — 2025.2
Universidade de Pernambuco (UPE)
Aluno: Gabriel Esteves dos Santos Silva

Descrição:
TowerDefenseJava (BitGuardians) é um jogo do tipo Tower Defense, onde o jogador deve proteger sua base posicionando torres estrategicamente ao longo de um caminho. O projeto aplica os princípios da Programação Orientada a Objetos, como abstração, encapsulamento, herança, polimorfismo e tratamento de exceções.

Funcionalidades:

Checkpoint 1: Implementação do mapa e caminho, movimentação de inimigos, loop de jogo e dano à base.

Checkpoint 2: Adição de torres com disparo e alcance, sistema de projéteis, economia com moedas e recompensa por inimigo derrotado, e gerenciamento de ondas.

Justificativa de Design:
O projeto foi estruturado de forma modular, separando responsabilidades por classe. As classes Inimigos e Torre são abstratas e definem o comportamento genérico das entidades, permitindo a criação de subclasses específicas. O encapsulamento garante a integridade dos atributos, e o polimorfismo possibilita diferentes comportamentos de torres e inimigos sem duplicação de código. O GameLoop gerencia o avanço do tempo, coordenando a atualização de inimigos, torres e projéteis a cada tick.

Como executar:

Remove-Item -Recurse -Force .\bin\* -ErrorAction Ignore
$files = Get-ChildItem -Recurse -Filter *.java .\src\ | ForEach-Object { $_.FullName }
javac -d bin $files
java -cp bin app.Main

Clonar o repositório:
git clone https://github.com/Gabriel-Esteves-0404/TowerDefenseJava.git

Compilar o projeto:
javac -d bin src/**/*.java

Executar o jogo:
java -cp bin jogo.Main

Tecnologias utilizadas:
Java 17, VS Code, Git e GitHub.

Próximos passos:
Implementar sistema de upgrades de torres, efeitos de status (lentidão, queimadura), novos tipos de inimigos e interface gráfica simples.

Licença:
Projeto de uso educacional e livre para fins de aprendizado.
© 2025 — Gabriel Esteves dos Santos Silva