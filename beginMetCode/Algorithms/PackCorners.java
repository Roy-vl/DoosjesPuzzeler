
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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



class SortByDistance implements Comparator<Corner>
{
    public int compare(Corner a, Corner b)
    {
        return (a.x*a.x+a.y*a.y) - (b.x*b.x+b.y*b.y);
    }
}


public class PackCorners implements PackerStrategy{
    
    @Override
    public void pack(RectanglesContainer RC){
       
       ArrayList<Corner> Corners = new ArrayList<>();
       Corners.add(new Corner(0,0));
       
        for(Rectangle curRec : RC.rectangles){
            
            for(Corner curCor : Corners){
                
                curRec.px = curCor.x;
                curRec.py = curCor.y;

                if(RC.checkCollision(curRec)){
                    if(RC.rotationAllowed){
                        curRec.rotated = true;
                        if(RC.checkCollision(curRec)){
                            continue;
                        }
                    }else{
                        continue;
                    }
                }
                
                Corners.remove(curCor);
                Corners.add(new Corner(
                        curRec.px+curRec.getWidth(),
                        curRec.py
                ));
                Corners.add(new Corner(
                        curRec.px,
                        curRec.py+curRec.getHeight()
                ));

                curRec.placed = true;
                        
                break;
            }
            
            Collections.sort(Corners, new SortByDistance());
        
            if(!curRec.placed){
                System.out.println("fuck");
            }

        }
    }
}
