public class RectanglePacking {
    
    public static void main(String[] args) {
        RectanglesContainer RC = new RectanglesContainer();
        RC.parseInput();
        
        if(RC.ContainerHeight>0){
            RC.tetrisPack();
        }else{
            RC.packNextToEachother();
        }
        
        RC.printOutput();
        RC.visualize();
    }
    
}
