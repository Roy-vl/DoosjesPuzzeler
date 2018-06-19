import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JFrame;

public class QuadTree {
    public ArrayList<Rectangle> rectangles;
    public int      capacity;
    public AABB     rectanglesBound;
    public  int     forcedRectanglesBoundHeight;
    public AABB     containerBound;
    public int      totalRectanglesArea;
    public boolean  split;
    public QuadTree tr, tl, br, bl;//top right, top left, bottom right, bottom left
    
    public QuadTree(){
        rectangles = null;
        capacity = 0;
        rectanglesBound = null;
        forcedRectanglesBoundHeight = 0;
        containerBound = null;
        totalRectanglesArea = 0;
        split = false;
        tr = null;
        tl = null;
        br = null;
        bl = null;
    }
    
    public QuadTree(int _x1, int _y1, int _x2, int _y2){
        rectangles = new ArrayList<>();
        capacity = 4;
        rectanglesBound = new AABB(Integer.MAX_VALUE,Integer.MAX_VALUE,-Integer.MAX_VALUE,-Integer.MAX_VALUE);
        forcedRectanglesBoundHeight = 0;
        containerBound = new AABB(_x1, _y1, _x2, _y2);
        split = false;
        totalRectanglesArea = 0;
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
        clone.rectanglesBound = rectanglesBound.clone();
        clone.forcedRectanglesBoundHeight = forcedRectanglesBoundHeight;
        clone.containerBound = containerBound.clone();
        clone.totalRectanglesArea = totalRectanglesArea;
        clone.split = split;
        if(split){
            clone.tr = tr.clone();
            clone.tl = tl.clone();
            clone.br = br.clone();
            clone.bl = bl.clone();
        }
        
        return clone;
    }
    
    public int getRectanglesBoundArea(){
        if(forcedRectanglesBoundHeight==0){
            return rectanglesBound.getArea();
        }else{
            return rectanglesBound.getWidth()*forcedRectanglesBoundHeight;
        }
    }
    
    public int getTotalRectanglesArea(){
        return totalRectanglesArea;
    }
    
    public int getCost(){
        return getRectanglesBoundArea()-getTotalRectanglesArea();
    }
  
    public boolean collides(Rectangle aRec){
        if(rectanglesBound.collides(aRec)){
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
        return containerBound.encapsulates(aRec) && !collides(aRec);
    }
    
    public boolean collides(int px, int py){
        Rectangle pointRec = new Rectangle(-1, px, py, 1, 1, false);
        return collides(pointRec);
    }
    
    public void recalculateRectanglesBound(){
        rectanglesBound = new AABB(Integer.MAX_VALUE,Integer.MAX_VALUE,-Integer.MAX_VALUE,-Integer.MAX_VALUE);
        
        for(Rectangle curRec : rectangles) rectanglesBound.extend(curRec);
        if(split){
            rectanglesBound.extend(tl.rectanglesBound);
            rectanglesBound.extend(tr.rectanglesBound);
            rectanglesBound.extend(bl.rectanglesBound);
            rectanglesBound.extend(br.rectanglesBound);
        }

    }
    
    public void addRectangle(Rectangle aRec){
        if(containerBound.encapsulates(aRec)){
            totalRectanglesArea += aRec.getArea();

            //if split try to add it to the children
            if(split){

                if(tl.containerBound.encapsulates(aRec)){
                    tl.addRectangle(aRec);
                    rectanglesBound.extend(tl.rectanglesBound);
                    return;
                }else if(tr.containerBound.encapsulates(aRec)){
                    tr.addRectangle(aRec);
                    rectanglesBound.extend(tr.rectanglesBound);
                    return;
                }else if(bl.containerBound.encapsulates(aRec)){
                    bl.addRectangle(aRec);
                    rectanglesBound.extend(bl.rectanglesBound);
                    return;
                }else if(br.containerBound.encapsulates(aRec)){
                    br.addRectangle(aRec);
                    rectanglesBound.extend(br.rectanglesBound);
                    return;
                }
                
            }

            //if could not be added to children add to self
            rectangles.add(aRec);
            rectanglesBound.extend(aRec);
            
            //if the capacity is reached and is has not split yet; split.
            if(rectangles.size()>=capacity && !split) split();

        }else{
            System.out.println("RECTANGLE DOES NOT FIT :");
            System.out.println("rectangle : "+aRec.px+","+aRec.py+" "+aRec.sx+","+aRec.sy);
            System.out.println("QuadTree : "+containerBound.x1+","+containerBound.y1+" "+containerBound.x2+","+containerBound.y2);
        }
    }
    
    public void removeRectangle(Rectangle aRec){
        if(rectanglesBound.encapsulates(aRec)){
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
            //System.out.println("ALREADY SPLITTED");
            return;
        }

        
        //create children
        /*
        int mx = (rectangles_bound.x1+rectangles_bound.x2)/2;
        int my = (rectangles_bound.y1+rectangles_bound.y2)/2;  
        tl = new QuadTree(container_bound.x1,container_bound.y1,mx                ,my                );
        tr = new QuadTree(mx                ,container_bound.y1,container_bound.x2,my                );
        bl = new QuadTree(container_bound.x1,my                ,mx                ,container_bound.y2);
        br = new QuadTree(mx                ,my                ,container_bound.x2,container_bound.y2);
        */
        
        
        if(containerBound.getHeight()/containerBound.getWidth() > 1.5){
            int my = containerBound.y1+containerBound.getWidth();  
            tl = new QuadTree(containerBound.x1,containerBound.y1,containerBound.x2,my);
            tr = new QuadTree(containerBound.x1,my,containerBound.x2,containerBound.y2);
            bl = new QuadTree(0,0,0,0);
            br = new QuadTree(0,0,0,0);
        }else if(containerBound.getWidth()/containerBound.getHeight() > 1.5){
            int mx = containerBound.x1+containerBound.getHeight();  
            tl = new QuadTree(containerBound.x1,containerBound.y1,mx,containerBound.y2);
            tr = new QuadTree(mx,containerBound.y1,containerBound.x2,containerBound.y2);
            bl = new QuadTree(0,0,0,0);
            br = new QuadTree(0,0,0,0);
        }else{    
            int mx = (containerBound.x1+containerBound.x2)/2;  
            int my = (containerBound.y1+containerBound.y2)/2;  
            tl = new QuadTree(containerBound.x1,containerBound.y1,mx                ,my                );
            tr = new QuadTree(mx                ,containerBound.y1,containerBound.x2,my                );
            bl = new QuadTree(containerBound.x1,my                ,mx                ,containerBound.y2);
            br = new QuadTree(mx                ,my                ,containerBound.x2,containerBound.y2);
        }
        
        //divide rectangles into children if possible
        for(Rectangle curRec : new ArrayList<>(rectangles)){
            if(tl.containerBound.encapsulates(curRec)){ tl.addRectangle(curRec); rectangles.remove(curRec); }
            if(tr.containerBound.encapsulates(curRec)){ tr.addRectangle(curRec); rectangles.remove(curRec); }
            if(bl.containerBound.encapsulates(curRec)){ bl.addRectangle(curRec); rectangles.remove(curRec); }
            if(br.containerBound.encapsulates(curRec)){ br.addRectangle(curRec); rectangles.remove(curRec); }
        }
        
        split = true;
    }
    
    public void getAllRectangles(ArrayList<Rectangle> recs){
        for(Rectangle curRec : rectangles) recs.add(curRec);
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
    
    public void drawTo(Graphics2D g, int scale){

        for (Rectangle curRec : rectangles) {
            final float hue = (float) (curRec.id*1.61803398875);
            final float saturation = .6f;//1.0 for brilliant, 0.0 for dull
            final float luminance = .8f;; //1.0 for brighter, 0.0 for black
            final Color color = Color.getHSBColor(hue, saturation, luminance);
            g.setColor(color);    
            g.fillRect(curRec.px*scale,curRec.py*scale,curRec.getWidth()*scale,curRec.getHeight()*scale);
        }
       
        
        g.setColor(new Color(255,0,0));
        g.drawRect( rectanglesBound.x1*scale, rectanglesBound.y1*scale, rectanglesBound.getWidth()*scale, rectanglesBound.getHeight()*scale);
        g.setColor(new Color(255,255,255));
        g.drawRect( containerBound.x1*scale, containerBound.y1*scale, containerBound.getWidth()*scale, containerBound.getHeight()*scale);
        
        if(split){
            tl.drawTo(g,scale);
            tr.drawTo(g,scale);
            bl.drawTo(g,scale);
            br.drawTo(g,scale);
        }
    }
    
    public void visualize(){    
        //make a new windows frame
        JFrame window = new JFrame("  Bounding Dimensions : " + (int)(rectanglesBound.getWidth()) + "," + (int)(rectanglesBound.getHeight()) 
                + ", Bounding Area : " + getRectanglesBoundArea()
                + ", Rectangles Area :" + getTotalRectanglesArea()
                + ", Cost : " + getCost() );

        //create a zoomable pane
        ZoomableScrollPane imageZoom = new ZoomableScrollPane(this, 800, 800,10);     

        window.setContentPane(imageZoom);
        window.pack();
        window.setVisible(true);
    }
  
}   
