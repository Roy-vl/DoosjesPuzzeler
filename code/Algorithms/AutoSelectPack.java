
import java.util.Arrays;

public class AutoSelectPack implements PackerStrategy {

    @Override
    public boolean applicable(ProblemStatement PS) {
        return true;
    }

    @Override
    public RectanglesContainer pack(ProblemStatement PS) {
        double relativeSize = (PS.getContainerHeight() / 20);
        int relativeS = (int) relativeSize;
        Rectangle[] rectangles = PS.getRectangles();
        if (PS.getRotationAllowed()) {
            for (Rectangle curRec : rectangles) {
                if ((curRec.sy > curRec.sx && curRec.sy > relativeS) || (curRec.sy > PS.getContainerHeight())) {
                    curRec.rotated = true;
                }
            }
        }

        if (PS.getRectangleAmount() < 25) {
            System.out.println("Choosed BackTrackCornerPack");
            return new BacktrackCornerPack().pack(PS);
        }

        RectanglesContainer bestRC = new RectanglesContainer();
        int bestCost = Integer.MAX_VALUE;
        RectanglesContainer curRC;

        for (int sorting = 1; sorting <= 3; sorting++) {
            switch (sorting) {
                case 1:
                    Arrays.sort(rectangles, new SortByArea());
                    break;
                case 2:
                    Arrays.sort(rectangles, new SortByArea());
                    Arrays.sort(rectangles, new SortByDecreasingWidth());
                    break;
                case 3:
                    Arrays.sort(rectangles, new SortByArea());
                    Arrays.sort(rectangles, new SortByDecreasingHeight());
                    break;
            }

            ProblemStatement sortedPS = new ProblemStatement(
                    PS.getContainerHeight(),
                    PS.getRotationAllowed(),
                    PS.getRectangleAmount(),
                    PS.getRectanglesArea(),
                    PS.getMaxDimension(),
                    rectangles
            );

            if (PS.getContainerHeight() > 0 && PS.getRectangleAmount() > 25) {
                System.out.println("Choosed GreedyCornerPack, with sorting: " + sorting);
                curRC = new GreedyCornerPack().pack(sortedPS);
            } else if (PS.getContainerHeight() > 0 && PS.getRectangleAmount() == 25) {
                System.out.println("Choosed GreedyTrivialPack, with sorting: " + sorting);
                curRC = new GreedyTrivialPack().pack(sortedPS);
            } else if (PS.getRectangleAmount() > 25) {
                System.out.println("Choosed MultipleGreedyCornerPack, with sorting: " + sorting);
                curRC = new MultipleGreedyCornerPack().pack(sortedPS);
            } else {
                System.out.println("Choosed MultipleTrivialCornerPack, with sorting: " + sorting);
                curRC = new MultipleGreedyTrivialPack().pack(sortedPS);
            }

            if (curRC.getCost() < bestCost) {
                bestCost = curRC.getCost();
                bestRC = curRC;
                System.out.println("sorting: " + sorting + " gave cost: " + bestCost);
            }
        }
        return bestRC;
    }
}
