
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import static java.lang.Math.max;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class RectanglesContainer {
    int         containerHeight;
    boolean     rotationAllowed;
    int         rectangleAmount;
    Rectangle[] rectangles;
    
    
    @Override
    public RectanglesContainer clone(){
        RectanglesContainer clone = new RectanglesContainer();
        clone.containerHeight = this.containerHeight;
        clone.rotationAllowed = this.rotationAllowed;
        clone.rectangleAmount = this.rectangleAmount;
        clone.rectangles = new Rectangle[rectangleAmount];
        for(int i = 0; i < rectangleAmount; i++){
            clone.rectangles[i] = this.rectangles[i].clone();
        }
        return clone;
    }
    
    public void parseInput(){
        Scanner scanner = new Scanner(System.in);
        
        //parse containerheight
        scanner.next();
        scanner.next();
        if(scanner.next().equals("fixed")){
            containerHeight = scanner.nextInt();
        }else{
            containerHeight = 0;
        }
        
        //parse rotation
        scanner.next();
        scanner.next();
        rotationAllowed = scanner.next().equals("yes");
        
        //parse rectangle amount
        scanner.next();
        scanner.next();
        scanner.next();
        rectangleAmount = scanner.nextInt();
        rectangles = new Rectangle[rectangleAmount];
        
        //parse rectangles
        for(int i=0;i<rectangleAmount;i++){
            Rectangle newRectangle = new Rectangle();
            newRectangle.sx = scanner.nextInt();
            newRectangle.sy = scanner.nextInt();
            newRectangle.id = i;
            rectangles[i] = newRectangle;
        }
    }
    
    public void parseCustomInput(String file){
        Scanner scanner = null;
        try {
            File readFile = new File(file);
            scanner = new Scanner(readFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RectanglesContainer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //parse containerheight
        scanner.next();
        scanner.next();
        if(scanner.next().equals("fixed")){
            containerHeight = scanner.nextInt();
        }else{
            containerHeight = 0;
        }
        
        //parse rotation
        scanner.next();
        scanner.next();
        rotationAllowed = scanner.next().equals("yes");
        
        //parse rectangle amount
        scanner.next();
        scanner.next();
        scanner.next();
        rectangleAmount = scanner.nextInt();
        rectangles = new Rectangle[rectangleAmount];
        
        //parse rectangles
        for(int i=0;i<rectangleAmount;i++){
            Rectangle newRectangle = new Rectangle();
            newRectangle.sx = scanner.nextInt();
            newRectangle.sy = scanner.nextInt();
            newRectangle.id = i;
            rectangles[i] = newRectangle;
        }
    }
    
    
    public int getTotalWidth(){
        int tx = 0;
        for (Rectangle curRec : rectangles) {
            tx = max(tx,curRec.px+curRec.getWidth());
        }
        return tx;
    }
    
    public int getTotalHeight(){
        int ty = 0;
        for (Rectangle curRec : rectangles) {
            ty = max(ty, curRec.py + curRec.getHeight());
        }
        return max(ty, containerHeight);
    }
    
    public int getBoundingArea(){
        return getTotalWidth()*getTotalHeight();
    }
    
    public int getTotalArea(){
        int a = 0;
        for (Rectangle curRec : rectangles) {
            a += curRec.getArea();
        }
        return a;
    }
    
    public int getCost(){
        return getBoundingArea()-getTotalArea();
    }
    
    public void randomizePositions(){
        for (Rectangle curRec : rectangles) {
            curRec.px = (int)(Math.random()*100);
            curRec.py = (int)(Math.random()*100);
        }
    }
    
    public void resetRectangles(){
        for (Rectangle curRec : rectangles){
            curRec.px = 0;
            curRec.py = 0;
            curRec.placed = false;
        }
    }
    
    public boolean checkCollision(Rectangle aRec){
        boolean collision = false;
        for(Rectangle curRec : rectangles){
            if(curRec.placed){
                collision = collision || curRec.Collides(aRec);
            }
        }
        return collision;
    }
    
    public void sortRectangles(Comparator<Rectangle> C){
        Arrays.sort(rectangles, C);
    }
    
    public void printOutput(){
        //first sort by id to get original listing
        sortRectangles(new SortByID());
        //print output
        System.out.println("container height: "+(containerHeight==0?"free":("fixed "+containerHeight)));
        System.out.println("rotations allowed: "+(rotationAllowed?"yes":"no"));
        System.out.println("number of rectangles: "+rectangleAmount);
        for (Rectangle curRec : rectangles) {
            System.out.println(curRec.sx+" "+curRec.sy);
        }
        System.out.println("placement of rectangles");
        for (Rectangle curRec : rectangles) {
            System.out.println((rotationAllowed?(curRec.rotated?"yes ":"no "):"")+curRec.px+" "+curRec.py);
        }
    }
    
    public void visualize(){
        
        int windowSizeX = 1000;
        int windowSizeY = 1000;
        
        int maxx = getTotalWidth();
        int maxy = getTotalHeight();
        
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
     
        ////create white outlines
        //g.setColor(new Color(255,255,255));
        //for (Rectangle curRec : Rectangles) {
           // g.drawRect(curRec.px,curRec.py,curRec.getWidth(),curRec.getHeight());
        //}
        
        //create window
        JFrame frame = new JFrame();
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

