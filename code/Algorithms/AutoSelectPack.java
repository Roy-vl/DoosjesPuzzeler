

public class AutoSelectPack implements PackerStrategy{
 
    @Override
    public RectanglesContainer pack(ProblemStatement PS){

        if(PS.getRectangleAmount()<25){
            return new BacktrackCornerPack().pack(PS);
        }     
        
        if(PS.getContainerHeight()>0){
                
            return new GreedyCornerPack().pack(PS);

        }

        return new MultipleGreedyCornerPack().pack(PS);

    }
}