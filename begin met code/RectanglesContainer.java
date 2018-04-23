
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import static java.lang.Math.max;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class RectanglesContainer {
    int         ContainerHeight;
    boolean     RotationAllowed;
    int         RectangleAmount;
    Rectangle[] Rectangles;
    
    public void parseInput(){
        Scanner scanner = new Scanner(System.in);
        
        //parse containerheight
        scanner.next();
        scanner.next();
        if(scanner.next().equals("fixed")){
            ContainerHeight = scanner.nextInt();
        }else{
            ContainerHeight = 0;
        }
        
        //parse rotation
        scanner.next();
        scanner.next();
        RotationAllowed = scanner.next().equals("yes");
        
        //parse rectangle amount
        scanner.next();
        scanner.next();
        scanner.next();
        RectangleAmount = scanner.nextInt();
        Rectangles = new Rectangle[RectangleAmount];
        
        //parse rectangle
        for(int i=0;i<RectangleAmount;i++){
            Rectangle newRectangle = new Rectangle();
            newRectangle.sx = scanner.nextInt();
            newRectangle.sy = scanner.nextInt();
            Rectangles[i] = newRectangle;
        }
    }
    
    public void randomizePositions(){
        for (Rectangle curRec : Rectangles) {
            curRec.px = (int)(Math.random()*100);
            curRec.py = (int)(Math.random()*100);
        }
    }
    
    public void printOutput(){
        System.out.println("container height: "+(ContainerHeight==0?"free":("fixed "+ContainerHeight)));
        System.out.println("rotations allowed: "+(RotationAllowed?"yes":"no"));
        System.out.println("number of rectangles: "+RectangleAmount);
        for (Rectangle curRec : Rectangles) {
            System.out.println(curRec.sx+" "+curRec.sy);
        }
        System.out.println("placement of rectangles");
        for (Rectangle curRec : Rectangles) {
            System.out.println((RotationAllowed?(curRec.rotated?"yes ":"no "):"")+curRec.px+" "+curRec.py);
        }
    }
    
    public void visualize(){
        
        int maxx = 0;
        int maxy = 0;
        for (Rectangle curRec : Rectangles) {
            maxx = max(maxx,curRec.px+curRec.sx);
            maxy = max(maxy,curRec.py+curRec.sy);
        }
        
        BufferedImage image = new BufferedImage(maxx,maxy,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        for (Rectangle curRec : Rectangles) {
            g.setColor(new Color((int)(Math.random() * 0x1000000)));
            g.fillRect(curRec.px,curRec.py,curRec.sx,curRec.sy);
        }

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(max(maxx,500),max(maxy,500));
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
