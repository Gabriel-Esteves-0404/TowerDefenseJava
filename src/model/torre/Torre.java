package model.torre;

import java.util.ArrayList;
import java.util.List;
import model.Posicao;
import model.economia.Banco;
import model.excecoes.NivelMaximoException;
import model.excecoes.SaldoInsuficienteException;
import model.inimigos.Inimigos;

public abstract class Torre {

    protected Posicao posicao;
    protected int dano;
    protected int intervaloTiro;
    protected double custo;
    protected int cooldown;
    protected  int alcance;
    protected  int nivel = 1;
    protected int maxNivel = 3;

    public Torre (Posicao posicao, int dano, int intervaloTiro, double custo, int cooldown, int alcance) {
        this.posicao = posicao;
        this.dano = dano;
        this.intervaloTiro = intervaloTiro;
        this.custo = custo;
        this.cooldown = cooldown;
        this.alcance = alcance;
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

        public abstract Projetil atirar(Inimigos alvo);

        public boolean podeAtirar(){
            return(this.cooldown <= 0);
        }

        public double atualizarCooldown(){
                this.cooldown --;
                return this.cooldown;
            }

        public boolean podeMelhorar() {
        return nivel < maxNivel;
    }

    public boolean melhorar(Banco banco) throws NivelMaximoException, SaldoInsuficienteException {
        if (!podeMelhorar()) {
            throw new NivelMaximoException("A torre já está no nível máximo.");
        }
        int custoUpgrade = getCustoUpgrade();
        if (banco.getSaldo() < custoUpgrade) {
            throw new SaldoInsuficienteException("Saldo insuficiente para upgrade.");
        }
        banco.setSaldo(banco.getSaldo() - custoUpgrade);
        nivel++;
        aplicarMelhoriasDoNivel();
        return true;
    }
        protected abstract void aplicarMelhoriasDoNivel();

        public int getNivel() {
            return nivel;
        }

        public Posicao getPosicao(){
            return posicao;
        }

        public int getMaxNivel() {
            return maxNivel;
        }

        public int getCustoUpgrade() {
            return switch (this.nivel) {
                case 1 -> 15;
                case 2 -> 25;
                case 3 -> 35;
                default -> 0;
            };
        }

        public int getAlcance() {
            return alcance;
        }

        public void tickAnimacao() {

        }


}
