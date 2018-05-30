
import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
ProblemStatement is purely used for parsing the input and storing that data.
NO changing is allowed.
*/

public class ProblemStatement{
    private int         containerHeight;
    private boolean     rotationAllowed;
    private int         rectangleAmount;
    private int         rectanglesArea;
    private int         maxDimension;
    private Rectangle[] rectangles;
    
    public ProblemStatement(){
        containerHeight = 0;
        rotationAllowed = false;
        rectangleAmount = 0;
        rectanglesArea = 0;
        maxDimension = 0 ;
        rectangles = new Rectangle[rectangleAmount];
    }
    
    public ProblemStatement(int _containerHeight, boolean _rotationAllowed, int _rectangleAmount, int _totalRectangleArea, int _maxDimension, Rectangle[] _rectangles){
        containerHeight = _containerHeight;
        rotationAllowed = _rotationAllowed;
        rectangleAmount = _rectangleAmount;
        rectanglesArea = _totalRectangleArea;
        maxDimension = _maxDimension;
        rectangles = _rectangles;
    }
    
    public int getContainerHeight(){
        return containerHeight;
    }
    
    public boolean getRotationAllowed(){
        return rotationAllowed;
    }
    
    public int getRectangleAmount(){
        return rectangleAmount;
    }
    
    public int getRectanglesArea(){
        return rectanglesArea;
    }
    
    public int getMaxDimension(){
        return maxDimension;
    }
    
    public Rectangle[] getRectangles(){
        return rectangles.clone();
    }
    
    public void parseInput(Scanner scanner){
        //refresh
        containerHeight = 0;
        rotationAllowed = false;
        rectangleAmount = 0;
        rectanglesArea = 0;
        maxDimension = 0;
        rectangles = new Rectangle[rectangleAmount];
        
        //parse containerheight
        scanner.next();
        scanner.next();
        if(scanner.next().equals("fixed")){
            containerHeight = scanner.nextInt();
        }else{
            containerHeight = 0;
        }
        
        //parse rotation
        scanner.next();
        scanner.next();
        rotationAllowed = scanner.next().equals("yes");
        
        //parse rectangle amount
        scanner.next();
        scanner.next();
        scanner.next();
        rectangleAmount = scanner.nextInt();

        //parse rectangles
        rectangles = new Rectangle[rectangleAmount];
        for(int i=0;i<rectangleAmount;i++){
            Rectangle newRectangle = new Rectangle();
            newRectangle.sx = scanner.nextInt();
            newRectangle.sy = scanner.nextInt();
            newRectangle.id = i;
            rectangles[i] = newRectangle;
            rectanglesArea += newRectangle.getArea();
            maxDimension = Math.max(maxDimension, Math.max(newRectangle.sx,newRectangle.sy));
        }
    }
    
    public void fromFile(){    
        FileDialog dialog = new FileDialog((Frame) null, "Select File to Open");
        dialog.setMultipleMode(true);
        dialog.setMode(FileDialog.LOAD);
        dialog.setVisible(true);
        File[] files = dialog.getFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                    
                try {
                    System.out.println(file + " chosen.");
                    parseInput(new Scanner(file));

                } catch (FileNotFoundException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                   
            }
        } else {
            System.out.println("No file chosen");
        }
    }
    
    public void generateRandomInput(int size, Boolean rotationAllowed, int containerHeight){
        this.rotationAllowed = rotationAllowed;
        this.containerHeight = containerHeight;
        this.rectangleAmount = size;
        rectangles = new Rectangle[rectangleAmount];
        for(int i = 0; i < rectangleAmount; i++){
            Rectangle newRectangle = new Rectangle();
            newRectangle.sx = (int)(Math.random()*100)+1;
            newRectangle.sy = (int)(Math.random()*100)+1;
            newRectangle.id = i;
            rectangles[i] = newRectangle;
            rectanglesArea += newRectangle.getArea();
        }
    }
    
    public void print(){
        //print output
        System.out.println("container height: "+(containerHeight==0?"free":("fixed "+containerHeight)));
        System.out.println("rotations allowed: "+(rotationAllowed?"yes":"no"));
        System.out.println("number of rectangles: "+rectangleAmount);
        for (Rectangle curRec : rectangles) {
            System.out.println(curRec.sx+" "+curRec.sy);
        }   
    }
}
