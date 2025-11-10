package model.torre;

import java.util.ArrayList;
import java.util.List;
import model.Posicao;
import model.inimigos.Inimigos;



public class Torre {

    // Atributos

    private Posicao posicao;
    private int dano;
    private int intervaloTiro;
    private double custo;
    private int cooldown;
    private int alcance;

    //Construtor
    public Torre (Posicao posicao, int dano, int intervaloTiro, double custo, int cooldown, int alcance) {
    this.posicao = posicao;
    this.dano = dano;
    this.intervaloTiro = intervaloTiro;
    this.custo = custo;
    this.cooldown = cooldown;
    this.alcance = alcance;
    }

    //Métodos

    public boolean verificarAlcance(Posicao posinimigo){
        double distancia = Math.sqrt(Math.pow(posinimigo.getColuna() - this.posicao.getColuna(),2) + Math.pow(posinimigo.getLinha() - this.posicao.getLinha(),2));
        return distancia <= this.alcance; // retorna true se for
    }

        public List<Inimigos> inimigosNoAlcance( List<Inimigos> inimigosAtivos){
            ArrayList<Inimigos> inimigosNoAlcance = new ArrayList<>(); // criar um ArrayList
            for(int i = 0; i < inimigosAtivos.size();i++){
                if(verificarAlcance(inimigosAtivos.get(i).getPosicaoAtual())){
                    inimigosNoAlcance.add(inimigosAtivos.get(i));
                }
            }
            return inimigosNoAlcance;
        }

        public Inimigos proximoAlvo(List<Inimigos> inimigosNoAlcance){
            int colunaAnterior = 0;
            Inimigos minimo = null;
            for(int i=0; i < inimigosNoAlcance.size(); i++){
                int colunaAtual = inimigosNoAlcance.get(i).getPosicaoAtual().getColuna();
                if (colunaAnterior < colunaAtual) {
                    colunaAnterior = colunaAtual;
                    minimo = inimigosNoAlcance.get(i);
                    }
            }
            return minimo;
         }

        public boolean atirar(Inimigos alvo){
            Projetil projetil = new Projetil(this.dano, 2.0, this.posicao, alvo);
            this.cooldown = this.intervaloTiro;
            return alvo.receberDano(projetil);
        }

        public boolean podeAtirar(){
            return(this.cooldown <= 0);

        }

        public double atualizarCooldown(){
                this.cooldown --;
                return this.cooldown;
            }
}


            

        



