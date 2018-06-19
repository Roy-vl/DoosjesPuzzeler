import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class GreedyCornerPack implements PackerStrategy{
    long startTime;
    
    ProblemStatement PS; 
    
    QuadTree QT; 
    
    ArrayList<Point> corners; 
    ArrayList<Rectangle> toPlace;
    
    float bestArea;
    float bestCost;
    QuadTree bestQT;
    
    public boolean canBePlaced(Rectangle aRec){
        return QT.canBePlaced(aRec);
    }
    
    public void addCorner(Point C){
        if(QT.collides(C.x,C.y)) return;//already filled corner
        corners.add(C);
    }
    
    public void place(Rectangle curRec){
        Point newCor1 = new Point(
            curRec.px + curRec.getWidth(),
            curRec.py
        );
        
        Point newCor2 = new Point(
            curRec.px,
            curRec.py + curRec.getHeight()
        );
        QT.addRectangle(curRec);
        addCorner(newCor1);
        addCorner(newCor2);
    }
    
    @Override
    public boolean applicable(ProblemStatement PS){
        if(PS.getContainerHeight()==0){
            return false;
        }
        return true;
    }
    
    @Override
    public QuadTree pack(ProblemStatement PS){
        QT = new QuadTree(0,0,1000000,PS.getContainerHeight());
        QT.forcedRectanglesBoundHeight = PS.getContainerHeight();
        
        Rectangle[] rectangles = PS.getRectangles();
        
        //Rotate rectangles if neccesary
        double relativeSize = (PS.getContainerHeight() / 20);
        int relativeS = (int) relativeSize;
        if(PS.getRotationAllowed()){
            for(Rectangle curRec : rectangles){           
                if((curRec.sy > curRec.sx && curRec.sy > relativeS) || curRec.sy > PS.getContainerHeight() ){
                    curRec.rotated = true;
                }
            }
        }
      
        corners = new ArrayList<>();
        corners.add(new Point(0,0));
        
        Arrays.sort(rectangles, new SortByArea());
        Arrays.sort(rectangles, new SortByDecreasingWidth());
 
        for(Rectangle curRec : rectangles){
            /*curRec.px = (int)(Math.random()*2000);
            curRec.py = (int)(Math.random()*2000);
            place(curRec);
            continue;*/

            boolean placed = false;
            Collections.sort(corners, new SortByLeftness());
            for(Point curCor : new ArrayList<>(corners)){
                if(QT.collides(curCor.x,curCor.y)){
                    corners.remove(curCor);
                    continue;
                }
                
                curRec.px = curCor.x;
                curRec.py = curCor.y;
                
                if(canBePlaced(curRec)){
                    place(curRec);
                    corners.remove(curCor);
                    placed = true;
                    break;       
                }
            }
            
            if(!placed){

                Point P = corners.get(0).clone();
                while(!placed){
                    //System.out.println(P.x+","+P.y);
                    curRec.px = P.x;
                    curRec.py = P.y;
                    if(canBePlaced(curRec)){
                        place(curRec);
                        placed = true;
                    }
                    P.y+=curRec.getHeight();
                    if(P.y>=PS.getContainerHeight()){
                        P.x++;
                        P.y = 0;
                    }
                }            
            }
        }
        
        return QT;
    }
}