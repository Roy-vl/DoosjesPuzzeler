/*
StrategyPicker is a class that will pick and return an appropriate PackerStrategy for any ProblemStatement
*/

public class StrategyPicker {
    
    public PackerStrategy pick(ProblemStatement PS){

        if(PS.getRectangleAmount()<25){
            return new BacktrackCornerPack();
        }     
        
        if(PS.getContainerHeight()>0){
                
            return new GreedyCornerPack();

        }

        return new MultipleGreedyCornerPack();

    } 
}
