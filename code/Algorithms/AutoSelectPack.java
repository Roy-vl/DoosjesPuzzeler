

public class AutoSelectPack implements PackerStrategy{
    
    @Override
    public boolean applicable(ProblemStatement PS){
        return true;
    }
 
    @Override
    public RectanglesContainer pack(ProblemStatement PS){

        if (PS.getRectangleAmount() < 25) {
            System.out.println("Choosed BackTrackCornerPack");
            return new BacktrackCornerPack().pack(PS);
        }   
        
        if (PS.getRectangleAmount() == 25) {
            if (PS.getContainerHeight() == 0) {
                System.out.println("Choosed MultipleGreedyTriviaBestFitPack vs. MultipleGreedyCornerPack");
                RectanglesContainer RCTwo = new MultipleGreedyCornerPack().pack(PS);
                RectanglesContainer RCOne = new MultipleGreedyTrivialBestFitPack().pack(PS);
                if (RCOne.getCost() > RCTwo.getCost()) {
                    return RCTwo;
                } else {
                    return RCOne;
                }
            } else {
                System.out.println("Choosed GreedyTriviaBestFitPack vs. GreedyCornerPack");
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
            System.out.println("Choosed MultipleGreedyCornerPack");
            return new MultipleGreedyCornerPack().pack(PS);
        }

        System.out.println("Choosed GreedyCornerPack");
        return new GreedyCornerPack().pack(PS);
    }
}