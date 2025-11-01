
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

