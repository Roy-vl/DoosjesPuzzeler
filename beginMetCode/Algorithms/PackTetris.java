public class PackTetris implements PackerStrategy{
    
    @Override
    public void pack(Rectangle[] rectangles, boolean rotationAllowed, int containerHeight) {
      
        int[] widths = new int[containerHeight];    
        
        for(Rectangle curRec : rectangles){

            curRec.rotated = rotationAllowed && curRec.sy>curRec.sx;             

            //find lowest point in the widths
            int ty = 0;
            int tx = widths[0];
            for(int w=0;w<=widths.length-curRec.getHeight();w++){
                if(widths[w]<tx){
                    ty = w;
                    tx = widths[w];
                }
            }
            
            boolean placed = false;
            while(!placed){

                //check if it can be placed at ty,tx
                boolean canBePlaced = true;
                for(int i=ty; i<ty+curRec.getHeight(); i++){
                    canBePlaced = canBePlaced && widths[i]<=tx;
                }
                
                //place it
                if(canBePlaced){
                    curRec.px = tx;
                    curRec.py = ty;
                    
                    for(int i=ty; i<ty+curRec.getHeight(); i++){
                        widths[i]=curRec.px+curRec.getWidth();
                    }
                    
                    placed = true;
                 
                //continue search
                }else{
                    ty++;
                    if(ty>containerHeight-curRec.getHeight()){
                        ty=0;
                        tx++;
                    }
                }
            }  
        }    
    }
}
