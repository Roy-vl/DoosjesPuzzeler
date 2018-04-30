
import java.util.ArrayList;


public class PackCornersExhaustiveRecursive implements PackerStrategy{
    
    private ArrayList<RectanglesContainer> potentialSolves = new ArrayList<>();
    
    private void tryAll(RectanglesContainer RC, ArrayList<Corner> corners, ArrayList<Rectangle> set){
        if (set.isEmpty()){
            potentialSolves.add(RC);
            
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
                }
            }
            cSet.remove(toRemove2);
            
            //and place it in the RC
            Rectangle toEdit = null;
            for(Rectangle rect : cRC.rectangles){
                if(rect.id == cCurRec.id){
                    toEdit = rect;
                }
            }
            toEdit.px = cCurCor.x;
            toEdit.py = cCurCor.y;
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
            }
        }
        toEdit.px = cCurCor.x;
        toEdit.py = cCurCor.y;
        toEdit.placed = true;
        //check if it collides
        return cRC.checkCollision(toEdit);
    }
    
    
    @Override
    public void pack(RectanglesContainer RC){
       
        ArrayList<Corner> corners = new ArrayList<>();
        corners.add(new Corner(0,0));
        ArrayList<Rectangle> rectangles = new ArrayList<>();
        
        for(Rectangle rec : RC.rectangles){
            rectangles.add(rec);
        }
        
        //tryAll populates potentialSolves with all possible solutions
        tryAll(RC, corners, rectangles);
        
        //recPack populates potentialSolves with all possible solutions
        //recPack(Corners, RC, rectangles);
        
        
        //test which potential solution is the best
        int curMinSize = Integer.MAX_VALUE;
        RectanglesContainer curBest = null;
        for(RectanglesContainer potSol : potentialSolves){
            int curWidth = 0;
            int curHeigth = 0;
            
            //awfull implementation to get extremes of the surrounding rect
            for(Rectangle rect : potSol.rectangles){
                int rightMostEdge = rect.px + rect.getWidth();
                if (rightMostEdge > curWidth){
                    curWidth = rightMostEdge;
                }
                int topMostEdge = rect.py + rect.getHeight();
                if (topMostEdge > curHeigth){
                    curHeigth = topMostEdge;
                }
            }
            
            //multiply the outer edges of the box
            int size = curWidth * curHeigth;
            
            //if the size of the box is smaller than the rest
            if(size < curMinSize){
                //potSol.visualize();
                curBest = potSol;
                curMinSize = size;
            }
        }
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
