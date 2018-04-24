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
