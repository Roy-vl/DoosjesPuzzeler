public class StrategyPicker {
    
    public PackerStrategy pick(RectanglesContainer RC){
        
        if(RC.rectangleAmount<10){
            if(RC.containerHeight>0){
                return new PackCornersExhaustiveRecursive();
            }else{
                return new PackLikeMultipleBeasts();
            }
        }
        
        if(RC.rectangleAmount==25 || RC.rectangleAmount==10){
            if(RC.containerHeight>0){
                return new PackLikeABeast();
            }else{
                return new PackLikeMultipleBeasts();
            }
        }
        
        if(RC.rectangleAmount>25){
            if(RC.containerHeight>0){
                return new PackTetris();
                
            }else{
               
                int minimumHeight = 0;
                for(Rectangle curRec : RC.rectangles) if(minimumHeight < curRec.getHeight()) minimumHeight = curRec.getHeight();
                RC.containerHeight = minimumHeight;
      
                return new PackTetris();
            }
        }
        
        return new PackNextToEachother();
    } 
}