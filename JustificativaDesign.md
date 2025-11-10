
-----------------------------------------------------------<Classe Posicao> ---------------------------------------------------------------
- Possui os metodos getters retornam os valores inteiros da linha e coluna de uma Posicao 

- Vai ser utilizado a priori na classe Mapa, para gerenciar as celulas presentes na Grid

- Possui um @Override de equals, para comparação de posição, ou seja, as posições são comparadas com o equals

-----------------------------------------------------------<Classe Mapa> -----------------------------------------------------------------

// Atributos e contrutor

- Cria um Array bidimensional booleano chamado <mapa> para informar se tal posição é true (pode construir torres) ou false (não pode construir torres) que tem como tamanho do array uma quatidade fianl de linhas e colunas.

- Cria um Array dinâmico, esssa escolha foi feita para tornar mais simples, conforme for definindo o caminho dos inimigos a partir do tamanho do mapa.

- na definição do <caminhoInimigo> temos a divisão da quantidade de linhas por 2 para indicar o centro do mapa e pecorremos com for para pecorrer as colunas daquelas linhas e tornar o valor booleanos daquelas coordenadas como false(obs: se for impar, o truncamento joga pra o número anterior, ex: 5/2 = 2).

// Métodos

- <estaDentro> vai retornar true ou false indicando se a posição informada está dentro do mapa(true) ou fora (false).

// Getters

- <getBase()>: Última posição do caminhoInimigos (base).
- <getSpawn()>: Primeira posição do caminhoInimigos.
- <getCaminho()>: Retorna o ArrayList das posições do caminhoInimigos.
- <getLinhas() e getColunas()>: retornas as linhas e colunas.

-----------------------------------------------------------<Classe Inimigo> ---------------------------------------------------------------

// Atributos e contrutor

- Atributos: vidaInimimigo, velocidade, indiceCaminho, posicaoAtual, danoBase.

- <IndiceCaminho>, inicialmente = 0, será o indice do ArrayList caminhoInimigos, que indicará a posição do inimigo. A <posiçãoAtual> será a posição indicada pelo caminhosInimigos apontada pelo indiceCaminho.

// Métodos

- <movimentoInimigo> é um método booleano que vai verificar se ele chegou ao fim, caso passe do fim ou chegou lá. A posicaoAtual é recebida a posicao do mapa.getBase() e retorna o valor false (indicando futuramente que ele dar dano e some do jogo). Caso ainda não tenha chegado na base, é incrementado ele pecorre o caminhoInimigos conforme sua velocidade.

- <receberDano> é um método que vai diminuir a vida do inimigo conforme o dano do projetil, após isso, ele verifica se o inimigo está vivo (false) ou morto (true), com isso o Gameloop tira ou não esse inimigo da lista de inimigos ativos

// Getters

- <getPosicaoAtual()>: retorna a posicao Atual que o inimigo se encontra.
- <getDanoBase()>: retorna o dano que o inimigo vai dar na base.

-----------------------------------------------------------<Classe Torre> -----------------------------------------------------------------

// Atributos e contrutor

- Atributos: posicao,dano,intervaloTiro,custo,cooldown,alcance.

// Métodos

- <verificarAlcance> este método booleano ele vai verificar se a posição de um inimigo ele está dentro (true) ou fora (false) do alcance.

- <inimigosNoAlcance> este método ele vai pegar a lista de Inimigos Ativos e vai criar um novo ArrayList "inimigosNoAlcance" e vai colocar dentro dessa listas os inimigos ativos que possuirem um valor true quando forem usados no método verificarAlcance.

- <proximoAlvo> este método retorna o inimigo mais próximo da base que esteja no alcance da torre (esse método de medida serve para um caminho horizontal que é o caminho que a inicio estou usando).

-<atirar> este método recebe como variável um inimigo, cria um projetil e utiliza o método receberDano() no inimigo para diminuir sua vida. Redefini o cooldown para o intervalo de tira.

-<podeAtirar> este método verifica se ainda há cooldown.

-<atualizarCooldown> este método diminui o cooldown e retorna o novo.

-----------------------------------------------------------<Classe Projetil> --------------------------------------------------------------


// Atributos e construtor

- Atributos: dano,velocidade, posicaoInicial, alvo.

// Métodos

<atualizarPosicao>: esse método ele vai pegar as distancias de linhas e de colunas e dividir pela distancia euclidiana, entre o alvo e a torre (inicial do projetil), normalizando essa distancia, o valor é multiplicado pela velocidade e arrendondado para a céula mais próxima. Retorna uma posição.

<colidir> este método compara atráves do equals se o projetil atingiu o inimigo. Retorna booleano.

//Getter

<getaDano()> retorna o dano que o projetil dá.

-----------------------------------------------------------<Classe Banco>------------------------------------------------------------------

//Atributos e construtor

- Atributos: saldo.

//Métodos

- <podeDebitar> este método booleano recebe um inteiro de entrada e verifica se tem saldo suficiente para debitar.

- <debitar> este método recebe um valor de entrada que diminui no saldo, retorna o saldo.

-<creditar> este método adciona um valor de entrada que aumenta no saldo, retorna o saldo

//Getters e Setters

-<getSaldo()>: retorna o saldo.
-<setSaldo()>: altera o saldo.


-----------------------------------------------------------<Classe WaveManager>------------------------------------------------------------

//Atributos e construtor

- Atributos:

- indiceDaOndaAtual ( em que onda está o jogo).
- proximoSpawnTick ( quando spawnará o próximo inimigo).
- restanteNessOnda( quantos inimigos restam nessa onda).
- intervaloSpawn (quanto tempo é pra nascer de inimigo em inimigo na onda).
- vidaPadraoInimigo (o quanto de vida que o inimigo terá naquela onda).
- velocidadePadraoInimigo (o quanto de velocidde o inimigo terá naquela onda).
- recomensaPorKill (valor que se ganha quando um inimigo é morto).

- Construtor: O construtor se inicia neutro, para poder futuramente poder ser alterado seus dados conforme vai se passando as ondas, e para assim que instanciar o waveManager não vim a primeira onda, ela vim apenas quando chamar o método.

// Métodos

<iniciarOnda>: esse é o método que atribui valores aos atributos, inicialmente igual para todas as ondas.

<haMaisSpawnNaOnda>: verifica se há algum inimigo restante na onda.

<spawnsDoTicks>: este método verifica se há inimigos restantes na onda, caso tenha, ele faz outra verificação pra o inicio do jogo, que quando o proximoSpawnTick for 0 ele é atribuito o valor do intervalo de spawn (pre-definido) mais o tick atual. Após faz outra verificação para ver quando o proximoSpawnTick for igual ao intervaloSpawn instanciar um novo inimigo, diminui 1 do inimigos restantes, altera o proximoSpawnTick para (intervaloSpawn + tickAtual) e retorna o ArrayList dos inimigos. Caso o ProximoSpawnTick ainda não alcance o intervaloSpawn pré-estabelecido, apenas é retornada a lista de inimigos.

<ondaConcluida>: este método verifica se o os booleanos de (haMaisSpawnsNaOnda, inimigosAtivosVazios e projeteisAtivosVazio) for true, ele retorna como true também, representando que a onda foi concluida.

// getters

-<getRecompensaPorKill>: retorna o valor da recompensa por kill.

-<getIndiceOndaAtual>: retorna o indice de onda Atual.


-----------------------------------------------------------<Classe GameLoop>---------------------------------------------------------------

//Atributos e construtor

- Atributos:

- mapa (onde vai ser instanciado o mapa).
- banco (onde vai ser instanciado o banco).
- ondas (onde vai ser instanciado o waveManager).
- inimigosAtivos(vai receber uma lista de inimigos que estão vivos no jogo).
- torresAtivas(vai receber uma lista de torres ativas no jogo).
- projeteisAtivos(vai receber uma lista de projeteis que estão no jogo intanciados).
- vidaBase (vida da base).
- tick (contador de tick do jogo, inicia-se 0).
- jogoAtivo(booleano que indica que se for true o jogo continua).

//Métodos

- <tick>: 
1. Inicia-se criando uma lista <Inimigos> que vai receber o método do waveManager spawnsDoTick(), que retornará a lista de inimigos que foram adcionados no tick, se forem adicionados, e caso sejam adcionados, eles são atribuidos aos inimigosAtivos (é utilizada lista para em casos futuros dê para spawnar mais de 1 inimigos por tick).
2. Utiliza um laço para varrer todos os indices da lista, aplica o movimentoInimigo no inimigo da vez, caso o retorno seja false (o inimigo chegou a base), é diminuido vida à base e é removido o inimigo da lista de inimigosAtivos.
3. Verifica se há vida na base, se não houver ele dar valor falso ao atributo de jogoAtivo e sai do método.
4. Utiliza um laço pra pecorrer a lista de torres ativas, instancia o objeto no indice, atualiza o cooldown da torre, verifica se pode atirar, caso seja true, ele vai criar uma lista "alvos" que vai ser a lista dos inimigos no alvo da torre e vai criar um inimigo "alvo" que vai ser o proximoAlvo de alvos, se "alvos" não for vazio. Após ele vai atribuir o boolean "morreu" ao inimigo atirado, caso seja true, é printado que o inimigo morreu e a recompensa adquirida, o saldo bancário e o saldo bancário é setado.
5. Verifica se a onda foi concluida e se for, o jogoAtivo é dado como false e sai do método.

- <adcionarTorre>: método que acrescenta uma torre instanciada à lista de torres ativas

- <run>: aqui inicia a primeira onda e tem-se um laço com o while como parâmetro é utilizado o boolean "jogoAtivo" e enquanto for true é aplicados ticks.


-----------------------------------------------------------<Classe Loja>------------------------------------------------------------------

//Atributos e construtores

- Não há construtores nem atributos apenas métodos.

// Métodos

- <comprarTorreNormal>: declarei um custo fixo da torre sendo 10.0, é verificado um se há saldo para o custo, se houver ele debita e cria a torre, caso não haja, ele apenas emiti uma mensagem de saldo insuficiente e retorna vazio.

- <comprarTorreNormal>: o mesmo do método anterior, porém custo da torre informado na classe (20).

-----------------------------------------------------------<Classe TorreArqueira>------------------------------------------------------------------

- Método herdado de torre, com valores dos atributos diferentes, herdando tudo.

-----------------------------------------------------------<Classe Main>------------------------------------------------------------------

- é instanciado todos as classes, de inicio para teste é posto no jogo:

- 2 torres (1 arqueira e 1 normal).
- Mapa com 5 linhas e 10 colunas.
- Inicio de saldo com 50.0.
- base com 5 de hp.
- Apenas 1 onda.


-------------------------------------------------------------------------------------------------------------------------------------------

// Para compilar e rodar:

-javac -d bin (Get-ChildItem -Recurse -Filter *.java | ForEach-Object { $_.FullName })
java -cp bin app.Main





