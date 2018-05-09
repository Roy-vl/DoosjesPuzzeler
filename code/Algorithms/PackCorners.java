
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

class Corner{
    int x,y;
    public Corner(int _x,int _y){
        x=_x;
        y=_y;
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
    
    ProblemStatement PS; 
    RectanglesContainer RC; 
    ArrayList<Corner> corners; 
    ArrayList<Rectangle> toPlace;
    int bestArea;
    int bestCost;
    RectanglesContainer bestRC;
    
    public boolean canBePlaced(Rectangle aRec){
        return !RC.checkCollision(aRec) && (PS.getContainerHeight()==0 || aRec.py+aRec.getHeight()<=PS.getContainerHeight());
    }
    
    public void placeAndRecurse(Corner curCor, Rectangle curRec){
        Corner newCor1 = new Corner(
            curRec.px+curRec.getWidth(),
            curRec.py
        );
                        
        Corner newCor2 = new Corner(
                curRec.px,
                curRec.py+curRec.getHeight()
        );
 
        RC.addRectangle(curRec);
        toPlace.remove(curRec);
        corners.remove(curCor);
        corners.add(newCor1);
        corners.add(newCor2);

        Backtrack();

        RC.removeRectangle(curRec);
        toPlace.add(curRec);
        corners.add(curCor);
        corners.remove(newCor1);
        corners.remove(newCor2);
        
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
        //RC.visualize();
        
        if(bestCost == 0) return;
        
        if(toPlace.isEmpty()){
            int newArea = RC.getBoundingArea();
            int newCost = RC.getCost();
            if(newArea<bestArea){
                RC.visualize();
                bestArea = newArea;
                bestCost = newCost;
                bestRC   = RC.clone();
            }
        }else{
            if(RC.getBoundingArea()>=bestArea) return;//Pruning
            
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
    public RectanglesContainer pack(ProblemStatement cPS){
        PS = cPS;
        RC = new RectanglesContainer();
        
        if(PS.getContainerHeight()>0) RC.setForcedBoundingHeight(PS.getContainerHeight());
        
        corners = new ArrayList<>();
        corners.add(new Corner(0,0));
        
        Rectangle[] rectangles = PS.getRectangles();
        toPlace = new ArrayList<>();
        for(Rectangle curRec : rectangles) toPlace.add(curRec);
        
        bestArea = 1000000000;
        bestCost = 1000000000;
        bestRC   = null;
        
        Backtrack();
        
        return bestRC;
    }
}
