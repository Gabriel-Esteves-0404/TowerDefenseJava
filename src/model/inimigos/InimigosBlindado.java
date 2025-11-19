package model.inimigos;

public class InimigosBlindado extends Inimigos{

    public InimigosBlindado(){
        super(6,1,2,4);
    } 

    @Override
    public void aplicarCongelamento(int duracaoTicks) {
    }

    @Override
    public void aplicarVeneno(int danoPorTick, int duracaoTicks) {
    }
}
