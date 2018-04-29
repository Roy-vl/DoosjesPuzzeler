
import java.util.Comparator;

public class Rectangle {
    int id;
    int px,py,sx,sy;
    boolean rotated;
    boolean placed;
    
    public Rectangle(){
        id=0;
        px=0;
        py=0;
        sx=0;
        sy=0;
        rotated=false;
        placed = false;
    }
    
    public Rectangle(int _id, int _px, int _py, int _sx, int _sy, boolean _rotated, boolean _placed){
        id=_id;
        px=_px;
        py=_py;
        sx=_sx;
        sy=_sy;
        rotated=_rotated;
        placed =_placed;
    }
    
    public int getWidth(){
        return rotated?sy:sx;
    }
    
    public int getHeight(){
        return rotated?sx:sy;
    }
    
    public boolean Collides(Rectangle B){
        return px < B.px + B.getWidth() &&
               px + getWidth() > B.px &&
               py < B.py + B.getHeight() &&
               getHeight() + py > B.py;
    }
}

class SortByID implements Comparator<Rectangle>
{
    public int compare(Rectangle a, Rectangle b)
    {
        return a.id-b.id;
    }
}

class SortByArea implements Comparator<Rectangle>
{
    public int compare(Rectangle a, Rectangle b)
    {
        return b.sx*b.sy-a.sx*a.sy;
    }
}