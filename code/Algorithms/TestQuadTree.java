
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class TestQuadTree implements PackerStrategy{
    
    @Override
    public RectanglesContainer pack(ProblemStatement PS){
        RectanglesContainer RC = new RectanglesContainer();
        
        int maxx = 10000;
        int maxy = 10000;

        QuadTree Q = new QuadTree(0,0,maxx,maxy);
        for(Rectangle curRec : PS.getRectangles()){
            Q.addRectangle(curRec);
        }
        
        int windowSizeX = 1000;
        int windowSizeY = 1000;

        
        float scale = maxx>=maxy ? (float)windowSizeX/maxx : (float)windowSizeY/maxy;
             
        //create an image and its graphics 
        BufferedImage image = new BufferedImage(maxx,maxy,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        
        //black background
        g.setColor(new Color(0,0,0));
        g.fillRect(0,0,maxx,maxy);
        
        Q.visualize(g);
        
        //create window
        JFrame frame = new JFrame();
        frame.setTitle("Bounding Size : "+maxx+","+maxy);
        
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
        
        return RC;
    }
}

