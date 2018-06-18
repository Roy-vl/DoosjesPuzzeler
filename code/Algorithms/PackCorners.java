
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class PackCorners implements PackerStrategy{
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
        corners.add(newCor1);
        corners.add(newCor2);
    }
    
    @Override
    public QuadTree pack(ProblemStatement PS){
        int height = 400;
        QT = new QuadTree(0,0,100000,PS.getContainerHeight());
        
        Rectangle[] rectangles = PS.getRectangles();
      
        corners = new ArrayList<>();
        corners.add(new Point(0,0));
        
        Arrays.sort(rectangles, new SortByArea());
        Arrays.sort(rectangles, new SortByDecreasingWidth());
 
        for(Rectangle curRec : rectangles){
            /*curRec.px = (int)(Math.random()*2000);
            curRec.py = (int)(Math.random()*2000);
            place(curRec);
            continue;*/
            
            if(PS.getRotationAllowed()) if(curRec.sy>PS.getContainerHeight()) curRec.rotated = true;
            
            boolean placed = false;
            Collections.sort(corners, new SortByLeftness());
            for(Point curCor : new ArrayList<>(corners)){
                curRec.px = curCor.x;
                curRec.py = curCor.y;
                
                if(canBePlaced(curRec)){
                    place(curRec);
                    //QT.visualize();
                    corners.remove(curCor);
                    placed = true;
                    break;       
                }
            }
            
            if(!placed){
                System.out.println("RECTANGLE "+curRec.id+" COULD NOT BE PLACED BY PLACING IT IN A CORNER REVERTING TO OTHER (SLOW) METHODS");
                System.out.println("Rectangle dimensions :"+curRec.getWidth()+","+curRec.getHeight());
                System.out.println("Rectangle rotated :"+curRec.rotated);
                //basically just tries to find the first free spot in leftness order (just as in GreedyTrivialPack)
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
                    if(P.y>=height){
                        P.x++;
                        P.y = 0;
                    }
                }            
                System.out.println("RECTANGLE "+curRec.id+" COULD BE PLACED BY REVERTING TO OTHER (SLOW) METHODS");
            }
}
        
        return QT;
    }
}