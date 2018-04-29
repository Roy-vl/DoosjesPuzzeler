public class RectanglePacking {
    
    public static void main(String[] args) {
        RectanglesContainer RC = new RectanglesContainer();
        RC.parseInput();
        
        RC.sortRectangles(new SortByArea());
        
        PackerStrategy strategy = RC.containerHeight>0 ? new PackTetris() : (RC.rectangleAmount>25 ? new PackNextToEachother() : new PackCorners());
        
        strategy.pack(RC);
        
        RC.printOutput();
        RC.visualize();
    }
    
}
