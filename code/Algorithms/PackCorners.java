
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

public class PackCorners implements PackerStrategy{
    
    ProblemStatement PS; 
    RectanglesContainer RC; 
    ArrayList<Corner> corners; 
    ArrayList<Rectangle> toPlace;
    int bestArea;
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
        if(toPlace.isEmpty()){
            int newArea = RC.getBoundingArea();
            if(newArea<bestArea){
                //RC.visualize();
                bestArea = newArea;
                bestRC   = RC.clone();
            }
        }else{
            if(RC.getBoundingArea()>=bestArea) return;//Pruning
            
            for(Corner curCor : new ArrayList<Corner>(corners)){
                for(Rectangle curRec : new ArrayList<Rectangle>(toPlace)){
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
        
        toPlace = new ArrayList<>(Arrays.asList(PS.getRectangles()));
        //Collections.sort(toPlace, new SortByArea());
        
        bestArea = 1000000000;
        bestRC   = null;
        
        Backtrack();
        
        return bestRC;
    }
}
