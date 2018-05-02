public class PackingSolver{
    
    public static void main(String[] args) {
        RectanglesContainer RC = new RectanglesContainer();
        RC.parseInput();
        
        RC.sortRectangles(new SortByPackingScore());
     
        //PackerStrategy strategy = RC.containerHeight>0 ? new PackTetris() : new PackLikeABeast();
                
        //new PackTetris();
        //new PackNextToEachother();
        //new PackLikeABeast() ;
        PackerStrategy strategy = new PackCornersExhaustiveRecursive();
        //new PackCorners());
        
        strategy.pack(RC);
        
        RC.printOutput();
        RC.visualize();
    }
    
}
