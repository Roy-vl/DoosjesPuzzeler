
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.PriorityQueue;

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
        rec = null;
        node1 = null;
        node2 = null;
    }
    
    Rectangle rec;
    
    Node node1;
    Node node2;
}

public class PackGuillotine implements PackerStrategy{
    
    Node mainNode;
    
    public boolean placeRec(Node aNode, Rectangle aRec){
        if(aRec.getWidth()<aNode.sx && aRec.getHeight()<aNode.sy){
            if(aNode.rec==null){
                aRec.px = aNode.px;
                aRec.py = aNode.py;
                aNode.rec = aRec;
                
                aNode.node1 = new Node(
                        aNode.px+aRec.getWidth(),
                        aNode.py,
                        aNode.sx-aRec.getWidth(),
                        aNode.sy
                );
                aNode.node2 = new Node(
                        aNode.px,
                        aNode.py+aRec.getHeight(),
                        aRec.getWidth(),
                        aNode.sy-aRec.getHeight()
                );
                return true;
            }else{
                return placeRec(aNode.node1,aRec) ? true : placeRec(aNode.node2,aRec) ; 
            }
        }
        return false;
    }
    
    public void fromNodesToRC(Node aNode, RectanglesContainer RC){
        if(aNode.rec!=null) RC.addRectangle(aNode.rec);
        if(aNode.node1!=null) fromNodesToRC(aNode.node1, RC);
        if(aNode.node2!=null) fromNodesToRC(aNode.node2, RC);
    }
    
    @Override
    public RectanglesContainer pack(ProblemStatement PS){
        RectanglesContainer RC = new RectanglesContainer();
        
        mainNode = new Node(0,0,100000,PS.getContainerHeight()>0 ? PS.getContainerHeight() : 1000000);
        
        Rectangle[] rectangles = PS.getRectangles();
        PriorityQueue<Rectangle> toPlace = new PriorityQueue<>(rectangles.length,new SortByArea());
        for(Rectangle curRec : rectangles) toPlace.add(curRec);
        
        while(toPlace.peek()!=null){
            placeRec(mainNode,toPlace.poll());
        }
        
        fromNodesToRC(mainNode, RC);
        
        return RC;
    }
}

