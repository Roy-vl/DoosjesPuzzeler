import java.util.Scanner;

public class PackingSolver {

    public static void main(String[] args) {
        ProblemStatement PS = new ProblemStatement();
        
        //Handle System.In input
        Scanner scanner = new Scanner(System.in);
        PS.parseInput(scanner);
        
        PackerStrategy strategy = (new AutoSelectPack());

        RectanglesContainer packedRC = strategy.pack(PS);
        
        PS.print();
        packedRC.printPlacement(PS.getRotationAllowed());
    }
}