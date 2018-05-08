/*
StrategyPicker is a class that will pick and return appropriate PackerStrategy for any ProblemStatement
*/

public class StrategyPicker {
    
    public PackerStrategy pick(ProblemStatement PS){
        

        if(PS.getRectangleAmount()<10){
            return new PackCorners();
        }
        
        if(PS.getRectangleAmount()==25 || PS.getRectangleAmount()==10){
            if(PS.getContainerHeight()>0){
                return new PackLikeABeast();
            }else{
                return new PackLikeMultipleBeasts();
            }
        }
        
        if(PS.getRectangleAmount()>25){
            if(PS.getContainerHeight()>0){
                
                return new PackLikeABeast();
                
            }else{

                return new PackNextToEachother();
            }
        }
        
        return new PackNextToEachother();
    } 
}
