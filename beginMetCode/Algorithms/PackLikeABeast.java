public class PackLikeABeast implements PackerStrategy{
    
    boolean[][] filledSpots;
    
    public boolean canBePlacedAt(int tx, int ty, Rectangle R){
        boolean canBePlaced = true;
        for(int x = tx; x < tx+R.getWidth() && canBePlaced; x++){
        for(int y = ty; y < ty+R.getHeight() && canBePlaced; y++){
            canBePlaced = canBePlaced && !filledSpots[x][y];
        }
        }
        return canBePlaced;
    }
    
    public void fillSpots(Rectangle R){
        for(int x = R.px; x < R.px+R.getWidth(); x++){
        for(int y = R.py; y < R.py+R.getHeight(); y++){
            filledSpots[x][y] = true;
        }
        }   
    }
    
    @Override
    public void pack(RectanglesContainer RC) {
        
        int Width = 10000;

        filledSpots = new boolean[Width][RC.containerHeight];
  
        for(Rectangle curRec : RC.rectangles){      
            for(int tx = 0; tx <= Width              - curRec.getWidth()  && !curRec.placed; tx++){
            for(int ty = 0; ty <= RC.containerHeight - curRec.getHeight() && !curRec.placed; ty++){
                if(canBePlacedAt(tx,ty,curRec)){
                    curRec.placed = true;
                    curRec.px = tx;
                    curRec.py = ty;
                    fillSpots(curRec);                          
                }
            }
            }
            if(!curRec.placed) System.out.println("Rectangle "+curRec.id+" is NOT PLACED!");
        }
    }
}