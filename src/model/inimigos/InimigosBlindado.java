package model.inimigos;

public class InimigosBlindado extends Inimigos{

    public InimigosBlindado(){
        super(12,1,2,3);
        carregarFramesComParenteses("BLINDADO_WALK_", 7);
    } 

    @Override
    public void aplicarCongelamento(int duracaoTicks) {
    }

    @Override
    public void aplicarVeneno(int danoPorTick, int duracaoTicks) {
    }
}
