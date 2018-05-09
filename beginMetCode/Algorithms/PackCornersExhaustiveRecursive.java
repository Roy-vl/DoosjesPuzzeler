
import java.util.ArrayList;
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
        Corner clone = new Corner(this.x, this.y);
        return clone;
    }
}


public class PackCornersExhaustiveRecursive implements PackerStrategy{
    
    private RectanglesContainer curBest;
    private int curMinCost = Integer.MAX_VALUE;
    private int counter = 0;
    long startTime;
    long estimatedTime = System.currentTimeMillis() - startTime;
    
    private void tryAll(RectanglesContainer RC, ArrayList<Corner> corners, ArrayList<Rectangle> set){
        //if all rectangles have been placed
        if (set.isEmpty()){
            //and if the heigth is valid
            if(RC.getBoundingHeight() <= RC.containerHeight || RC.containerHeight == 0){
                int cost = RC.getCost();
                //and if the size of the box is smaller than current smallest
                if(cost < curMinCost){
                    //this is the new current best sollution
                    curBest = RC;
                    curMinCost = cost;
                    
                    RC.visualize();
                    
                    long runtingTime = System.currentTimeMillis() - startTime;
                    System.out.println("<==============better solution found==============>");
                    System.out.println(" Better fit found after "
                            +runtingTime+" ms"+" = "+(runtingTime/1000)+" sec");
                    System.out.println(" bounding area of "+(RC.getBoundingArea()));
                    System.out.println(" bounding width of "+(RC.getBoundingWidth()));
                    System.out.println(" bounding heigth of "+(RC.getBoundingHeight()));
                    System.out.println(" cost of "+(RC.getCost()));
                    System.out.println("<=================================================>");
                    System.out.println("");
                    System.out.println("");
                    
                    /*
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(PackCornersExhaustiveRecursive.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    */
                }
            }
        //if not all rectangles have been placed
        } else {
            for(Corner curCor : corners){
                for(Rectangle curRec : set){
                    
                    placeRect(RC, corners, curCor, set, curRec);
                    
                    if(RC.rotationAllowed){
                        Rectangle cCurRec = (Rectangle) curRec.clone();
                        cCurRec.rotated = true;
                        placeRect(RC, corners, curCor, set, cCurRec);
                    }
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
            
            
            //prune the currently tested branch it it's already to high
            //or if an already better final solution is found
            if((cRC.getBoundingHeight() > cRC.containerHeight && cRC.containerHeight != 0) || cRC.getCost() >= curMinCost){
            //System.out.println("PRUNED");
                return;
            }
            
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
        startTime = System.currentTimeMillis();
        ArrayList<Corner> corners = new ArrayList<>();
        corners.add(new Corner(0,0));
        ArrayList<Rectangle> rectangles = new ArrayList<>();
        
        //make a set of all rectangles
        for(Rectangle rec : RC.rectangles){
            rectangles.add(rec);
        }
        
        //tryAll tries every legal placing and sets curBest
        tryAll(RC, corners, rectangles);
        
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
