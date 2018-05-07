public class StrategyPicker {
    
    public PackerStrategy pick(RectanglesContainer RC){
        
        if(RC.rectangleAmount<=10){
            return new PackCornersExhaustiveRecursive();
        }
        
        if(RC.rectangleAmount==25){
            if(RC.containerHeight>0){
                return new PackLikeABeast();
            }else{
                return new PackLikeMultipleBeasts();
            }
        }
        
        if(RC.rectangleAmount>25){
            if(RC.containerHeight>0){
                return new PackLikeABeast();
                
            }else{

                return new PackNextToEachother();
            }
        }
        
        return new PackNextToEachother();
    } 
}
