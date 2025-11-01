package jogo;

import java.util.ArrayList;
import java.util.List;
import model.Mapa;
import model.inimigos.Inimigos;

public class WaveManager {
    private int indiceDaOndaAtual;
    private int proximoSpawnTick;
    private int restanteNessaOnda;
    private int intervaloSpawn;
    private int atrasoInicial;
    private int vidaPadraoInimigo;
    private int velocidadePadraoInimigo;
    private int recompensaPorKill;

    public WaveManager(){
        this.indiceDaOndaAtual = 0;
        this.proximoSpawnTick = 0;
        this.restanteNessaOnda = 0;
        this.intervaloSpawn = 0;
        this.atrasoInicial = 0;
        this.vidaPadraoInimigo = 0;
        this.velocidadePadraoInimigo = 0;
        this.recompensaPorKill = 0;

    }
    public void iniciarPrimeiraOnda(int tickAtual){
        this.indiceDaOndaAtual = 0;
        this.restanteNessaOnda = 5;
        this.intervaloSpawn = 8;
        this.atrasoInicial = 0;
        this.vidaPadraoInimigo = 5;
        this.velocidadePadraoInimigo = 1;
        this.recompensaPorKill = 5;
        this.proximoSpawnTick = tickAtual + this.atrasoInicial;
    }

    public List<Inimigos> spawnsDoTick(int tickAtual, Mapa mapa){
        List<Inimigos> novosInimigos = new ArrayList<>();

        if(this.restanteNessaOnda == 0){
            return novosInimigos;
        }

        if (this.proximoSpawnTick <= tickAtual){
            Inimigos inimigos = new Inimigos(vidaPadraoInimigo, velocidadePadraoInimigo);
            this.proximoSpawnTick += this.intervaloSpawn;
            novosInimigos.add(inimigos);
            this.restanteNessaOnda --;
            return novosInimigos;   
        }
            return novosInimigos;
        }

        public boolean haMaisSpawnsNaOnda(){
            if(restanteNessaOnda <= 0){
                return false;
            }
            return true;
        }

        public boolean ondaConcluida(boolean inimigosAtivosVazios, boolean projeteisAtivosVazios) {
            if (!haMaisSpawnsNaOnda() && inimigosAtivosVazios && projeteisAtivosVazios) {
                return true;
                }
                    return false;
                    
                        }


        public int getRecompensaPorKill(){
            return recompensaPorKill;
        }

        public int getIndiceOndaAtual(){
            return indiceDaOndaAtual;
        }
    }