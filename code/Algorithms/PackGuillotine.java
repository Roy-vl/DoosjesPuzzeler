
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

class Node{
    int px;
    int py;
    int sx;
    int sy;
    
    public Node(int _px, int _py, int _sx, int _sy){
        px = _px;
        py = _py;
        sx = _sx;
        sy = _sy;
    }
    
    Node node1;
    Node node2;
}

public class PackGuillotine implements PackerStrategy{
    
    ArrayList<Rectangle> toPlace;
    
    @Override
    public RectanglesContainer pack(ProblemStatement PS){
        RectanglesContainer RC = new RectanglesContainer();
        
        Node startNode = new Node(0,0,100000,PS.getContainerHeight()>0 ? PS.getContainerHeight() : 1000000);
        
        toPlace = new ArrayList<>(Arrays.asList(PS.getRectangles()));
        Collections.sort(toPlace, new SortByArea());
        
        return RC;
    }
}

