
import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PackingSolver {

    public static void main(String[] args) {
        FileDialog dialog = new FileDialog((Frame) null, "Select File to Open");
        dialog.setMultipleMode(true);
        dialog.setMode(FileDialog.LOAD);
        dialog.setVisible(true);
        File[] files = dialog.getFiles();
        for (File file : files) {
            try {
                System.out.println(file + " chosen.");
                Scanner scanner = new Scanner(file);

                ProblemStatement PS = new ProblemStatement();
                PS.parseInput(scanner);

                PackerStrategy strategy = (new StrategyPicker()).pick(PS);
                System.out.println("Applying "+strategy.getClass().getSimpleName()); 
                
                long startTime = System.currentTimeMillis();
                RectanglesContainer packedRC = strategy.pack(PS);
                long estimatedTime = System.currentTimeMillis() - startTime;
                System.out.println("Packing time : "+estimatedTime+"ms");
                
                PS.print();
                packedRC.printPlacement(PS.getRotationAllowed());

                packedRC.visualize();

            } catch (FileNotFoundException ex) {
                Logger.getLogger(PackingSolver.class.getName()).log(Level.SEVERE, null, ex);
            }     
        }
    }
    
}
