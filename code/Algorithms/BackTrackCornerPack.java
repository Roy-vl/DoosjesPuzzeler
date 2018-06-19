
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class BackTrackCornerPack implements PackerStrategy{
    long startTime;
    ProblemStatement PS; 
    
    boolean[][] filledSpots;
    int width;
    int height;
    QuadTree QT; 
    
    ArrayList<Point> corners; 
    ArrayList<Rectangle> toPlace;
    
    int bestArea;
    int bestCost;
    QuadTree bestQT;
    
    public boolean checkSpot(int x, int y){
        if(x<0 || y<0 || x>=width || y>=height) return true;
        return filledSpots[x][y];
    }
    
    public boolean canBePlacedAt(Point P, Rectangle R){
        for(int x = P.x; x < P.x+R.getWidth(); x++){
        for(int y = P.y; y < P.y+R.getHeight(); y++){
            if(checkSpot(x,y)) return false;
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
        if(checkSpot(C.x,C.y)) return;//already filled corner
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
        fillSpots(curRec);
        addCorner(newCor1);
        addCorner(newCor2);

        Backtrack();

        QT.removeRectangle(curRec);
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
        
        width = 10000;
        height = PS.getContainerHeight()>0 ? PS.getContainerHeight() : 10000;
        filledSpots = new boolean[width][height];
       
        QT = new QuadTree();
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
