
import java.util.Arrays;

public class PackLikeABeast implements PackerStrategy{
    
    boolean[][] filledSpots;
    
    public boolean canBePlacedAt(int tx, int ty, Rectangle R){
        for(int x = tx; x < tx+R.getWidth(); x++){
        for(int y = ty; y < ty+R.getHeight(); y++){
            if(filledSpots[x][y]) return false;
        }
        }
        return true;
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
  
        int mx = 0;
        int my = 0;
        
        for(Rectangle curRec : rectangles){
            
            curRec.rotated = curRec.sy > PS.getContainerHeight() && PS.getRotationAllowed();

            //find the earliest open spot
            while(filledSpots[mx][my]){
                my++;
                if(my >= PS.getContainerHeight()){
                    my = 0;
                    mx++;
                }
            }
            
            boolean placed = false;
            int tx = mx;
            int ty = my;
            
            while(!placed){
                if(ty > PS.getContainerHeight()-curRec.getHeight()){
                    tx++;
                    ty = 0;
                }
                if(tx>=Width) break;
       
                if(canBePlacedAt(tx,ty,curRec)){
                    curRec.px = tx;
                    curRec.py = ty;
                    fillSpots(curRec);
                    RC.addRectangle(curRec);
                    placed = true;
                }
                
                ty++;
            }
            
            if(!placed) System.out.println("RECTANGLE "+curRec.id+" COULD NOT BE PLACED");
        }
        
        return RC;
    }
}