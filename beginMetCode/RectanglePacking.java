public class RectanglePacking {
    
    public static void main(String[] args) {
        RectanglesContainer rC = new RectanglesContainer();
        rC.parseInput();
        rC.packNextToEachother();
        rC.printOutput();
        rC.visualize();
    }
    
}
