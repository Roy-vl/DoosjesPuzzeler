public class RectanglePacking {
    
    public static void main(String[] args) {
        RectanglesContainer RC = new RectanglesContainer();
        RC.parseInput();
        RC.randomizePositions();
        RC.printOutput();
        RC.visualize();
    }
    
}
