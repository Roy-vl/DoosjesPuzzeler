
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
        //return (int) (Math.pow(a.x,4)+Math.pow(a.y,4) - (Math.pow(b.x,4)+Math.pow(b.y,4)));
    }
}

public class PackCorners implements PackerStrategy{
    long startTime;
    ProblemStatement PS; 
    
    boolean[][] filledSpots;
    int width;
    int height;
    RectanglesContainer RC; 
    
    ArrayList<Corner> corners; 
    ArrayList<Rectangle> toPlace;
    
    int bestArea;
    int bestCost;
    RectanglesContainer bestRC;
    
    public boolean canBePlaced(Rectangle aRec){
        if(aRec.px+aRec.getWidth()>width || aRec.py+aRec.getHeight()>height) return false;
        for(int x = aRec.px; x < aRec.px+aRec.getWidth(); x++){
        for(int y = aRec.py; y < aRec.py+aRec.getHeight(); y++){
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
        fillSpots(curRec);
        corners.add(newCor1);
        corners.add(newCor2);

        Backtrack();

        RC.removeRectangle(curRec);
        emptySpots(curRec);
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
        //limit runtime to ~290 sec.     
        if((System.currentTimeMillis() - startTime) > 60000) return; 
        
        if(bestCost == 0) return;
        
        if(RC.getBoundingArea() >= bestArea) return;//Pruning
        
        if(toPlace.isEmpty()){
            int newArea = RC.getBoundingArea();
            if(newArea < bestArea){
                bestArea = newArea;
                bestCost = RC.getCost();
                bestRC = RC.clone();    
            }
        }else{
    
            Collections.sort(toPlace, new SortByArea());
            Collections.sort(corners, new SortByDistance());
                
            for(Rectangle curRec : new ArrayList<>(toPlace)){
                toPlace.remove(curRec);
                
                for(Corner curCor : new ArrayList<>(corners)){
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
        corners.add(new Corner(0,0));
      
        toPlace = new ArrayList<>(Arrays.asList(PS.getRectangles()));
        
        if(PS.getContainerHeight()>0){
            bestRC = (new PackLikeABeast()).pack(PS).clone();
        }else{
            bestRC = (new PackLikeMultipleBeasts()).pack(PS).clone();
        }
        bestArea = bestRC.getBoundingArea();//Integer.MAX_VALUE;
        bestCost = bestRC.getCost();//Integer.MAX_VALUE;
            
        Backtrack();
        
        return bestRC;
    }
}
