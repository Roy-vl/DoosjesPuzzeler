

public class AutoSelectPack implements PackerStrategy{
 
    @Override
    public RectanglesContainer pack(ProblemStatement PS){

        if(PS.getRectangleAmount()<25){
            System.out.println("Choosed BackTrackCornerPack");
            return new BacktrackCornerPack().pack(PS);
        }     
        
        if(PS.getContainerHeight()>0){
            System.out.println("Choosed GreedyCornerPack");
            return new GreedyCornerPack().pack(PS);

        }
        
        System.out.println("Choosed MultipleGreedyCornerPack");
        return new MultipleGreedyCornerPack().pack(PS);

    }
}