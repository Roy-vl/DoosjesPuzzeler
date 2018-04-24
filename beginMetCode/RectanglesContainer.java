
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import static java.lang.Math.max;
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
        
        PackerHelper helper = new PackerHelper(strategy);
        helper.pack(rotationAllowed, rectangles, containerHeight);
    }
    
    /*
    public void packNextToEachother(){
        int x=0;
        for (Rectangle curRec : rectangles) {
            curRec.px=x;
            curRec.rotated = rotationAllowed?(curRec.sx>curRec.sy):false;
            x += curRec.getWidth();
        }
    }
    */
    
    
    public void tetrisPack(){
        
        int[] widths = new int[containerHeight];    
        
        for(Rectangle curRec : rectangles){

            //rotate such that it is horizontal
            curRec.rotated = rotationAllowed&&curRec.sx<=containerHeight&&(curRec.sx<curRec.sy);
            
            //find lowest point in the widths
            int ty = 0;
            int tx = widths[0];
            for(int w=0;w<widths.length-curRec.getHeight();w++){
                if(widths[w]<tx){
                    ty = w;
                    tx = widths[w];
                }
            }
            
            boolean placed = false;
            while(!placed){

                //check if it can be placed at ty,tx
                boolean canBePlaced = true;
                for(int i=ty; i<ty+curRec.getHeight(); i++){
                    canBePlaced = canBePlaced && widths[i]<=tx;
                }
                
                //place it
                if(canBePlaced){
                    curRec.px = tx;
                    curRec.py = ty;
                    
                    for(int i=ty; i<ty+curRec.getHeight(); i++){
                        widths[i]=curRec.px+curRec.getWidth();
                    }
                    
                    placed = true;
                 
                //continue search
                }else{
                    ty++;
                    if(ty>=containerHeight-curRec.getHeight()){
                        ty=0;
                        tx++;
                    }
                }
            }  
            
        }    
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
    
    public void visualize(){
        
        int maxx = getTotalWidth();
        int maxy = getTotalHeight()+1;//1 extra for the containerborder
        
        //create an image and its graphics 
        BufferedImage image = new BufferedImage(maxx,maxy,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        
        //black background
        g.setColor(new Color(0,0,0));
        g.fillRect(0,0,maxx,maxy);

        //color rectangles
        for (Rectangle curRec : rectangles) {
            g.setColor(new Color((int)(Math.random() * 0x1000000)));
            g.fillRect(curRec.px,curRec.py,curRec.getWidth(),curRec.getHeight());
        }
        

        ////create white outline
        //g.setColor(new Color(255,255,255));
        //for (Rectangle curRec : Rectangles) {
           // g.drawRect(curRec.px,curRec.py,curRec.getWidth(),curRec.getHeight());
        //}
        
        //red containerborder
        if(containerHeight>0){
            g.setColor(new Color(255,0,0));
            g.fillRect(0,containerHeight-1,maxx,1);
        }
        
        //create window
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(maxx+16,maxy+39);//+16,+40 for windows bullshit
        frame.setVisible(true);

        frame.add(new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, null);
            }
        });
    }

}

