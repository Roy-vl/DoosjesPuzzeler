/*
StrategyPicker is a class that will pick and return an appropriate PackerStrategy for any ProblemStatement
*/

public class StrategyPicker {
    
    public PackerStrategy pick(ProblemStatement PS){

        if(PS.getRectangleAmount()<25){
            return new BacktrackCornerPack();
        }     
        
        if(PS.getRectangleAmount()>=25){
            if(PS.getContainerHeight()>0){
                
                return new GreedyCornerPack();
                
            }else{

                return new MultipleGreedyCornerPack();
            }
        }
        
        return new PackNextToEachother();
    } 
}
