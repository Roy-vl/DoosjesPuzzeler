
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JFrame;

class AABB{
    float x1,y1,x2,y2;
    
    public AABB(float _x1, float _y1, float _x2, float _y2){
        x1 = _x1;
        y1 = _y1;
        x2 = _x2;
        y2 = _y2;
    }
    
    @Override
    public AABB clone(){
        return new AABB(x1,y1,x2,y2);
    }
    
    public boolean collides(Rectangle aRec){
        return x1 < aRec.px + aRec.getWidth() &&
               x2 > aRec.px &&
               y1 < aRec.py + aRec.getHeight() &&
               y2 > aRec.py;
    }
    
    public boolean encapsulates(Rectangle aRec){
        return aRec.px >= x1 &&
               aRec.py >= y1 && 
               aRec.px + aRec.getWidth() <= x2 && 
               aRec.py + aRec.getHeight() <= y2;
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
    
    public float getWidth(){
        return x2-x1;
    }
    
    public float getHeight(){
        return y2-y1;
    }
    
    public float getArea(){
        return getWidth()*getHeight();
    }
}

public class QuadTree {
    private ArrayList<Rectangle> rectangles;
    private int      capacity;
    private AABB     rectangles_bound;
    AABB             container_bound;
    private int      totalRectanglesArea;
    private boolean  split;
    private QuadTree tr, tl, br, bl;//top right, top left, bottom right, bottom left
    
    public QuadTree(){
        rectangles = null;
        capacity = 0;
        rectangles_bound = null;
        container_bound = null;
        split = false;
        tr = null;
        tl = null;
        br = null;
        bl = null;
    }
    
    public QuadTree(float _x1, float _y1, float _x2, float _y2){
        rectangles = new ArrayList<>();
        capacity = 4;
        rectangles_bound = new AABB(Integer.MAX_VALUE,Integer.MAX_VALUE,-Integer.MAX_VALUE,-Integer.MAX_VALUE);
        container_bound = new AABB(_x1, _y1, _x2, _y2);
        split = false;
        tr = null;
        tl = null;
        br = null;
        bl = null;
    }
    
    @Override
    public QuadTree clone(){
        QuadTree clone = new QuadTree();
        
        clone.rectangles = new ArrayList<>();
        for(Rectangle curRec : rectangles) clone.rectangles.add(curRec.clone());
        clone.capacity = capacity;
        clone.rectangles_bound = rectangles_bound.clone();
        clone.container_bound = container_bound.clone();
        clone.split = split;
        if(split){
            clone.tr = tr.clone();
            clone.tl = tl.clone();
            clone.br = br.clone();
            clone.bl = bl.clone();
        }
        
        return clone;
    }
    
    public int getTotalRectanglesArea(){
        return totalRectanglesArea;
    }
    
    public float getCost(){
        return rectangles_bound.getArea()-getTotalRectanglesArea();
    }
  
    public boolean collides(Rectangle aRec){
        if(rectangles_bound.collides(aRec)){
            if(split){
                if(tl.collides(aRec)) return true;
                if(tr.collides(aRec)) return true;
                if(bl.collides(aRec)) return true;
                if(br.collides(aRec)) return true;
            }
            for(Rectangle curRec : rectangles) if(curRec.Collides(aRec)) return true;
        }
        return false;
    }
    
    public boolean canBePlaced(Rectangle aRec){
        return container_bound.encapsulates(aRec) && !collides(aRec);
    }
    
    public boolean collides(int px, int py){
        Rectangle pointRec = new Rectangle(-1, px, py, 1, 1, false);
        return collides(pointRec);
    }
    
    public void recalculateRectanglesBound(){
        rectangles_bound = new AABB(Integer.MAX_VALUE,Integer.MAX_VALUE,-Integer.MAX_VALUE,-Integer.MAX_VALUE);
        
        for(Rectangle curRec : rectangles) rectangles_bound.extend(curRec);
        if(split){
            rectangles_bound.extend(tl.rectangles_bound);
            rectangles_bound.extend(tr.rectangles_bound);
            rectangles_bound.extend(bl.rectangles_bound);
            rectangles_bound.extend(br.rectangles_bound);
        }

    }
    
    public void addRectangle(Rectangle aRec){
        if(container_bound.encapsulates(aRec)){
            totalRectanglesArea += aRec.getArea();

            //if split try to add it to the children
            if(split){

                if(tl.container_bound.encapsulates(aRec)){
                    tl.addRectangle(aRec);
                    rectangles_bound.extend(tl.rectangles_bound);
                    return;
                }else if(tr.container_bound.encapsulates(aRec)){
                    tr.addRectangle(aRec);
                    rectangles_bound.extend(tr.rectangles_bound);
                    return;
                }else if(bl.container_bound.encapsulates(aRec)){
                    bl.addRectangle(aRec);
                    rectangles_bound.extend(bl.rectangles_bound);
                    return;
                }else if(br.container_bound.encapsulates(aRec)){
                    br.addRectangle(aRec);
                    rectangles_bound.extend(br.rectangles_bound);
                    return;
                }
                
            }

            //if could not be added to children add to self
            rectangles.add(aRec);
            rectangles_bound.extend(aRec);
            
            //if the capacity is reached and is has not split yet; split.
            if(rectangles.size()>=capacity && !split) split();

        }else{
            System.out.println("RECTANGLE DOES NOT FIT :");
            System.out.println("rectangle : "+aRec.px+","+aRec.py+" "+aRec.sx+","+aRec.sy);
            System.out.println("QuadTree : "+container_bound.x1+","+container_bound.y1+" "+container_bound.x2+","+container_bound.y2);
        }
    }
    
    public void removeRectangle(Rectangle aRec){
        if(rectangles_bound.encapsulates(aRec)){
            if(!rectangles.remove(aRec)){
                if(split){
                    tl.removeRectangle(aRec);
                    tr.removeRectangle(aRec);
                    bl.removeRectangle(aRec);
                    br.removeRectangle(aRec);
                }
            }
            totalRectanglesArea -= aRec.getArea();
            recalculateRectanglesBound();
        }
    }
    
    public void split(){
        //System.out.println("split");
        if(split){
            System.out.println("ALREADY SPLITTED");
            return;
        }

        
        //create children
        /*
        float mx = (rectangles_bound.x1+rectangles_bound.x2)/2;
        float my = (rectangles_bound.y1+rectangles_bound.y2)/2;  
        tl = new QuadTree(container_bound.x1,container_bound.y1,mx                ,my                );
        tr = new QuadTree(mx                ,container_bound.y1,container_bound.x2,my                );
        bl = new QuadTree(container_bound.x1,my                ,mx                ,container_bound.y2);
        br = new QuadTree(mx                ,my                ,container_bound.x2,container_bound.y2);
        */
        
        
        if(container_bound.getHeight()/container_bound.getWidth() > 1.5){
            float my = container_bound.y1+container_bound.getWidth();  
            tl = new QuadTree(container_bound.x1,container_bound.y1,container_bound.x2,my);
            tr = new QuadTree(container_bound.x1,my,container_bound.x2,container_bound.y2);
            bl = new QuadTree(0,0,0,0);
            br = new QuadTree(0,0,0,0);
        }else if(container_bound.getWidth()/container_bound.getHeight() > 1.5){
            float mx = container_bound.x1+container_bound.getHeight();  
            tl = new QuadTree(container_bound.x1,container_bound.y1,mx,container_bound.y2);
            tr = new QuadTree(mx,container_bound.y1,container_bound.x2,container_bound.y2);
            bl = new QuadTree(0,0,0,0);
            br = new QuadTree(0,0,0,0);
        }else{    
            float mx = (container_bound.x1+container_bound.x2)/2;  
            float my = (container_bound.y1+container_bound.y2)/2;  
            tl = new QuadTree(container_bound.x1,container_bound.y1,mx                ,my                );
            tr = new QuadTree(mx                ,container_bound.y1,container_bound.x2,my                );
            bl = new QuadTree(container_bound.x1,my                ,mx                ,container_bound.y2);
            br = new QuadTree(mx                ,my                ,container_bound.x2,container_bound.y2);
        }
        
        //divide rectangles into children if possible
        for(Rectangle curRec : new ArrayList<>(rectangles)){
            if(tl.container_bound.encapsulates(curRec)){ tl.addRectangle(curRec); rectangles.remove(curRec); }
            if(tr.container_bound.encapsulates(curRec)){ tr.addRectangle(curRec); rectangles.remove(curRec); }
            if(bl.container_bound.encapsulates(curRec)){ bl.addRectangle(curRec); rectangles.remove(curRec); }
            if(br.container_bound.encapsulates(curRec)){ br.addRectangle(curRec); rectangles.remove(curRec); }
        }
        
        split = true;
    }
    
    public void getAllRectangles(ArrayList<Rectangle> recs){
        for(Rectangle curRec : rectangles) recs.add(curRec.clone());
        if(split){
            tl.getAllRectangles(recs);
            tr.getAllRectangles(recs);
            bl.getAllRectangles(recs);
            br.getAllRectangles(recs);
        }
    }
    
    public void printPlacement(boolean rotationAllowed){
        ArrayList<Rectangle> all_rectangles = new ArrayList<>();
        getAllRectangles(all_rectangles);
        Collections.sort(all_rectangles, new SortByID());
        System.out.println("placement of rectangles");
        for (Rectangle curRec : all_rectangles) {
            System.out.println((rotationAllowed?(curRec.rotated?"yes ":"no "):"")+curRec.px+" "+curRec.py);
        }
    }
    
    public void drawTo(Graphics2D g){
        for (Rectangle curRec : rectangles) {
            g.setColor(curRec.getColor());    
            g.fillRect(curRec.px,curRec.py,curRec.getWidth(),curRec.getHeight());
        }
        g.setColor(new Color(255,255,255));
        g.drawRect((int) container_bound.x1, (int) container_bound.y1, (int)(container_bound.getWidth()) , (int)(container_bound.getHeight()));
        g.setColor(new Color(255,0,0));
        g.drawRect((int) rectangles_bound.x1, (int) rectangles_bound.y1, (int)(rectangles_bound.getWidth()) , (int)(rectangles_bound.getHeight()));
        
        if(split){
            tl.drawTo(g);
            tr.drawTo(g);
            bl.drawTo(g);
            br.drawTo(g);
        }
    }
    
    public BufferedImage createImage(){
        int maxx = (int) (rectangles_bound.getWidth());
        int maxy = (int) (rectangles_bound.getHeight());
      
        
        int windowSizeX = 1000;
        int windowSizeY = 1000;

        float scale = maxx>=maxy ? (float)windowSizeX/maxx : (float)windowSizeY/maxy;
             
        //create an image and its graphics 
        BufferedImage image = new BufferedImage(maxx,maxy,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        
        //black background
        g.setColor(new Color(0,0,0));
        g.fillRect(0,0,maxx,maxy);
        
        //recursive drawing
        drawTo(g);
        
        return image; 
    }
    
    public void visualize(){    
        //create an image and its graphics 
        BufferedImage image = createImage();
      
        //make a new windows frame
        JFrame window = new JFrame("  Bounding Dimensions : " + (int)(rectangles_bound.getWidth()) + "," + (int)(rectangles_bound.getHeight()) 
                + ", Bounding Area : " + (int)(rectangles_bound.getArea())
                + ", Rectangles Area :" + getTotalRectanglesArea()
                + ", Cost : " + (int)(getCost()));

        //create a zoomable pane
        ZoomableScrollPane imageZoom = new ZoomableScrollPane(image, 800, 800);     

        window.setContentPane(imageZoom);
        window.pack();
        window.setVisible(true);
    }
  
}   
