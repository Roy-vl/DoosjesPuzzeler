
import java.util.ArrayList;
import java.util.Arrays;

public class PackBuckets implements PackerStrategy{
    
    @Override
    public RectanglesContainer pack(ProblemStatement PS){
        
        RectanglesContainer RC = new RectanglesContainer();
        
        Rectangle[] rectangles = PS.getRectangles();
        ArrayList<ArrayList<Rectangle>> buckets = (new Bucketeer()).toBuckets(rectangles);
        
        for(ArrayList<Rectangle> bucket : buckets){
        
            RC = new RectanglesContainer();
            int x=0;
            for (Rectangle curRec : bucket){
                curRec.px = x;
                x += curRec.getWidth();
                RC.addRectangle(curRec);
            }
            RC.visualize();
            
        }
        
        return RC;
    }
}

