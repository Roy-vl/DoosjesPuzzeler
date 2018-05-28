
import java.util.Arrays;

public class PackNextToEachother implements PackerStrategy{
    
    @Override
    public RectanglesContainer pack(ProblemStatement PS){
        RectanglesContainer RC = new RectanglesContainer();
        
        Rectangle[] rectangles = PS.getRectangles();
        Arrays.sort(rectangles,new SortByDecreasingWidth());
        
        int x=0;
        for (Rectangle curRec : rectangles){
            curRec.px = x;    
            x += curRec.getWidth();
            RC.addRectangle(curRec);
        }
        
        return RC;
    }
}

