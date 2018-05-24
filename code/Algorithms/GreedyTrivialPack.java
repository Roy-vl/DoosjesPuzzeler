
import java.util.Arrays;

public class GreedyTrivialPack implements PackerStrategy{

    int width;
    int height;
    boolean[][] filledSpots;
 
    public boolean canBePlacedAt(int tx, int ty, Rectangle R){
        if(tx+R.getWidth()>width || ty+R.getHeight()>height) return false;//out of bounds
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
        width = 10000;
        height = PS.getContainerHeight();
        
        RectanglesContainer RC = new RectanglesContainer();
        RC.setForcedBoundingHeight(height);

        filledSpots = new boolean[width][height];
 
        Rectangle[] rectangles = PS.getRectangles();
        if(PS.getRotationAllowed()){
            for(Rectangle curRec : rectangles) if(curRec.sy > curRec.sx) curRec.rotated = true;
        }
        Arrays.sort(rectangles,new SortByDecreasingWidth());
  
        int mx = 0;
        int my = 0;
              
        for(Rectangle curRec : rectangles){
   
            //find the earliest open spot
            while(filledSpots[mx][my]){
                my++;
                if(my >= height){
                    my = 0;
                    mx++;
                }
            }
            
            boolean placed = false;
            int tx = mx;
            int ty = my;
             
            while(!placed){
                //System.out.println(tx+","+ty);
                if(ty > height-curRec.getHeight()){
                    tx++;
                    ty = 0;
                }
                if(tx>=width) break;
       
                if(canBePlacedAt(tx,ty,curRec)){
                    curRec.px = tx;
                    curRec.py = ty;
                    fillSpots(curRec);
                    RC.addRectangle(curRec);
                    placed = true;     
                }
                
                ty++;
                
                
            }
             
            if(!placed){
                System.out.println("RECTANGLE "+curRec.id+" COULD NOT BE PLACED");
                System.out.println("Rectangle dimensions :"+curRec.sx+","+curRec.sy);
                System.out.println("Container dimensions :"+width+","+height);
            }

        }
         
        return RC;
    }
}