# TowerDefenseJava

Projeto da disciplina **Linguagem de Programação Orientada a Objetos (LPOO) - 2025.2**  
Universidade de Pernambuco (UPE)  

## 👥 Integrantes
- Gabriel Esteves dos Santos Silva  

## 📌 Checkpoint 1
**Objetivo:** Implementar o núcleo do jogo (Mapa, Inimigos e Loop).  

### O que foi implementado
- Classe `Mapa`: cria grid 2D, define caminho fixo, spawn e base.  
- Classe `Posicao`: representa coordenadas no grid.  
- Classe `Inimigos`: percorrem automaticamente o caminho até a base.  
- Classe `App`: loop principal que inicializa o mapa, inimigos e executa o jogo.  

**Critérios atendidos:**
- Inimigos percorrem o caminho e chegam até a base.  
- Loop de jogo funcionando.  
- HUD simples no console (spawn, base, início do loop).  

### ⚙️ Como compilar e executar
No terminal (raiz do projeto):
```bash
javac -d bin -sourcepath src src/app/App.java
java -cp bin app.App
