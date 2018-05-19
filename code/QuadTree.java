
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

class AABB{
    float x1,y1,x2,y2;
    
    public AABB(float _x1, float _y1, float _x2, float _y2){
        x1 = _x1;
        y1 = _y1;
        x2 = _x2;
        y2 = _y2;
    }
    
    public boolean collides(Rectangle aRec){
        return x1 < aRec.px + aRec.getWidth() &&
               x2 > aRec.px &&
               y1 < aRec.py + aRec.getHeight() &&
               y2 > aRec.py;
    }
    
    public boolean canContain(Rectangle aRec){
        return aRec.px>=x1 && aRec.py>=y1 && aRec.px+aRec.getWidth() <= x2 && aRec.py+aRec.getHeight() <= y2;
    }
    
    public void extend(Rectangle aRec){
        x1 = Math.min(x1,aRec.px);
        y1 = Math.min(y1,aRec.py);
        x2 = Math.max(x2,aRec.px+aRec.getWidth());
        y2 = Math.max(y2,aRec.py+aRec.getHeight());
    }
    
    public void extend(AABB aAABB){
        x1 = Math.min(x1,aAABB.x1);
        y1 = Math.min(y1,aAABB.y1);
        x2 = Math.max(x2,aAABB.x2);
        y2 = Math.max(y2,aAABB.y2);
    }
}

public class QuadTree {
    ArrayList<Rectangle> rectangles;
    int capacity;
    AABB rectangles_bound;
    AABB container_bound;
    QuadTree tr, tl, br, bl;//top right, top left, bottom right, bottom left
    
    public QuadTree(float _x1, float _y1, float _x2, float _y2){
        rectangles = new ArrayList<>();
        capacity = 4;
        rectangles_bound = null;
        container_bound = new AABB(_x1, _y1, _x2, _y2);
        tr = null;
        tl = null;
        br = null;
        bl = null;
    }
    
    public boolean collides(Rectangle aRec){
        if(rectangles_bound!=null && rectangles_bound.collides(aRec)){
            for(Rectangle curRec : rectangles) if(curRec.Collides(aRec)) return true;
            if(tr!=null) return tr.collides(aRec) || tl.collides(aRec) || br.collides(aRec) || bl.collides(aRec);
        }
        return false;
    }
    
    public void addRectangle(Rectangle aRec){
        if(container_bound.canContain(aRec)){
            if(tl != null && tl.container_bound.canContain(aRec)){
                tl.addRectangle(aRec);
                rectangles_bound.extend(tl.rectangles_bound);
            }else if(tr != null && tr.container_bound.canContain(aRec)){
                tr.addRectangle(aRec);
                rectangles_bound.extend(tr.rectangles_bound);
            }else if(bl != null && bl.container_bound.canContain(aRec)){
                bl.addRectangle(aRec);
                rectangles_bound.extend(bl.rectangles_bound);
            }else if(br != null && br.container_bound.canContain(aRec)){
                br.addRectangle(aRec);
                rectangles_bound.extend(br.rectangles_bound);
            }else{ 
                rectangles.add(aRec);
                if(rectangles_bound==null){
                    rectangles_bound = new AABB(aRec.px,aRec.py,aRec.px+aRec.getWidth(),aRec.py+aRec.getHeight());
                }else{
                    rectangles_bound.extend(aRec);
                }      
                if(rectangles.size()>=capacity && tl==null) split();
            }
        }else{
            System.out.println("RECTANGLE DOES NOT FIT :");
            System.out.println("rectangle : "+aRec.px+","+aRec.py+" "+aRec.sx+","+aRec.sy);
            System.out.println("QuadTree : "+container_bound.x1+","+container_bound.y1+" "+container_bound.x2+","+container_bound.y2);
        }
    }
    
    public void split(){
        if(tl!=null){
            System.out.println("ALREADY SPLITTED");
            return;
        }
        float mx = (container_bound.x1+container_bound.x2)/2;
        float my = (container_bound.y1+container_bound.y2)/2;
        
        tl = new QuadTree(container_bound.x1,container_bound.y1,mx                ,my                );
        tr = new QuadTree(mx                ,container_bound.y1,container_bound.x2,my                );
        bl = new QuadTree(container_bound.x1,my                ,mx                ,container_bound.y2);
        br = new QuadTree(mx                ,my                ,container_bound.x2,container_bound.y2);
        
        for(Rectangle curRec : new ArrayList<>(rectangles)){
            if(tl.container_bound.canContain(curRec)) { tl.addRectangle(curRec); rectangles.remove(curRec); }
            if(tr.container_bound.canContain(curRec)) { tr.addRectangle(curRec); rectangles.remove(curRec); }
            if(bl.container_bound.canContain(curRec)) { bl.addRectangle(curRec); rectangles.remove(curRec); }
            if(br.container_bound.canContain(curRec)) { br.addRectangle(curRec); rectangles.remove(curRec); }
        }
    }
    
    public void visualize(Graphics2D g){
      
        for (Rectangle curRec : rectangles) {
            //System.out.println("Drawing rectangle : "+curRec.id);
            //to get rainbow, pastel colors, that are never black
            Random random = new Random();
            final float hue = (float) (curRec.id*0.1);
            final float saturation = 0.9f;//1.0 for brilliant, 0.0 for dull
            final float luminance = 1.0f; //1.0 for brighter, 0.0 for black
            final Color color = Color.getHSBColor(hue, saturation, luminance);
            g.setColor(color);    
            g.fillRect(curRec.px,curRec.py,curRec.getWidth(),curRec.getHeight());
        }
        g.setColor(new Color(255,255,255));
        if(container_bound!=null) g.drawRect((int) container_bound.x1, (int) container_bound.y1, (int)(container_bound.x2-container_bound.x1) , (int)(container_bound.y2-container_bound.y1));
        g.setColor(new Color(255,0,0));
        if(rectangles_bound!=null) g.drawRect((int) rectangles_bound.x1, (int) rectangles_bound.y1, (int)(rectangles_bound.x2-rectangles_bound.x1) , (int)(rectangles_bound.y2-rectangles_bound.y1));
        
        if(tl != null) tl.visualize(g);
        if(tr != null) tr.visualize(g);
        if(bl != null) bl.visualize(g);
        if(br != null) br.visualize(g);
    }
  
}   
