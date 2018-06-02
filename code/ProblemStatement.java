
import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Comparator;
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
    
    public ProblemStatement getSortedProblemStatement(Comparator<Rectangle> comparator){
        Rectangle[] sortedrectangles = getRectangles();
        Arrays.sort(sortedrectangles,comparator);   
        
        return new ProblemStatement(
           containerHeight,
           rotationAllowed,
           rectangleAmount,
           rectanglesArea,
           maxDimension,
           sortedrectangles
        );
    }
    
    //DEEP COPY OF RECTANGLES, DO NOT TOUCH
    public Rectangle[] getRectangles(){
        Rectangle[] clone = new Rectangle[rectangleAmount];
        for(int i=0;i<rectangleAmount;i++) clone[i] = rectangles[i].clone();
        return clone;
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
    
    public void generateRandomInput(int rectangleAmount, Boolean rotationAllowed, int containerHeight, int maxWidth, int maxHeight){
        this.rotationAllowed = rotationAllowed;
        this.containerHeight = containerHeight;
        this.rectangleAmount = rectangleAmount;
        rectangles = new Rectangle[rectangleAmount];
        
        for(int i = 0; i < rectangleAmount; i++){
            Rectangle newRectangle = new Rectangle();
            newRectangle.sx = (int)(Math.random()*maxWidth)+1;
            newRectangle.sy = (int)(Math.random()*maxHeight)+1;
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
