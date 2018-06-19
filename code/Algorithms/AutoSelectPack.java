

public class AutoSelectPack implements PackerStrategy{
    
    @Override
    public boolean applicable(ProblemStatement PS){
        return true;
    }
 
    @Override
    public QuadTree pack(ProblemStatement PS){

        if(PS.getRectangleAmount()<25){
            System.out.println("Choosed BackTrackCornerPack");
            return new BackTrackCornerPack().pack(PS);
        }     
        
        if(PS.getContainerHeight()>0){
            System.out.println("Choosed GreedyCornerPack");
            return new GreedyCornerPack().pack(PS);

        }
        
        System.out.println("Choosed MultipleGreedyCornerPack");
        return new MultipleGreedyCornerPack().pack(PS);

    }
}