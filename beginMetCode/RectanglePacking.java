public class RectanglePacking {
    
    public static void main(String[] args) {
        RectanglesContainer RC = new RectanglesContainer();
        RC.parseInput();
        
        PackerStrategy strategy;
        if(RC.containerHeight>0){
            strategy = new PackTetris();
        }else{
            strategy = new PackNextToEachother();
        }
        
        RC.pack(strategy);
        RC.printOutput();
        RC.visualize();
    }
    
}
