
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import static java.lang.Math.max;
import static java.lang.Math.min;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class RectanglesContainer {
    int         containerHeight;
    boolean     rotationAllowed;
    int         rectangleAmount;
    Rectangle[] rectangles;
    
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
        
        //parse rectangle
        for(int i=0;i<rectangleAmount;i++){
            Rectangle newRectangle = new Rectangle();
            newRectangle.sx = scanner.nextInt();
            newRectangle.sy = scanner.nextInt();
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
            ty = max(ty,curRec.py+curRec.getHeight());
        }
        return ty;
    }
    
    public void randomizePositions(){
        for (Rectangle curRec : rectangles) {
            curRec.px = (int)(Math.random()*100);
            curRec.py = (int)(Math.random()*100);
        }
    }
    
    void pack(PackerStrategy strategy) {

        strategy.pack(rectangles, rotationAllowed, containerHeight);
        
    }
    
    public void printOutput(){
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
    
    public ArrayList<Color> getRainbow(int steps){
        int fadeLength = (steps/6);
        ArrayList<Color> colors = new ArrayList<>();
        for (int r=0; r<fadeLength; r++) colors.add(new Color( r*255/fadeLength, 255, 0));
        for (int g=fadeLength; g>0; g--) colors.add(new Color( 255, g*255/fadeLength, 0));
        for (int b=0; b<fadeLength; b++) colors.add(new Color( 255, 0, b*255/fadeLength));
        for (int r=fadeLength; r>0; r--) colors.add(new Color( r*255/fadeLength, 0, 255));
        for (int g=0; g<fadeLength; g++) colors.add(new Color( 0, g*255/fadeLength, 255));
        for (int b=fadeLength; b>0; b--) colors.add(new Color( 0, 255, b*255/fadeLength));
        colors.add(new Color( 0, 255, 0));
        return colors;
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
        ArrayList<Color> colors = getRainbow(min(maxx,1920));
        int colorSize = colors.size();
        for (Rectangle curRec : rectangles) {
            int distance = (curRec.px+curRec.py)/2;
            
            if(distance < colorSize){
                g.setColor(colors.get(distance));
            } else {
                g.setColor(colors.get(colorSize-1));
            }
            
            g.fillRect(curRec.px,curRec.py,curRec.getWidth(),curRec.getHeight());
        }
        
        
        //create white outlines
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

