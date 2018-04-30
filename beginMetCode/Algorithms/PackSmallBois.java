
public class PackSmallBois implements PackerStrategy {

    @Override
    public void pack(Rectangle[] rectangles, boolean rotationAllowed, int containerHeight) {
        int initWidth = 0;
        for (Rectangle rectangle : rectangles) {
            initWidth += rectangle.getWidth();
        }
        int i = 0;
        boolean[][] grid = new boolean[containerHeight][initWidth];
        rectanglePropper(rectangles, rotationAllowed, containerHeight, i, grid);
    }

    private static void rectanglePropper(Rectangle[] rectangles, boolean rotationAllowed, int containerHeight, int i, boolean[][] grid) {
        rectangles[i].px = 0;
        rectangles[i].py = 0;
        if (i > 0) {
            int y = rectangles[i - 1].getHeight() + rectangles[i - 1].py;
            if (y + rectangles[i].getHeight() > containerHeight) {
                int lowWidth = 0;
                for (Rectangle rectangle : rectangles) {
                    if (rectangle.py == 0) {
                        lowWidth += rectangle.getWidth();
                    }
                }
                rectangles[i].px = lowWidth;
            } else {
                rectangles[i].py = y;
            }
        }
        i++;
        System.out.println(i + " " + rectangles.length);
        if (i < rectangles.length) {
            rectanglePropper(rectangles, rotationAllowed, containerHeight, i, grid);
        }
    }
}

/**
 * container height: fixed 20 rotations allowed: no number of rectangles: 3 3 98
 * 9 11 3
 *
 */
