package model.inimigos;

public class InimigosCorredor extends Inimigos {

    public InimigosCorredor(){
        super(8,3,1,2);
        this.multiplicadorVelocidade = 1.4;
        carregarFramesComParenteses("CORREDOR_RUN_", 7);
    }
    
}
