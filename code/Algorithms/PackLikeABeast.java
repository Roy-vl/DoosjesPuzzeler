
import java.util.Arrays;

public class PackLikeABeast implements PackerStrategy{
    
    boolean[][] filledSpots;
    
    public boolean canBePlacedAt(int tx, int ty, Rectangle R){
        boolean canBePlaced = true;
        for(int x = tx; x < tx+R.getWidth() && canBePlaced; x++){
        for(int y = ty; y < ty+R.getHeight() && canBePlaced; y++){
            canBePlaced = canBePlaced && !filledSpots[x][y];
        }
        }
        return canBePlaced;
    }
    
    public void fillSpots(Rectangle R){
        for(int x = R.px; x < R.px+R.getWidth(); x++){
        for(int y = R.py; y < R.py+R.getHeight(); y++){
            filledSpots[x][y] = true;
        }
        }   
    }
    
    @Override
    public RectanglesContainer pack(ProblemStatement PS){
        
        RectanglesContainer RC = new RectanglesContainer();
        RC.setForcedBoundingHeight(PS.getContainerHeight());
        
        int Width = 10000;

        filledSpots = new boolean[Width][PS.getContainerHeight()];
        
        //create a clone of the PS rectangles
        Rectangle[] rectangles = PS.getRectangles();
        Arrays.sort(rectangles,new SortByArea());
  
        for(Rectangle curRec : rectangles){
            if(curRec.sy > PS.getContainerHeight() && PS.getRotationAllowed()){
                curRec.rotated = true;
            }
            boolean placed = false;
            for(int tx = 0; tx <= Width                   - curRec.getWidth()  && !placed ; tx++){
            for(int ty = 0; ty <= PS.getContainerHeight() - curRec.getHeight() && !placed; ty++){
                
                if(canBePlacedAt(tx,ty,curRec)){
                    curRec.px = tx;
                    curRec.py = ty;
                    fillSpots(curRec);
                    RC.addRectangle(curRec);
                    placed = true;
                    break;
                }
            }
            }
            if(!placed) System.out.println("RECTANGLE "+curRec.id+" COULD NOT BE PLACED");
        }
        
        return RC;
    }
}