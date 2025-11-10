package jogo;

import java.util.ArrayList;
import java.util.List;
import model.inimigos.Inimigos;

public class WaveManager {
    private int indiceDaOndaAtual;
    private int proximoSpawnTick;
    private int restanteNessaOnda;
    private int intervaloSpawn;
    private int vidaPadraoInimigo;
    private int velocidadePadraoInimigo;
    private int recompensaPorKill;

    public WaveManager(){
        this.indiceDaOndaAtual = 0;
        this.proximoSpawnTick = 0;
        this.restanteNessaOnda = 0;
        this.intervaloSpawn = 0;
        this.vidaPadraoInimigo = 0;
        this.velocidadePadraoInimigo = 0;
        this.recompensaPorKill = 0;

    }
    public void iniciarOnda(int tickAtual){
        this.indiceDaOndaAtual += 1 ;
        this.restanteNessaOnda = 5;
        this.intervaloSpawn = 8;
        this.vidaPadraoInimigo = 10;
        this.velocidadePadraoInimigo = 1;
        this.recompensaPorKill = 5;
        this.proximoSpawnTick = 0;
    }

    public boolean haMaisSpawnsNaOnda(){
        return(restanteNessaOnda > 0);
    }

    public List<Inimigos> spawnsDoTick(int tickAtual){
        List<Inimigos> novosInimigos = new ArrayList<>();
        if(haMaisSpawnsNaOnda()){
            if(proximoSpawnTick == 0){
                proximoSpawnTick = (tickAtual + intervaloSpawn);
                return novosInimigos;
            }
            if(proximoSpawnTick <= tickAtual){
                Inimigos inimigo = new Inimigos(vidaPadraoInimigo, velocidadePadraoInimigo);
                proximoSpawnTick = (tickAtual + intervaloSpawn);
                novosInimigos.add(inimigo);
                restanteNessaOnda --;
                return novosInimigos;   
            }
        }
        return novosInimigos;
    }

        public boolean ondaConcluida(boolean inimigosAtivosVazios, boolean projeteisAtivosVazios) {
            return(!haMaisSpawnsNaOnda() && inimigosAtivosVazios && projeteisAtivosVazios);          
        }

        public int getRecompensaPorKill(){
            return recompensaPorKill;
        }

        public int getIndiceOndaAtual(){
            return indiceDaOndaAtual;
        }
    }