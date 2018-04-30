public class RectanglePacking {
    
    public static void main(String[] args) {
        RectanglesContainer RC = new RectanglesContainer();
        RC.parseInput();
        
        RC.sortRectangles(new SortByArea());
        
        
        
        PackerStrategy strategy = 
                
        //new PackTetris();
        //new PackNextToEachother();
        new PackLikeABeast() ;
        //new PackCornersExhaustiveRecursive();
        //new PackCorners());
        
        strategy.pack(RC);
        
        RC.printOutput();
        RC.visualize();
    }
    
}
