public class RectanglePacking {
    
    public static void main(String[] args) {
        RectanglesContainer RC = new RectanglesContainer();
        RC.parseInput();
        RC.packNextToEachother();
        RC.printOutput();
        RC.visualize();
    }
    
}
