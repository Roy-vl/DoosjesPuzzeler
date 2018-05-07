
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PackCornersExhaustiveRecursive implements PackerStrategy{
    
    private ArrayList<RectanglesContainer> potentialSolves = new ArrayList<>();
    private RectanglesContainer curBest;
    private int curMinCost = Integer.MAX_VALUE;
    
    int RCIndex = 0;
    int previousBest = 0;
    ArrayList<Integer> BestIndexes = new ArrayList<>();
    
    private void tryAll(RectanglesContainer RC, ArrayList<Corner> corners, ArrayList<Rectangle> set){
        if (set.isEmpty()){
            RCIndex++;
            
            /*RC.visualize();
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(PackCornersExhaustiveRecursive.class.getName()).log(Level.SEVERE, null, ex);
            }*/
            
            if(RC.getTotalHeight() <= RC.containerHeight || RC.containerHeight == 0){
                int cost = RC.getCost();
                //if the size of the box is smaller than current smallest
                if(cost < curMinCost){
                    curBest = RC;
                    curMinCost = cost;
                    BestIndexes.clear();
                    BestIndexes.add(RCIndex);
                } else if ( cost == curMinCost){
                    BestIndexes.add(RCIndex);
                }
            }
            
        } else {
            for(Corner curCor : corners){
                for(Rectangle curRec : set){
                    
                    placeRect(RC, corners, curCor, set, curRec);
                    
                    if(RC.rotationAllowed){
                        Rectangle cCurRec = (Rectangle) curRec.clone();
                        cCurRec.rotated = true;
                        placeRect(RC, corners, curCor, set, cCurRec);
                    }
                    
                    /*
                    Rectangle cCurRec = (Rectangle) curRec.clone();
                    placeRect(RC, corners, curCor, set, cCurRec);
                    
                    Rectangle cCurRec2 = (Rectangle) curRec.clone();
                    cCurRec2.rotated = true;
                    placeRect(RC, corners, curCor, set, cCurRec2);
                    */
                    
                    /*
                    if(RC.rotationAllowed){
                        Rectangle cCurRec = (Rectangle) curRec.clone();
                        cCurRec.rotated = true;
                        placeRect(RC, corners, curCor, set, cCurRec);
                    }
                    */
                }
            }
        }
    }
    
    private void placeRect(RectanglesContainer RC, ArrayList<Corner> corners, Corner curCor, ArrayList<Rectangle> set, Rectangle curRec) {
        RectanglesContainer cRC = (RectanglesContainer) RC.clone();
        ArrayList<Corner> cCorners = (ArrayList<Corner>) corners.clone();
        Corner cCurCor = (Corner) curCor.clone();
        ArrayList<Rectangle> cSet = (ArrayList<Rectangle>) set.clone();
        Rectangle cCurRec = (Rectangle) curRec.clone();
        
        //if the rectangle can be placed
        if (tryCanPlace(cRC, cCurCor, cCurRec)){
            //remove current corner and add the new corners
            Corner toRemove = null;
            for(Corner corner : cCorners){
                if(corner.x == cCurCor.x && corner.y == cCurCor.y){
                    toRemove = corner;
                    break;
                }
            }
            cCorners.remove(toRemove);
            cCorners.add(new Corner(cCurCor.x, cCurCor.y + cCurRec.getHeight()));
            cCorners.add(new Corner(cCurCor.x + cCurRec.getWidth(), cCurCor.y));
            
            //remove the rectangle from the set  
            Rectangle toRemove2 = null;
            for(Rectangle rect : cSet){
                if(rect.id == cCurRec.id){
                    toRemove2 = rect;
                    break;
                }
            }
            cSet.remove(toRemove2);
            
            //and place it in the RC
            Rectangle toEdit = null;
            for(Rectangle rect : cRC.rectangles){
                if(rect.id == cCurRec.id){
                    toEdit = rect;
                    break;
                }
            }
            toEdit.px = cCurCor.x;
            toEdit.py = cCurCor.y;
            toEdit.rotated = cCurRec.rotated;
            toEdit.placed = true;
            
            tryAll(cRC, cCorners, cSet);
        }      
    }
    
    private boolean tryCanPlace(RectanglesContainer RC, Corner curCor, Rectangle curRec) {
        RectanglesContainer cRC = (RectanglesContainer) RC.clone();
        Corner cCurCor = (Corner) curCor.clone();
        Rectangle cCurRec = (Rectangle) curRec.clone();
        
        //place copy of rectangle in the copy of RC
        Rectangle toEdit = null;
        for(Rectangle rect : cRC.rectangles){
            if(rect.id == cCurRec.id){
                toEdit = rect;
                break;
            }
        }
        toEdit.px = cCurCor.x;
        toEdit.py = cCurCor.y;
        toEdit.rotated = cCurRec.rotated;
        
        //check if it collides
        return !cRC.checkCollision(toEdit);
    }
    
    
    @Override
    public void pack(RectanglesContainer RC){
       
        
        ArrayList<Corner> corners = new ArrayList<>();
        corners.add(new Corner(0,0));
        ArrayList<Rectangle> rectangles = new ArrayList<>();
        
        //make a set of all rectangles
        for(Rectangle rec : RC.rectangles){
            rectangles.add(rec);
        }
        
        //tryAll tries every legal placing and sets curBest
        tryAll(RC, corners, rectangles);
        
        //System.out.println("indexes of best solution are: "+BestIndexes+"our of: "+RCIndex);
        
        //move the original RC rechtangle to optimum location
        for(Rectangle rec : RC.rectangles){
            for(Rectangle rect : curBest.rectangles){
                if(rec.id == rect.id){
                    rec.px = rect.px;
                    rec.py = rect.py;
                    rec.rotated = rect.rotated;
                }
            }
        }
    }

    
        



}
