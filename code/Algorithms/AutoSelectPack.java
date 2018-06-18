

public class AutoSelectPack implements PackerStrategy{
    
    @Override
    public boolean applicable(ProblemStatement PS){
        return true;
    }
 
    @Override
    public RectanglesContainer pack(ProblemStatement PS){

        if (PS.getRectangleAmount() < 25) {
            return new BacktrackCornerPack().pack(PS);
        }   
        
        if (PS.getRectangleAmount() == 25) {
            if (PS.getContainerHeight() == 0) {
                RectanglesContainer RCTwo = new MultipleGreedyCornerPack().pack(PS);
                RectanglesContainer RCOne = new MultipleGreedyTrivialBestFitPack().pack(PS);
                if (RCOne.getCost() > RCTwo.getCost()) {
                    return RCTwo;
                } else {
                    return RCOne;
                }
            } else {
                RectanglesContainer RCOne = new GreedyTrivialBestFitPack().pack(PS);
                RectanglesContainer RCTwo = new GreedyCornerPack().pack(PS);
                if (RCOne.getCost() > RCTwo.getCost()) {
                    return RCTwo;
                } else {
                    return RCOne;
                }
            }
        }
        
        if (PS.getContainerHeight() == 0) {
            return new MultipleGreedyCornerPack().pack(PS);
        }

        return new GreedyCornerPack().pack(PS);
    }
}