
import java.awt.Color;
import java.util.Comparator;
import java.util.Random;

public class Rectangle {
    int id;
    int px,py,sx,sy;
    boolean rotated;
    
    public Rectangle(){
        id=0;
        px=0;
        py=0;
        sx=0;
        sy=0;
        rotated=false;
    }
    
    @Override
    public Rectangle clone(){
        Rectangle clone = new Rectangle(id, px, py, sx, sy, rotated);
        return clone;
    }
    
    public Rectangle(int _id, int _px, int _py, int _sx, int _sy, boolean _rotated){
        id=_id;
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
    
    public int getArea(){
        return sx*sy;
    }
    
    public int getAntiSquariness(){
        return (int)(sx>sy ? (float)sx/sy: (float)sy/sx);
    }
    
    public int getPackingScore(){
        int score;
        score = 1000*getArea()+10*getHeight()+getWidth();
        return score;
    }
    
    public boolean Collides(Rectangle B){
        return px < B.px + B.getWidth() &&
               px + getWidth() > B.px &&
               py < B.py + B.getHeight() &&
               getHeight() + py > B.py;
    }
    
    public Color getColor(){
        Random random = new Random();
        final float hue = (float) (id*16.4371894723894123);
        final float saturation = 0.9f;//1.0 for brilliant, 0.0 for dull
        final float luminance = 1.0f; //1.0 for brighter, 0.0 for black
        return Color.getHSBColor(hue, saturation, luminance);
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
        return b.getArea()-a.getArea();
    }
}

class SortByPackingScore implements Comparator<Rectangle>
{
    public int compare(Rectangle a, Rectangle b)
    {
        return b.getPackingScore()-a.getPackingScore();
    }
}

//decreasing height widht and random

class SortByDecreasingHeight implements Comparator<Rectangle>
{
    public int compare(Rectangle a, Rectangle b)
    {
        return b.getHeight()-a.getHeight();
    }
}

class SortByDecreasingWidth implements Comparator<Rectangle>
{
    public int compare(Rectangle a, Rectangle b)
    {
        return b.getWidth()-a.getWidth();
    }
}
