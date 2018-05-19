
import java.util.Random;

public class TestQuadTree implements PackerStrategy{
    
    @Override
    public RectanglesContainer pack(ProblemStatement PS){
        RectanglesContainer RC = new RectanglesContainer();

        QuadTree Q = new QuadTree(0,0,1000,1000);
        for(Rectangle curRec : PS.getRectangles()){
            curRec.px = (new Random()).nextInt(900);
            curRec.py = (new Random()).nextInt(900);
            Q.addRectangle(curRec.clone());
        }

        Q.visualize();
        
        return RC;
    }
}

