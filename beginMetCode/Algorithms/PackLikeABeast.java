public class PackLikeABeast implements PackerStrategy{
    
    @Override
    public void pack(RectanglesContainer RC) {
        
        int minimumHeight = 0;
        int maximumHeight = 0;
        int maximalWidth = 0;
        int Width = 10000;
        
        for(Rectangle curRec : RC.rectangles){
            maximumHeight += curRec.getHeight();
            if(maximalWidth < curRec.getWidth()){maximalWidth = curRec.getWidth();}
            if(minimumHeight < curRec.getHeight()){minimumHeight = curRec.getHeight();}
        }
        
        boolean[][] filledSpots = new boolean[Width][minimumHeight];
        
        
        //System.out.println("maximumHeight : "+maximumHeight);
        //System.out.println("maximumWidth : "+maximalWidth);
        //System.out.println("minimumHeight: "+minimumHeight); 
 
        for(Rectangle curRec : RC.rectangles){      
            boolean canPlace = false;

            for(int tx = 0; tx <= Width        -curRec.getWidth()  && !curRec.placed; tx++){
            for(int ty = 0; ty <= minimumHeight-curRec.getHeight() && !curRec.placed; ty++){

                    //System.out.println("Trying to place at : "+tx+","+ty);

                    boolean canBePlaced = true;
                    for(int x = tx; x < tx+curRec.getWidth() && canBePlaced; x++){
                    for(int y = ty; y < ty+curRec.getHeight() && canBePlaced; y++){
                        canBePlaced = canBePlaced && !filledSpots[x][y];
                    }
                    }

                    //System.out.println("can curRec : "+curRec.id+" be placed? : "+canBePlaced);

                    if(canBePlaced){

                        curRec.placed = true;
                        curRec.px = tx;
                        curRec.py = ty;

                        for(int x = tx; x < tx+curRec.getWidth(); x++){
                        for(int y = ty; y < ty+curRec.getHeight(); y++){
                            filledSpots[x][y] = true;
                        }
                        }                            

                    }

            }
            }

            //if(!curRec.placed) System.out.println("Rectangle "+curRec.id+" is NOT PLACED!");
        }

    }
}