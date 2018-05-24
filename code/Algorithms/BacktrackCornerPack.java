
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class BacktrackCornerPack implements PackerStrategy{
    long startTime;
    ProblemStatement PS; 
    
    boolean[][] filledSpots;
    int width;
    int height;
    RectanglesContainer RC; 
    
    ArrayList<Point> corners; 
    ArrayList<Rectangle> toPlace;
    
    int bestArea;
    int bestCost;
    RectanglesContainer bestRC;
    
    public boolean canBePlacedAt(Point P, Rectangle R){
        if(P.x+R.getWidth()>width || P.y+R.getHeight()>height) return false;//out of bounds
        for(int x = P.x; x < P.x+R.getWidth(); x++){
        for(int y = P.y; y < P.y+R.getHeight(); y++){
            if(filledSpots[x][y]) return false;
        }
        }
        return true;
    }
    
    public void fillSpots(Rectangle aRec){
        for(int x = aRec.px; x < aRec.px+aRec.getWidth(); x++){
        for(int y = aRec.py; y < aRec.py+aRec.getHeight(); y++){
            filledSpots[x][y] = true;
        }
        }   
    }
    
     public void emptySpots(Rectangle aRec){
        for(int x = aRec.px; x < aRec.px+aRec.getWidth(); x++){
        for(int y = aRec.py; y < aRec.py+aRec.getHeight(); y++){
            filledSpots[x][y] = false;
        }
        }   
    }
     
    public void addCorner(Point C){
        if(C.x>=width || C.y>=height) return;//within bounds
        if(filledSpots[C.x][C.y]) return;//not already filled
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
 
        RC.addRectangle(curRec);
        fillSpots(curRec);
        addCorner(newCor1);
        addCorner(newCor2);

        Backtrack();

        RC.removeRectangle(curRec);
        emptySpots(curRec);
        corners.remove(newCor1);
        corners.remove(newCor2);
             
    }
    
    public void tryToPlaceAndRecurse(Point curCor, Rectangle curRec){
        
        curRec.rotated = false;
        if(canBePlacedAt(curCor,curRec)) placeAndRecurse(curCor, curRec);

        if(PS.getRotationAllowed()){
            curRec.rotated = true;
            if(canBePlacedAt(curCor, curRec)) placeAndRecurse(curCor, curRec);
        }
    }
   
    public void Backtrack(){
        //limit runtime to ~290 sec.     
        if((System.currentTimeMillis() - startTime) > 20000) return; 
        
        if(bestCost == 0) return;
        
        if(RC.getBoundingArea() >= bestArea) return;//Pruning
        
        if(toPlace.isEmpty()){
            int newArea = RC.getBoundingArea();
            if(newArea < bestArea){
                RC.visualize(); 
                bestArea = newArea;
                bestCost = RC.getCost();
                bestRC = RC.clone();    
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
    public RectanglesContainer pack(ProblemStatement PS){
        startTime = System.currentTimeMillis();
        
        this.PS = PS;
        
        width = 10000;
        height = PS.getContainerHeight()>0 ? PS.getContainerHeight() : 10000;
        filledSpots = new boolean[width][height];
       
        RC = new RectanglesContainer();
        if(PS.getContainerHeight()>0) RC.setForcedBoundingHeight(PS.getContainerHeight());
        
        corners = new ArrayList<>();
        corners.add(new Point(0,0));
      
        toPlace = new ArrayList<>(Arrays.asList(PS.getRectangles()));
        
        if(PS.getContainerHeight()>0){
            bestRC = (new GreedyCornerPack()).pack(PS).clone();
            bestRC.visualize();
        }else{
            bestRC = (new MultipleGreedyCornerPack()).pack(PS).clone();
        }
        bestArea = bestRC.getBoundingArea();//Integer.MAX_VALUE;
        bestCost = bestRC.getCost();//Integer.MAX_VALUE;
            
        Backtrack();
        
        return bestRC;
    }
}
