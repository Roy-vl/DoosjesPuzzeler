import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class GreedyCornerPack implements PackerStrategy{
    RectanglesContainer RC;
    
    int width;
    int height;
    boolean[][] filledSpots;
    
    ArrayList<Point> corners; 

    public boolean canBePlacedAt(Point P, Rectangle R){
        if(P.x+R.getWidth()>width || P.y+R.getHeight()>height) return false;//out of bounds
        for(int x = P.x; x < P.x+R.getWidth(); x++){
        for(int y = P.y; y < P.y+R.getHeight(); y++){
            if(filledSpots[x][y]){
                //First spot was filled, point is superfluous
                if(x==P.x && y == P.y){
                    corners.remove(P);
                }
                
                return false;
            }
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
    
    public void addCorner(Point C){
        if(C.x>=width || C.y>=height) return;//within bounds
        if(filledSpots[C.x][C.y]) return;//not already filled
        corners.add(C);
    }
    
    public void place(Point C, Rectangle R){
        R.px = C.x;
        R.py = C.y;
        
        Point newCor1 = new Point(
            R.px + R.getWidth(),
            R.py
        );

        Point newCor2 = new Point(
            R.px,
            R.py + R.getHeight()
        );

        RC.addRectangle(R);
        fillSpots(R);
        addCorner(newCor1);
        addCorner(newCor2);
        corners.remove(C);
    }
    
    @Override
    public RectanglesContainer pack(ProblemStatement PS){
        if(PS.getContainerHeight()==0){
            System.out.println("GreedyCornerPack is not applicable");
            return new RectanglesContainer();
        }
        
        RC = new RectanglesContainer();
        RC.setForcedBoundingHeight(PS.getContainerHeight());
        
        Rectangle[] rectangles = PS.getRectangles();
        
        
        height = PS.getContainerHeight();
        width = Math.max(PS.getMaxDimension(),PS.getRectanglesArea()/height*2);
        
        //Rotate rectangles if neccesary
        double relativeSize = (PS.getContainerHeight() / 20);
        int relativeS = (int) relativeSize;
        if(PS.getRotationAllowed()){
            for(Rectangle curRec : rectangles){           
                if((curRec.sy > curRec.sx && curRec.sy > relativeS) || curRec.sy > height){
                    curRec.rotated = true;
                }
            }
        }  

        filledSpots = new boolean[width][height];
      
        corners = new ArrayList<>();
        corners.add(new Point(0,0));

        Arrays.sort(rectangles,new SortByArea());
        Arrays.sort(rectangles,new SortByDecreasingWidth());
 
        for(Rectangle curRec : rectangles){
  
            boolean placed = false;
            
            Collections.sort(corners, new SortByLeftness());
     
            for(Point curCor : new ArrayList<>(corners)){
                curRec.px = curCor.x;
                curRec.py = curCor.y;
                
                if(canBePlacedAt(curCor,curRec)){
                    place(curCor,curRec);
                    placed = true;
                    break;       
                }
            }
            
            if(!placed){
                //System.out.println("RECTANGLE "+curRec.id+" COULD NOT BE PLACED BY PLACING IT IN A CORNER REVERTING TO OTHER (SLOW) METHODS");
                //System.out.println("Rectangle dimensions :"+curRec.getWidth()+","+curRec.getHeight());
                //System.out.println("Container dimensions :"+width+","+height);
                
                //basically just tries to find the first free spot in leftness order (just as in GreedyTrivialPack)
                Point P = corners.get(0);
                while(!placed){
                    if(canBePlacedAt(P,curRec)){
                        place(P,curRec);
                        placed = true;
                    }
                    P.y++;
                    if(P.y>=height){
                        P.x++;
                        P.y = 0;
                    }
                }            
                //System.out.println("RECTANGLE "+curRec.id+" COULD BE PLACED BY REVERTING TO OTHER (SLOW) METHODS");
            }
        }
        
        return RC;
    }
}