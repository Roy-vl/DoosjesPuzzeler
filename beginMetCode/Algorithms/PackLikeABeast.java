public class PackLikeABeast implements PackerStrategy{
    
    @Override
    public void pack(Rectangle[] rectangles, boolean rotationAllowed, int containerHeight) {
        
        
        
        int minimumHeight;
        int maximumHeight = 0;
        int maximalWidth = 0;
        
        for(Rectangle curRec : rectangles){
            maximumHeight += curRec.getHeight();
            if(maximalWidth < curRec.getWidth()){maximalWidth = curRec.getWidth();}
        }
        
        minimumHeight = maximalWidth * 2;
        boolean[][] filledSpots = new boolean[maximumHeight][50];
        
        for(int i = minimumHeight; i<= maximumHeight; i++){
            
            for(Rectangle curRec : rectangles){      
                boolean placed = false;
                for(int j = 0; j < i && !placed; j++){
                    for(int k = 0; k < 50; k++){
                        if(filledSpots[j][k] == false){
                            curRec.px = j;
                            curRec.py = k;
                            placed = true;
                            filledSpots[j][k] = true;
                            break;
                        }
                    }
                }
                
            }
            
        }
    }
}