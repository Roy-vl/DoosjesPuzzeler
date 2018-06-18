
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import static java.lang.Math.max;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javax.swing.JFrame;

public class RectanglesContainer{
    private ArrayList<Rectangle> rectangles;
    private int boundingWidth;
    private int boundingHeight;
    private int forcedBoundingWidth;
    private int forcedBoundingHeight;
    private int rectanglesArea;
    
    public RectanglesContainer(){
        rectangles = new ArrayList<>();
        boundingWidth = 0;
        boundingHeight = 0;
        forcedBoundingWidth = 0;
        forcedBoundingHeight = 0;
    }
    
    @Override
    public RectanglesContainer clone(){
        RectanglesContainer clone = new RectanglesContainer();
        for(Rectangle curRec : rectangles) clone.rectangles.add(curRec.clone());
        clone.boundingWidth = boundingWidth;
        clone.boundingHeight = boundingHeight;
        clone.forcedBoundingWidth = forcedBoundingWidth;
        clone.forcedBoundingHeight = forcedBoundingHeight;
        clone.rectanglesArea = rectanglesArea;
        return clone;
    }
    
    public void setForcedBoundingWidth(int w){
        forcedBoundingWidth = w;
    }
    
    public void setForcedBoundingHeight(int h){
        forcedBoundingHeight = h;
    }
    
    public int getBoundingWidth(){
        return forcedBoundingWidth>0?forcedBoundingWidth:boundingWidth;
    }
    
    public int getBoundingHeight(){
        return forcedBoundingHeight>0?forcedBoundingHeight:boundingHeight;
    }
    
    public int getBoundingArea(){
        return getBoundingWidth()*getBoundingHeight();
    }
    
    public int getRectanglesArea(){
        return rectanglesArea;
    }
    
    public int getCost(){
        return getBoundingArea()-getRectanglesArea();
    }
      
    public boolean checkCollision(Rectangle aRec){
        boolean collision = false;
        for(Rectangle curRec : rectangles) collision = collision || curRec.Collides(aRec);
        return collision;
    }
    
    public void addRectangle(Rectangle aRec){
        rectangles.add(aRec);
        boundingWidth  = max(boundingWidth, aRec.px+aRec.getWidth());
        boundingHeight = max(boundingHeight,aRec.py+aRec.getHeight());
        rectanglesArea += aRec.getArea();
    }
    
    public void removeRectangle(Rectangle aRec){
        rectangles.remove(aRec);
        
        boundingWidth  = 0;
        boundingHeight = 0;
        
        for(Rectangle curRec : rectangles){
            boundingWidth  = max(boundingWidth, curRec.px+curRec.getWidth());
            boundingHeight = max(boundingHeight,curRec.py+curRec.getHeight());
        }
        
        rectanglesArea -= aRec.getArea();
    }
    
    public void printPlacement(boolean rotationAllowed){
        Collections.sort(rectangles, new SortByID());
        System.out.println("placement of rectangles");
        for (Rectangle curRec : rectangles) {
            System.out.println((rotationAllowed?(curRec.rotated?"yes ":"no "):"")+curRec.px+" "+curRec.py);
        }
    }
    
}

