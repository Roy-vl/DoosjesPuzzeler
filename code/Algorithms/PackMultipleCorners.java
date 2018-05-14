
public class PackMultipleCorners implements PackerStrategy {

    @Override
    public RectanglesContainer pack(ProblemStatement PS) {

        PackerStrategy PLAB = new PackCorners();
        int i = 0;
        Rectangle[] rectangleSetClone = new Rectangle[PS.getRectangleAmount()];
        for (Rectangle curRec : PS.getRectangles()) {
            rectangleSetClone[i] = curRec;
            i++;
        }
        int y = 0;
        Rectangle[] rectangleSet6 = new Rectangle[6];
        while (y < PS.getRectangleAmount()) {

            if (y + 6 > PS.getRectangleAmount()) {
                int finalSet = PS.getRectangleAmount() - y;
                Rectangle[] rectangleSetFinal = new Rectangle[finalSet];
                for (int x = 0; x < finalSet; x++) {
                    rectangleSetFinal[x] = rectangleSetClone[y];
                   
                    System.out.println("rectangles " + rectangleSetFinal[x] + "and " + rectangleSetClone[y]);
                     y++;
                }
                ProblemStatement newPS = new ProblemStatement(
                        PS.getContainerHeight(),
                        PS.getRotationAllowed(),
                        rectangleSet6.length,
                        rectangleSet6
                );

                RectanglesContainer curRC = PLAB.pack(newPS);
            } else {

                for (int x = 0; x < 6; x++) {
                    rectangleSet6[x] = rectangleSetClone[y];
                    y++;
                    System.out.println("rectangles " + rectangleSet6[x] + "and " + rectangleSetClone[y]);
                }

                ProblemStatement newPS = new ProblemStatement(
                        PS.getContainerHeight(),
                        PS.getRotationAllowed(),
                        rectangleSet6.length,
                        rectangleSet6
                );

                RectanglesContainer curRC = PLAB.pack(newPS);
            }
        }
         //return curRC;
         return null;
    }
}

