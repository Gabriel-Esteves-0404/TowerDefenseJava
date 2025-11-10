<<<<<<< HEAD
# TowerDefenseJava

Projeto da disciplina **Linguagem de Programação Orientada a Objetos (LPOO)** – 2025.2  
Universidade de Pernambuco (UPE)  
**Aluno:** Gabriel Esteves dos Santos Silva  

---

## Checkpoint 1 – Mapa, caminho e inimigos

### Objetivo
O primeiro checkpoint teve como foco a construção da **base do jogo Tower Defense**, com:
- Representação do mapa e das posições (classe `Mapa` e `Posicao`);
- Implementação da movimentação dos inimigos (`Inimigos`);
- Estruturação inicial do loop de jogo e da lógica de avanço dos inimigos.

### Funcionalidades implementadas
1. **Mapa e Caminho:**  
   O mapa foi definido como uma grade de posições (`Posicao`), com coordenadas de linha e coluna.  
   O caminho percorrido pelos inimigos é representado como uma sequência ordenada de posições válidas.

2. **Classe Inimigos:**  
   - Atributos: `vida`, `velocidade`, `posicaoAtual`.  
   - Métodos principais:
     - `mover()`: faz o inimigo avançar no caminho.
     - `receberDano(int dano)`: reduz a vida do inimigo.
   - O inimigo percorre o caminho do mapa até atingir a base, momento em que a vida da base é reduzida.

3. **Simulação inicial:**  
   A cada tick do jogo, a posição do inimigo era atualizada e impressa no console, mostrando o movimento ao longo do mapa.



---

## Checkpoint 2 – Torres, projéteis e economia

Nesta segunda etapa, o jogo Tower Defense foi expandido com novas funcionalidades:
- Implementação de torres que detectam e atacam inimigos automaticamente.
- Criação da classe de projéteis, que se deslocam e causam dano aos inimigos.
- Sistema de economia, com saldo inicial, custo e recompensa por abates.
- Gerenciamento de ondas de inimigos e condição de vitória.
- Feedback completo no console, indicando tiros, mortes e progresso do jogo.

---

### Como executar

```bash
javac -d bin $(git ls-files "*.java")
java -cp bin app.Main


Exemplo de saída no console

--- Tick 0 ---
Inimigo em: (2,0)
Tick 0 | Vida da base: 5 | Inimigos ativos: 1

--- Tick 1 ---
Inimigo em: (2,1)
Torre atirou no inimigo em (2,1)
Tick 1 | Vida da base: 5 | Inimigos ativos: 1

--- Tick 4 ---
Inimigo em: (2,4)
Torre atirou no inimigo em (2,4)
Inimigo morto! +5 moedas. Saldo=20
Tick 4 | Vida da base: 5 | Inimigos ativos: 0
...
VITÓRIA!



### 1. Estrutura geral do jogo

O código é modularizado em pacotes (app, jogo, model) para garantir organização e separação de responsabilidades.
A execução inicia em app.Main, que instancia o mapa, o banco, as torres e o GameLoop, responsável por coordenar toda a execução do jogo.

O GameLoop é o coração do sistema, baseado em ticks (unidades de tempo discretas).
A cada tick:

Os inimigos se movem.

As torres atualizam seu cooldown.

Cada torre verifica se há inimigos dentro do alcance.

Se possível, atira e cria um Projetil.

Os projéteis se movem e aplicam dano nos inimigos.

O Banco atualiza o saldo com recompensas por abates.

O WaveManager coordena a aparição das ondas.

Essa estrutura mantém o jogo previsível e testável, essencial para futuras expansões e interface gráfica.

## 2. Classe Torre

A classe Torre define atributos e comportamentos básicos:

dano, alcance, intervaloTiro, cooldown, posicao.

Principais métodos:

podeAtirar(): verifica se o cooldown chegou a zero.

verificarAlcance(Posicao inimigo): calcula a distância entre torre e inimigo.

atualizarCooldown(int deltaTempo): diminui o cooldown a cada tick.

A torre só dispara se o cooldown estiver zerado e o inimigo estiver no alcance.
O disparo instancia um Projetil que carrega a lógica de colisão.

A subclasse TorreArqueira define valores específicos de dano, alcance e intervalo de tiro.

## 3. Classe Projetil

A classe Projetil representa o ataque em movimento:

Atributos: dano, velocidade, posicaoInicial, alvo.

Método atualizarPosicao() calcula a direção e o movimento do projétil até o inimigo.
Quando o projétil chega ao alvo, aplica o dano e é removido.

Essa separação entre torre e ataque mantém baixo acoplamento e alto potencial de expansão (mísseis, lasers, etc.).

### 4. Classe Inimigos

A classe Inimigos contém os atributos:

vida, velocidade, posicaoAtual.

Métodos:

mover(): desloca o inimigo a cada tick.

receberDano(int dano): reduz a vida e retorna se o inimigo foi derrotado.

A integração com o Banco garante recompensa imediata por abate.

## 5. Sistema de economia

O Banco centraliza o gerenciamento de moedas:

adicionarMoedas(int valor)

removerMoedas(int custo)

getSaldo()

Toda a transação financeira passa pelo Banco, garantindo encapsulamento e controle único das regras de negócio.
A Loja será usada nas próximas etapas para permitir compra de torres e upgrades.

## 6. Controle de ondas

O WaveManager organiza o surgimento de inimigos em ondas sucessivas.
Ele cria inimigos com intervalo temporal entre cada spawn e envia para o GameLoop, que os insere na lista de inimigos ativos.
Quando todas as ondas terminam e a base não foi destruída, o jogo declara vitória.

## 7. Decisões de design

Uso de ticks no GameLoop: facilita controle e depuração.

Cooldown individual por torre: evita disparos contínuos e permite equilíbrio.

Separação entre Torre, Projetil e Inimigo: reduz acoplamento e melhora legibilidade.

Padrão de pacotes: estrutura clara e escalável.

Princípio SRP (Responsabilidade Única): cada classe cumpre um papel específico.

Extensibilidade: o código está preparado para novas torres, inimigos e efeitos.

=======

# 🏰 TowerDefenseJava

Projeto desenvolvido na disciplina **Linguagem de Programação Orientada a Objetos (LPOO)** – 2025.2  
Universidade de Pernambuco (UPE)  
**Aluno:** Gabriel Esteves dos Santos Silva  

---

## 🧩 Checkpoint 1 – Mapa, Caminho e Inimigos

**Objetivo:** Criar a base lógica do jogo Tower Defense.

**Principais implementações:**
- Estrutura do mapa (`Mapa`) e posições (`Posicao`);
- Criação e movimentação dos inimigos (`Inimigos`);
- Início do loop de jogo (`GameLoop`) simulando o avanço dos inimigos até a base.


## ⚔️ Checkpoint 2 – Torres, Projéteis e Economia

**Objetivo:** Adicionar interação entre torres e inimigos, e introduzir economia no jogo.

**Principais implementações:**
- 🏹 Torres detectam inimigos e disparam automaticamente (`Torre`, `TorreArqueira`);
- 💥 Projéteis se movem e causam dano (`Projetil`);
- 💰 Sistema de moedas e recompensas (`Banco`);
- 🌊 Controle de ondas de inimigos (`WaveManager`);
- 🧾 Feedback detalhado no console com tiros, mortes e saldo atualizado.

**Exemplo de saída:**


--- Tick 4 ---
Torre atirou no inimigo em (2,4)
Inimigo morto! +5 moedas. Saldo=20
VITÓRIA!




## 🧠 Estrutura geral



src/
├─ app/Main.java
├─ jogo/GameLoop.java, WaveManager.java
└─ model/
├─ economia/Banco.java, Loja.java
├─ inimigos/Inimigos.java
└─ torre/Torre.java, TorreArqueira.java, Projetil.java


O jogo funciona em *ticks*, onde a cada ciclo:
1. Inimigos se movem;
2. Torres verificam alcance e disparam;
3. Projéteis atingem alvos;
4. Banco atualiza o saldo;
5. O `WaveManager` controla novas ondas.



## 🧩 Decisões de Design

- Estrutura modular por pacotes (`app`, `jogo`, `model`);
- Baixo acoplamento entre **Torre**, **Projetil** e **Inimigo**;
- Sistema baseado em *ticks* para facilitar testes e balanceamento;
- Economia centralizada no `Banco` (SRP – responsabilidade única);
- Arquitetura preparada para novas torres, inimigos e interface visual.

---

## ✅ Próximos Passos (Checkpoint 3)

- Novos tipos de torres e inimigos;  
- Loja funcional para upgrades;  
- Interface visual (HUD);  
- Ajuste de dificuldade e equilíbrio.

---

## 👨‍💻 Autor

**Gabriel Esteves dos Santos Silva**  
Engenharia da Computação – UPE  

>>>>>>> 7fcf45d2fd63349651927312fa4798bf39de5498
