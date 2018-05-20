import java.util.Arrays;
public class PackLikeABeast implements PackerStrategy{
       
    @Override
    public QuadTree pack(ProblemStatement PS){
        
        QuadTree QT = new QuadTree(0,0,500,PS.getContainerHeight());

        //create a clone of the PS rectangles
        Rectangle[] rectangles = PS.getRectangles();
        Arrays.sort(rectangles,new SortByArea());
  
        int mx = 0;
        int my = 0;
        
        for(Rectangle curRec : rectangles){
            
            curRec.rotated = curRec.sy > PS.getContainerHeight() && PS.getRotationAllowed();
            
            while(QT.collides(mx,my)){
                my++;
                if(my >= PS.getContainerHeight()){
                    my = 0;
                    mx++;
                }
            }
            
            int tx = mx;
            int ty = my;
            
            boolean placed = false;

            while(!placed){
                if(ty > PS.getContainerHeight()-curRec.getHeight()){
                    tx++;
                    ty = 0;
                }

                curRec.px = tx;
                curRec.py = ty;
                if(!QT.collides(curRec)){
                    QT.addRectangle(curRec);
                    placed = true;
                }
                
                ty++;
            }
            
            if(!placed) System.out.println("RECTANGLE "+curRec.id+" COULD NOT BE PLACED");
            
        }

        return QT;
    }
}