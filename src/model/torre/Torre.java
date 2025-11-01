package model.torre;

import java.util.ArrayList;
import java.util.List;
import model.Posicao;
import model.inimigos.Inimigos;


public class Torre {
    private Posicao posicao;
    private int dano;
    private double intervaloTiro;
    private double custo;
    private double cooldown;
    private int alcance;

    public Torre (Posicao posicao, int dano, double intervaloTiro, double custo, double cooldown, int alcance) {
    this.posicao = posicao;
    this.dano = dano;
    this.intervaloTiro = intervaloTiro;
    this.custo = custo;
    this.cooldown = cooldown;
    this.alcance = alcance;
    }

    public double atualizarCooldown(int deltaTempo){
            this.cooldown -= deltaTempo;
            return this.cooldown;
        }

    public boolean podeAtirar(){
        if(this.cooldown <= 0){
            return true;
        }
        return false;
    }

    public boolean verificarAlcance(Posicao posinimigo){
        double distancia = Math.sqrt(Math.pow(posinimigo.getColuna() - this.posicao.getColuna(),2) + Math.pow(posinimigo.getLinha() - this.posicao.getLinha(),2));
        return distancia <= this.alcance;
    }

    public List<Inimigos> inimigosNoAlcance( List<Inimigos> inimigosAtivos){
        ArrayList<Inimigos> inimigosNoAlcance = new ArrayList<>();
        for(int i = 0; i < inimigosAtivos.size();i++){
            if(verificarAlcance(inimigosAtivos.get(i).getPosicaoAtual())){
                inimigosNoAlcance.add(inimigosAtivos.get(i));
            }
            }
            return inimigosNoAlcance;
        }
    public Inimigos proximoAlvo(List<Inimigos> inimigosNoAlcance){
        double distancia = Double.MAX_VALUE;
        Inimigos minimo = null;
        for(int i=0; i < inimigosNoAlcance.size(); i++){
            double distanciaAtual = Math.sqrt(Math.pow(inimigosNoAlcance.get(i).getPosicaoAtual().getColuna() - this.posicao.getColuna(),2) + Math.pow(inimigosNoAlcance.get(i).getPosicaoAtual().getLinha() - this.posicao.getLinha(),2));
             if (distancia > distanciaAtual) {
                distancia = distanciaAtual;
                minimo = inimigosNoAlcance.get(i);
             }
        }
        return minimo;
    }
    public boolean atirar(model.inimigos.Inimigos alvo){
    Projetil projetil = new Projetil(this.dano, 2.0, this.posicao, alvo);
    this.cooldown = this.intervaloTiro;
    return alvo.receberDano(projetil);
}

    }


            

        



