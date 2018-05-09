
import java.util.Arrays;

public class PackTetris implements PackerStrategy{
    
    @Override
    public RectanglesContainer pack(ProblemStatement PS){
        RectanglesContainer RC = new RectanglesContainer();
      
        int[] widths = new int[PS.getContainerHeight()];    
        Rectangle[] rectangles = PS.getRectangles();
        Arrays.sort(rectangles,new SortByArea());
        
        for(Rectangle curRec : rectangles){

            curRec.rotated = PS.getRotationAllowed() && curRec.sy>curRec.sx;             

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
                    
                    RC.addRectangle(curRec);
                    
                    placed = true;
                 
                //continue search
                }else{
                    ty++;
                    if(ty>PS.getContainerHeight()-curRec.getHeight()){
                        ty=0;
                        tx++;
                    }
                }
            }  
        }    
        
        return RC;
    }
}
