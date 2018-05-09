
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

        //Commend this line out for Momotor Submission
        getFile(RC);

        Scanner scanner = new Scanner(System.in);
        RC.parseInput(scanner);
        getSolution(RC);
    }

    public static void getFile(RectanglesContainer RC) {
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
                    getSolution(RC);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(PackingSolver.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            Scanner scanner = new Scanner(System.in);
            RC.parseInput(scanner);
            getSolution(RC);
        }
    }


public static void getSolution(RectanglesContainer RC) {
         RC.sortRectangles(new SortByPackingScore());

        long startTime = System.currentTimeMillis();
        (new StrategyPicker()).pick(RC).pack(RC);
        long estimatedTime = System.currentTimeMillis() - startTime;

        RC.printOutput();
        System.out.println("----------------------------------------------------");

        //System.out.println();
        //System.out.println("time passed = "+estimatedTime+"ms");
        RC.visualize();
        System.out.println(RC.getBoundingWidth());
    }
}
