public class RectanglePacking {
    
    public static void main(String[] args) {
        RectanglesContainer RC = new RectanglesContainer();
        RC.parseInput();
        
        PackerStrategy strategy = RC.containerHeight>0 ? new PackTetris() : new PackNextToEachother();
        
        strategy.pack(RC);
        
        RC.printOutput();
        RC.visualize();
    }
    
}
