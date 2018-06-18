
import java.util.ArrayList;
import java.util.Arrays;

public class GreedyTrivialBestFitPack implements PackerStrategy {

    int width;
    int height;
    DoublyLinkedNode[][] spots;
    double fitRatio = 1.0;

    public boolean canBePlacedAt(int tx, int ty, Rectangle R) {
        if (tx + R.getWidth() > width || ty + R.getHeight() > height) {
            return false;//out of bounds
        }
        for (int x = tx; x < tx + R.getWidth(); x++) {
            for (int y = ty; y < ty + R.getHeight(); y++) {
                if (spots[x][y].filled) {
                    return false;
                }
            }
        }
        return true;
    }

    public void fillSpots(Rectangle R) {
        for (int x = R.px; x < R.px + R.getWidth(); x++) {
            for (int y = R.py; y < R.py + R.getHeight(); y++) {
                spots[x][y].filled = true;
            }
        }

        for (int x = R.px; x < R.px + R.getWidth(); x++) {
            spots[x][R.py].previous.next = spots[x][R.py + R.getHeight() - 1].next;
            spots[x][R.py + R.getHeight() - 1].next.previous = spots[x][R.py].previous;
        }
    }

    @Override
    public boolean applicable(ProblemStatement PS) {
        if (PS.getContainerHeight() == 0) {
            return false;
        }
        return true;
    }

    //helper method to find the best fitting rectangle
    public Rectangle findBestFit(ProblemStatement PS, DoublyLinkedNode[][] spots, RectanglesContainer RC, int x, int y, ArrayList<Rectangle> rects) {
        Rectangle bestRec = new Rectangle();
        int bestcost = Integer.MAX_VALUE;
        for (Rectangle curRec : rects) {
            //calculate the exces room after placement
            int rightSpace = 0;
            int bottomSpace = 0;
            for (int i = x + curRec.getWidth(); i < x + 50; i++) {
                if (!spots[i][y].filled) {
                    rightSpace++;
                }
            }
            for (int i = y + curRec.getHeight(); i < Math.min(y + curRec.getHeight() + 50, PS.getContainerHeight()); i++) {
                if (!spots[x][i].filled) {
                    bottomSpace++;
                }
            }

            //remember the "tightest" fit with the current ratio
            if (rightSpace + fitRatio * bottomSpace < bestcost) {
                if (canBePlacedAt(x, y, curRec)) {
                    bestcost = rightSpace + (int) fitRatio * bottomSpace;
                    bestRec = curRec;
                }
            }
        }
        return bestRec;
    }

    @Override
    public RectanglesContainer pack(ProblemStatement PS) {

        RectanglesContainer bestRC = new RectanglesContainer();
        int bestCost = Integer.MAX_VALUE;
                
        //try packing for differnt ratio of importance between hight and width
        for (double ratio = 0.5; ratio <= 2.5; ratio = ratio + 0.05) {
            fitRatio = ratio;
            width = 10000;
            height = PS.getContainerHeight();

            RectanglesContainer RC = new RectanglesContainer();
            RC.setForcedBoundingHeight(height);

            spots = new DoublyLinkedNode[width][height];

            //link the list
            DoublyLinkedNode begin = new DoublyLinkedNode(0, -1);
            DoublyLinkedNode previousnode = begin;
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    spots[x][y] = new DoublyLinkedNode(x, y);

                    previousnode.next = spots[x][y];
                    spots[x][y].previous = previousnode;

                    previousnode = spots[x][y];
                }
            }

            //prepare the rectangles
            Rectangle[] rectangles = PS.getRectangles();

            ArrayList<Rectangle> toPlace = new ArrayList();
            for (Rectangle curRec : rectangles) {
                toPlace.add(curRec);
            }

            //Rotate rectangles if neccesary
            double relativeSize = (PS.getContainerHeight() / 20);
            int relativeS = (int) relativeSize;
            if (PS.getRotationAllowed()) {
                for (Rectangle curRec : rectangles) {
                    if ((curRec.sy > curRec.sx && curRec.sy > relativeS) || curRec.sy > height || curRec.sx > width) {
                        curRec.rotated = true;
                    }
                }
            }

            //actual placing of rectangles
            for (int i = 0; i < rectangles.length; i++) {
                boolean placed = false;
                DoublyLinkedNode n = begin.next;

                //loop over all point until a rect can be placed
                while (!placed) {
                    //make a listof rectangles to fit in the slot
                    ArrayList<Rectangle> rectanglefits = new ArrayList<>();
                    for (Rectangle curRec : toPlace) {
                        if (canBePlacedAt(n.x, n.y, curRec)) {
                            rectanglefits.add(curRec);
                        }
                    }

                    //select the best "fit"
                    Rectangle bestFit = findBestFit(PS, spots, RC, n.x, n.y, rectanglefits);

                    //if it can be placed place it
                    if (canBePlacedAt(n.x, n.y, bestFit) && !(bestFit.sx == 0 || bestFit.sy == 0)) {
                        bestFit.px = n.x;
                        bestFit.py = n.y;
                        fillSpots(bestFit);
                        RC.addRectangle(bestFit);
                        //RC.visualize();
                        placed = true;
                        toPlace.remove(bestFit);
                    }
                    n = n.next;
                }

                if (!placed) {
                }
            }
            if (RC.getCost() < bestCost){
                bestRC = RC;
                bestCost = RC.getCost();
            }
        }
        return bestRC;
    }
}
