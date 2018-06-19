
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class BackTrackCornerPack implements PackerStrategy{
    long startTime;
    ProblemStatement PS; 
   
    QuadTree QT; 
    
    ArrayList<Point> corners; 
    ArrayList<Rectangle> toPlace;
    
    int bestArea;
    int bestCost;
    QuadTree bestQT;
     
    public void addCorner(Point C){
        if(QT.collides(C.x,C.y)) return;//already filled corner
        corners.add(C);
    }  
    
    public void placeAndRecurse(Point curCor, Rectangle curRec){
        curRec.px = curCor.x;
        curRec.py = curCor.y;
        
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

        Backtrack();

        QT.removeRectangle(curRec);
        corners.remove(newCor1);
        corners.remove(newCor2);      
    }
    
    public void tryToPlaceAndRecurse(Point curCor, Rectangle curRec){
        curRec.px = curCor.x;
        curRec.py = curCor.y;
        
        curRec.rotated = false;
        if(QT.canBePlaced(curRec)) placeAndRecurse(curCor, curRec);

        if(PS.getRotationAllowed()){
            curRec.rotated = true;
            if(QT.canBePlaced(curRec)) placeAndRecurse(curCor, curRec);
        }
    }
   
    public void Backtrack(){
        //limit runtime to ~290 sec.     
        if((System.currentTimeMillis() - startTime) > 20000) return; 
        
        if(bestCost == 0) return;
        
        if(QT.getRectanglesBoundArea() >= bestArea) return;//Pruning
        
        if(toPlace.isEmpty()){
            int newArea = (int)(QT.getRectanglesBoundArea());
            if(newArea < bestArea){
                bestArea = newArea;
                bestCost = (int)(QT.getCost());
                bestQT = QT.clone();    
            }
        }else{
    
            Collections.sort(toPlace, new SortByArea());
            Collections.sort(corners, new SortByDistance());
                
            for(Rectangle curRec : new ArrayList<>(toPlace)){
                toPlace.remove(curRec);
                
                for(Point curCor : new ArrayList<>(corners)){
                    corners.remove(curCor);
                    
                    tryToPlaceAndRecurse(curCor, curRec);
                    
                    corners.add(curCor);
                }
                
                toPlace.add(curRec);
            }
        }
    }
    
    @Override
    public boolean applicable(ProblemStatement PS){
        if(PS.getRectangleAmount()>10){
            return false;
        }
        return true;
    }
    
    @Override
    public QuadTree pack(ProblemStatement PS){ 
        
        startTime = System.currentTimeMillis();
        
        this.PS = PS;
  
        QT = new QuadTree(0,0,1000000,PS.getContainerHeight()>0 ? PS.getContainerHeight() : 1000000);
        if(PS.getContainerHeight()>0) QT.forcedRectanglesBoundHeight = PS.getContainerHeight();
        
        corners = new ArrayList<>();
        corners.add(new Point(0,0));
      
        toPlace = new ArrayList<>(Arrays.asList(PS.getRectangles()));
        
        if(PS.getContainerHeight()>0){
            bestQT = (new GreedyCornerPack()).pack(PS).clone();
        }else{
            bestQT = (new MultipleGreedyCornerPack()).pack(PS).clone();
        }
        bestArea = bestQT.getRectanglesBoundArea();//Integer.MAX_VALUE;
        bestCost = bestQT.getCost();//Integer.MAX_VALUE;
        
        Backtrack();
        
        return bestQT;
    }
}
