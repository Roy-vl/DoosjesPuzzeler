
import java.util.ArrayList;
import java.util.Arrays;

public class PackBuckets implements PackerStrategy{
    
    @Override
    public RectanglesContainer pack(ProblemStatement PS){
        
        Rectangle[] rectangles = PS.getRectangles();
        ArrayList<ArrayList<Rectangle>> buckets = (new Bucketeer()).toBuckets(rectangles);
        
        RectanglesContainer RC = new RectanglesContainer();
        int x=0;
        for (Rectangle curRec : buckets.get(0)){
            curRec.px = x;
            curRec.rotated = PS.getRotationAllowed() && ( curRec.sy>PS.getContainerHeight() || (curRec.sx>curRec.sy && curRec.sx<=PS.getContainerHeight()) );       
            x += curRec.getWidth();
            RC.addRectangle(curRec);
        }
        RC.visualize();
        
        RC = new RectanglesContainer();
        x=0;
        for (Rectangle curRec : buckets.get(1)){
            curRec.px = x;
            curRec.rotated = PS.getRotationAllowed() && ( curRec.sy>PS.getContainerHeight() || (curRec.sx>curRec.sy && curRec.sx<=PS.getContainerHeight()) );       
            x += curRec.getWidth();
            RC.addRectangle(curRec);
        }
        RC.visualize();
        
        RC = new RectanglesContainer();
        x=0;
        for (Rectangle curRec : buckets.get(2)){
            curRec.px = x;
            curRec.rotated = PS.getRotationAllowed() && ( curRec.sy>PS.getContainerHeight() || (curRec.sx>curRec.sy && curRec.sx<=PS.getContainerHeight()) );       
            x += curRec.getWidth();
            RC.addRectangle(curRec);
        }
        RC.visualize();
        
        RC = new RectanglesContainer();
        x=0;
        for (Rectangle curRec : buckets.get(3)){
            curRec.px = x;
            curRec.rotated = PS.getRotationAllowed() && ( curRec.sy>PS.getContainerHeight() || (curRec.sx>curRec.sy && curRec.sx<=PS.getContainerHeight()) );       
            x += curRec.getWidth();
            RC.addRectangle(curRec);
        }
        RC.visualize();
        
        return RC;
    }
}

