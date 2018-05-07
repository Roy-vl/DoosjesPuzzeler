
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import static java.lang.Math.max;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class RectanglesContainer{
    public ArrayList<Rectangle> rectangles;
    private int boundingWidth;
    private int boundingHeight;
    
    public RectanglesContainer(){
        rectangles = new ArrayList<>();
        boundingWidth = 0;
        boundingHeight = 0;
    }
    
    public int getBoundingWidth(){
        return boundingWidth;
    }
    
    public int getBoundingHeight(){
        return boundingHeight;
    }
    
    public int getBoundingArea(){
        return boundingWidth*boundingHeight;
    }
    
    public int getRectanglesArea(){
        int a = 0;
        for (Rectangle curRec : rectangles) a += curRec.getArea();
        return a;
    }
    
    public int getCost(){
        return getBoundingArea()-getRectanglesArea();
    }
      
    public boolean checkCollision(Rectangle aRec){
        boolean collision = false;
        for(Rectangle curRec : rectangles) collision = collision || curRec.Collides(aRec);
        return collision;
    }
    
    public void sortRectangles(Comparator<Rectangle> C){
        Collections.sort(rectangles, C);
    }
    
    public void addRectangle(Rectangle aRec){
        rectangles.add(aRec);
        boundingWidth  = max(boundingWidth, aRec.px+aRec.getWidth());
        boundingHeight = max(boundingHeight,aRec.px+aRec.getHeight());
    }
    
    public void printOutput(boolean rotationAllowed){
        System.out.println("placement of rectangles");
        for (Rectangle curRec : rectangles) {
            System.out.println((rotationAllowed?(curRec.rotated?"yes ":"no "):"")+curRec.px+" "+curRec.py);
        }
    }
    
    public void visualize(){
        
        int windowSizeX = 1000;
        int windowSizeY = 1000;
        
        int maxx = getBoundingWidth();
        int maxy = getBoundingHeight();
        
        float scale = maxx>=maxy ? (float)windowSizeX/maxx : (float)windowSizeY/maxy;
             
        //create an image and its graphics 
        BufferedImage image = new BufferedImage(maxx,maxy,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        
        //black background
        g.setColor(new Color(0,0,0));
        g.fillRect(0,0,maxx,maxy);

        //color rectangles
        for (Rectangle curRec : rectangles) {
            //to get rainbow, pastel colors, that are never black
            Random random = new Random();
            final float hue = random.nextFloat();
            final float saturation = 0.9f;//1.0 for brilliant, 0.0 for dull
            final float luminance = 1.0f; //1.0 for brighter, 0.0 for black
            final Color color = Color.getHSBColor(hue, saturation, luminance);
            g.setColor(color);    
            g.fillRect(curRec.px,curRec.py,curRec.getWidth(),curRec.getHeight());
        }
        
        //create window
        JFrame frame = new JFrame();
        frame.setTitle("Bounding Size : "+getBoundingWidth()+","+getBoundingHeight()+" with area : "+getBoundingArea()+" and cost of : "+getCost());
        
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(windowSizeX+16,windowSizeY+39);//+16,+40 for windows bullshit
        frame.setVisible(true);

        frame.add(new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, (int)(maxx*scale), (int)(maxy*scale), 0, 0, maxx, maxy, null);
            }
        });
    }

}

