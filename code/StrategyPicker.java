/*
StrategyPicker is a class that will pick and return an appropriate PackerStrategy for any ProblemStatement
*/

public class StrategyPicker {
    
    public PackerStrategy pick(ProblemStatement PS){
        
        //return new PackGuillotine();
        return new TestQuadTree();

        if(PS.getRectangleAmount()<=25){
            return new PackCorners();
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
