public class RectanglePacking {
    
    public static void main(String[] args) {
        RectanglesContainer rC = new RectanglesContainer();
        rC.parseInput();
        
        PackerStrategy strategy;
        if(rC.containerHeight>0){
            strategy = new PackTetris();
        }else{
            strategy = new PackNextToEachother();
        }
        
        rC.pack(strategy);
        rC.printOutput();
        rC.visualize();
    }
}
