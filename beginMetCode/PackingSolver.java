
import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PackingSolver {

    public static void main(String[] args) {
        RectanglesContainer RC = new RectanglesContainer();
        
        FileDialog dialog = new FileDialog((Frame) null, "Select File to Open");
        dialog.setMultipleMode(true);
        dialog.setMode(FileDialog.LOAD);
        dialog.setVisible(true);
        File[] files = dialog.getFiles();
        if (files.length != 0 && files != null) {
            for (File file : files) {
                try {
                    System.out.println(file + " chosen.");
                    Scanner scanner = new Scanner(file);
                    
                    RC.parseInput(scanner);
                    
                    RC.sortRectangles(new SortByPackingScore());

                    
                    PackerStrategy strategy = (new StrategyPicker()).pick(RC);
                    System.out.println("Applying "+strategy.getClass().getSimpleName());
                    
                    long startTime = System.currentTimeMillis();
                    strategy.pack(RC);
                    long estimatedTime = System.currentTimeMillis() - startTime;

                    System.out.println("time passed = "+estimatedTime+"ms");
                    RC.visualize();
                    
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(PackingSolver.class.getName()).log(Level.SEVERE, null, ex);
                }     
            }
        } else {
            System.out.println("No file selected");
        } 
    }
    
}
