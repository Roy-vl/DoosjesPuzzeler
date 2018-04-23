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
    
    public boolean Collides(Rectangle B){
        //TODO: IMPLEMENT ROTATION
        return px < B.px + B.sx &&
               px + sx > B.px &&
               py < B.py + B.sy &&
               sy + py > B.py;
    }
}
