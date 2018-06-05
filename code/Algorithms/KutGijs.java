import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class KutGijs implements PackerStrategy{
    RectanglesContainer RC;
    RectanglesContainer bestRC;
    int                 bestCost;
    
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
    
    public void removeRectangle(Rectangle R){
        for(int i = R.px; i < R.px+R.getWidth(); i++){
            for( int j = R.py; j < R.py+R.getHeight(); j++){
                filledSpots[i][j] = false;
            }
        }
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
    public boolean applicable(ProblemStatement PS){
        if(PS.getContainerHeight()==0){
            return false;
        }   
        return true;
    }
    
    
    @Override
    public RectanglesContainer pack(ProblemStatement PS){
        bestRC = null;
        bestCost = Integer.MAX_VALUE;
        
        height = PS.getContainerHeight();
        width = Math.max(PS.getMinPosContainerWidth(),PS.getRectanglesArea()/height*3);
        Rectangle[] rectangles = PS.getRectangles();
        
        for( int r = 0; r < rectangles.length; r++){
            if( rectangles[r].getWidth() <= height){
                rectangles[r].rotated = true;
            }
            
           
        for( int a = r; a < rectangles.length; a++){
             
            
            System.out.println("1");
            
            if( rectangles[a].getWidth() <= height){
                rectangles[a].rotated = true;
            }
                
               
            
           
            
        
            
            RC = new RectanglesContainer();
            RC.setForcedBoundingHeight(PS.getContainerHeight());
            
            filledSpots = new boolean[width][height];
            corners = new ArrayList<>();
            corners.add(new Point(0,0));
            
            //Rotate rectangles if neccesary
            double relativeSize = (PS.getContainerHeight() / 20);
            int relativeS = (int) relativeSize;
             

            Arrays.sort(rectangles,new SortByArea());
            Arrays.sort(rectangles,new SortByDecreasingWidth());

            for(Rectangle curRec : rectangles){
                System.out.println("2");
                boolean placed = false;
                
                if(curRec.getHeight() > height){
                    curRec.rotated = true;
                }

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
                    System.out.println("3");
                    //basically just tries to find the first free spot in leftness order (just as in GreedyTrivialPack)
                    Point P = corners.get(0);
                    while(!placed){
                        System.out.println(P.x);
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
            
            if(RC.getCost()<bestCost){
                bestRC = RC.clone();
                bestCost = RC.getCost();
            }
        
            rectangles[r].rotated = false;
            rectangles[a].rotated = false;
        }
        }
        return bestRC;
    }
}