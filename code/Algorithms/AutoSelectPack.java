
public class AutoSelectPack implements PackerStrategy {

    @Override
    public boolean applicable(ProblemStatement PS) {
        return true;
    }

    @Override
    public RectanglesContainer pack(ProblemStatement PS) {

        if (PS.getRectangleAmount() < 25) {
            System.out.println("Choosed BackTrackCornerPack");
            return new BacktrackCornerPack().pack(PS);
        }

        if (PS.getRectangleAmount() == 25) {
            if (PS.getContainerHeight() == 0) {
                System.out.println("Choosed MultipleGreedyTriviaBestFitPack");
                RectanglesContainer RCOne = new MultipleGreedyTrivialBestFitPack().pack(PS);
                
                return RCOne;
                /*
                RectanglesContainer RCTwo = new MultipleGreedyCornerPack().pack(PS);
                if (RCOne.getCost() > RCTwo.getCost()) {
                    return RCTwo;
                } else {
                    return RCOne;
                }
                 */
            } else {
                System.out.println("Choosed GreedyTriviaBestFitPack");
                RectanglesContainer RCOne = new GreedyTrivialBestFitPack().pack(PS);
                
                return RCOne;
                /*
                sRectanglesContainer RCTwo = new GreedyCornerPack().pack(PS);
                if (RCOne.getCost() > RCTwo.getCost()) {
                    return RCTwo;
                } else {
                    return RCOne;
                }
                 */
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
