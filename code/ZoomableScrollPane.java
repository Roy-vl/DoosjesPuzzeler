import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author Thanasis1101
 * @version 1.0
 */
public class ZoomableScrollPane extends JPanel implements MouseWheelListener, MouseListener, MouseMotionListener {

    private final BufferedImage image;

    private double zoomFactor = 1;
    private double prevZoomFactor = 1;
    private double xOffset = 0;
    private double yOffset = 0;
    private Point startPoint;

    public ZoomableScrollPane(BufferedImage image, int w, int h) {
        setPreferredSize(new Dimension(w,h));
        
        this.image = image;
        initComponent();
        
        zoomFactor = Math.min((double)(w)/image.getWidth(),(double)(h)/image.getHeight());
        
    }

    private void initComponent() {
        addMouseWheelListener(this);
        addMouseMotionListener(this);
        addMouseListener(this);
    }
    
    @Override
    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        
        AffineTransform at = new AffineTransform();
        at.translate(xOffset, yOffset);
        at.scale(zoomFactor, zoomFactor);
        g2.transform(at);
        
        g2.drawImage(image, 0, 0, this);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

        double zoomDiv = 1;
        if (e.getWheelRotation() < 0) zoomDiv *= 1.1;
        if (e.getWheelRotation() > 0) zoomDiv /= 1.1;
        
        double xRel = MouseInfo.getPointerInfo().getLocation().getX() - getLocationOnScreen().getX();
        double yRel = MouseInfo.getPointerInfo().getLocation().getY() - getLocationOnScreen().getY();

        xOffset = (zoomDiv) * (xOffset) + (1 - zoomDiv) * xRel;
        yOffset = (zoomDiv) * (yOffset) + (1 - zoomDiv) * yRel;

        zoomFactor *= zoomDiv;
        
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point curPoint = e.getLocationOnScreen();
        xOffset += curPoint.x - startPoint.x;
        yOffset += curPoint.y - startPoint.y;
        startPoint = curPoint;
        
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        startPoint = e.getLocationOnScreen();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}