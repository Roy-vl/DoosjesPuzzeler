public class Rectangle {
    int px,py,sx,sy;
    boolean rotated;
    
    public Rectangle(){
        px=0;
        py=0;
        sx=0;
        sy=0;
        rotated=false;
    }
    
    public Rectangle(int _px, int _py, int _sx, int _sy, boolean _rotated){
        px=_px;
        py=_py;
        sx=_sx;
        sy=_sy;
        rotated=_rotated;
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
