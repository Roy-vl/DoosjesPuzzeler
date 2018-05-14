
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

class Corner{
    int x, y;
    public Corner(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    @Override
    public Corner clone(){
        Corner clone = new Corner(x, y);
        return clone;
    }
}

class SortByDistance implements Comparator<Corner>
{
    public int compare(Corner a, Corner b)
    {
        return a.x*a.x+a.y*a.y - (b.x*b.x+b.y*b.y);
    }
}

public class PackCorners implements PackerStrategy{
    long startTime;
    ProblemStatement PS; 
    RectanglesContainer RC; 
    ArrayList<Corner> corners; 
    ArrayList<Rectangle> toPlace;
    int bestArea;
    int bestCost;
    RectanglesContainer bestRC;
    
    public boolean canBePlaced(Rectangle aRec){
        return !RC.checkCollision(aRec) && (PS.getContainerHeight() == 0 
                || aRec.py+aRec.getHeight() <= PS.getContainerHeight());
    }
    
    public void placeAndRecurse(Corner curCor, Rectangle curRec){
        Corner newCor1 = new Corner(
            curRec.px + curRec.getWidth(),
            curRec.py
        );
        
        Corner newCor2 = new Corner(
                curRec.px,
                curRec.py + curRec.getHeight()
        );
 
        RC.addRectangle(curRec);
        toPlace.remove(curRec);
        corners.remove(curCor);
        corners.add(newCor1);
        corners.add(newCor2);

        Backtrack();

        RC.removeRectangle(curRec);
        corners.remove(newCor1);
        corners.remove(newCor2);
        toPlace.add(curRec);
        corners.add(curCor);
        
    }
    
    public void tryToPlaceAndRecurse(Corner curCor, Rectangle curRec){
        curRec.px = curCor.x;
        curRec.py = curCor.y;

        curRec.rotated = false;
        if(canBePlaced(curRec)) placeAndRecurse(curCor, curRec);

        if(PS.getRotationAllowed()){
            
            curRec.rotated = true;
            if(canBePlaced(curRec)) placeAndRecurse(curCor, curRec);
            
        }
    }
   
    public void Backtrack(){
        //defines time limit
        if((System.currentTimeMillis() - startTime) > 300000) return; 
        
        if(toPlace.isEmpty()){
            int newArea = RC.getBoundingArea();
            if(newArea < bestArea){
                // TODO: remove the visualize for each solution
                RC.visualize(); 
                bestArea = newArea;
                bestCost = RC.getCost();
                if(bestCost == 0) return;
                bestRC = RC.clone();    
        }
        }else{
            if(RC.getBoundingArea() >= bestArea) return;//Pruning
            
            Collections.sort(toPlace, new SortByArea());
            Collections.sort(corners, new SortByDistance());
                
            for(Rectangle curRec : new ArrayList<>(toPlace)){
                for(Corner curCor : new ArrayList<>(corners)){
                    tryToPlaceAndRecurse(curCor, curRec);
                }
            }
        }
    }
    
    @Override
    public RectanglesContainer pack(ProblemStatement PS){
        startTime = System.currentTimeMillis();
        this.PS = PS;
        RC = new RectanglesContainer();
        
        if(PS.getContainerHeight()>0) 
            RC.setForcedBoundingHeight(PS.getContainerHeight());
        
        corners = new ArrayList<>();
        corners.add(new Corner(0,0));
      
        toPlace = new ArrayList<>(Arrays.asList(PS.getRectangles()));
        
        bestArea = 1000000000;
        bestCost = 1000000000;
        bestRC   = null;
        
        Backtrack();
        
        return bestRC;
    }
}
