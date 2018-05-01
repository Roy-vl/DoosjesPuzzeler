public class PackingSolver{
    
    public static void main(String[] args) {
        RectanglesContainer RC = new RectanglesContainer();
        RC.parseInput();
        
        RC.sortRectangles(new SortByArea());
     
        PackerStrategy strategy = RC.containerHeight>0 ? new PackTetris() : new PackLikeABeast();
                
        //new PackTetris();
        //new PackNextToEachother();
        //new PackLikeABeast() ;
        //new PackCornersExhaustiveRecursive();
        //new PackCorners());
        
        strategy.pack(RC);
        
        RC.printOutput();
        //RC.visualize();
    }
    
}
