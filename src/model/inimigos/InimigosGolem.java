package model.inimigos;

public class InimigosGolem extends Inimigos{

    public InimigosGolem(){
        super(14,1,2,0);
        carregarFramesComParenteses("GOLEM_WALK_", 7);
    }
}
