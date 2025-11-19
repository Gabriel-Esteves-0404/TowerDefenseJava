package jogo;

import java.util.ArrayList;
import java.util.List;
import model.inimigos.Inimigos;
import model.inimigos.InimigosBlindado;
import model.inimigos.InimigosCorredor;
import model.inimigos.InimigosGolem;
import model.inimigos.InimigosZumbi;

public class WaveManager {
    private int indiceDaOndaAtual;
    private int proximoSpawnTick;
    private int restanteNessaOnda;
    private int intervaloSpawn;
    private boolean primeiroSpawn;
    private final int TOTAL_DE_ONDAS = 5;
    private List<Inimigos> listaInimigosDaOndaAtual;



    public WaveManager(){
        indiceDaOndaAtual = 0;
        proximoSpawnTick = 0;
        restanteNessaOnda = 0;
        intervaloSpawn = 0;
        primeiroSpawn = true;
    }

    public void iniciarOnda(){
        indiceDaOndaAtual = 1;
        listaInimigosDaOndaAtual = listaInimigosDeInicioDaOnda(indiceDaOndaAtual);
        restanteNessaOnda = listaInimigosDaOndaAtual.size();
        intervaloSpawn = 5;
        proximoSpawnTick = 0;
        primeiroSpawn = true;

        System.out.println("\n=== Iniciando Onda " + indiceDaOndaAtual + 
                    " com " + restanteNessaOnda + " inimigos ===");

    }

    public List<Inimigos> listaInimigosDeInicioDaOnda(int indiceDaOndaAtual){
        List<Inimigos> listaInimigos = new ArrayList<>();
        if(indiceDaOndaAtual == 1){
            for(int i = 0; i < 5; i++){
                InimigosGolem golem = new InimigosGolem();
                listaInimigos.add(golem);
            }
        }
        else if(indiceDaOndaAtual < 6){
            for(int i = 0; i < indiceDaOndaAtual - 1; i++){
                for(int j = 0; j < 2; j++){
                    InimigosZumbi zumbi = new InimigosZumbi();
                    listaInimigos.add(zumbi);
                }
                InimigosGolem golem = new InimigosGolem();
                listaInimigos.add(golem);
                InimigosBlindado blindado = new InimigosBlindado();
                listaInimigos.add(blindado);
                InimigosCorredor corredor = new InimigosCorredor();
                listaInimigos.add(corredor);                   
            }
        }
        return listaInimigos;
    }
    
    public void proximaOnda(){
        indiceDaOndaAtual += 1 ;
        listaInimigosDaOndaAtual = listaInimigosDeInicioDaOnda(indiceDaOndaAtual);
        restanteNessaOnda = listaInimigosDaOndaAtual.size(); 
        intervaloSpawn = 6;
        proximoSpawnTick = 0;
        primeiroSpawn = true;
        System.out.println("\n=== Iniciando Onda " + indiceDaOndaAtual + 
                       " com " + restanteNessaOnda + " inimigos ===");
    }

    public boolean ultimaOndaTerminou(boolean inimigosAtivosVazios, boolean projeteisAtivosVazios) {
    return (indiceDaOndaAtual == TOTAL_DE_ONDAS 
            && !haMaisSpawnsNaOnda() 
            && inimigosAtivosVazios 
            && projeteisAtivosVazios);
}


    public boolean haMaisSpawnsNaOnda(){
        return(restanteNessaOnda > 0);
    }

    public List<Inimigos> spawnNasOnda(int tickAtual){
        List<Inimigos> inimigosNoJogo = new ArrayList<>();
        if(haMaisSpawnsNaOnda()){
            if(proximoSpawnTick == 0){
                proximoSpawnTick = (tickAtual + intervaloSpawn);
                return inimigosNoJogo;
            }

            if(indiceDaOndaAtual == 1){ 
                if(proximoSpawnTick <= tickAtual){
                    Inimigos inimigo = listaInimigosDaOndaAtual.get(0);
                    inimigosNoJogo.add(inimigo);
                    listaInimigosDaOndaAtual.remove(0);
                    restanteNessaOnda--;

                    System.out.println("Spawnado " + inimigo.getClass().getSimpleName() +
                                    " na Onda " + indiceDaOndaAtual +
                                    " no Tick " + tickAtual);

                    if(!listaInimigosDaOndaAtual.isEmpty()){
                        proximoSpawnTick = (tickAtual + intervaloSpawn);
                    }
                }
                return inimigosNoJogo;
            }

            if(proximoSpawnTick <= tickAtual){
                if(primeiroSpawn){
                    Inimigos inimigo1 = listaInimigosDaOndaAtual.get(0);
                    Inimigos inimigo2 = listaInimigosDaOndaAtual.get(1);

                    inimigosNoJogo.add(inimigo1);
                    inimigosNoJogo.add(inimigo2);

                    listaInimigosDaOndaAtual.remove(0);
                    listaInimigosDaOndaAtual.remove(0);
                    restanteNessaOnda-=2;
                    primeiroSpawn = false;

                    System.out.println("\n Spawnado " + 
                        inimigo1.getClass().getSimpleName() + " e " + inimigo2.getClass().getSimpleName() +
                        " na Onda " + indiceDaOndaAtual +
                        " no Tick " + tickAtual);

                }
                else {
                    Inimigos inimigo = listaInimigosDaOndaAtual.get(0);
                    inimigosNoJogo.add(inimigo);
                    listaInimigosDaOndaAtual.remove(0);
                    restanteNessaOnda--;

                    System.out.println("\n Spawnado " + inimigo.getClass().getSimpleName() +
                                    " na Onda " + indiceDaOndaAtual +
                                    " no Tick " + tickAtual);


                }
                
                if(!listaInimigosDaOndaAtual.isEmpty()){
                        proximoSpawnTick = (tickAtual + intervaloSpawn - (indiceDaOndaAtual - 2));
                    
                }
                return inimigosNoJogo;
            }
        }
        return inimigosNoJogo;     
    }

        public boolean ondaConcluida(boolean inimigosAtivosVazios, boolean projeteisAtivosVazios) {
            return(!haMaisSpawnsNaOnda() && inimigosAtivosVazios && projeteisAtivosVazios);          
        }

        public int getIndiceOndaAtual(){
            return indiceDaOndaAtual;
        }
        
    }