
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
    
    public int getDistance(){
        return (px+sx)*(px+sx)+(py+sy)*(py+sy);
    }
    
    Rectangle rec;
    
    Node node1;
    Node node2;
}

public class PackGuillotine implements PackerStrategy{
    
    Node                mainNode;
    RectanglesContainer curRC;
    
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
                
                curRC.addRectangle(aRec);
                
                return true;
            }else{
                if(aNode.node1.getDistance() < aNode.node2.getDistance()){
                    return  placeRec(aNode.node1,aRec) ? true : placeRec(aNode.node2,aRec); 
                }else{
                    return  placeRec(aNode.node2,aRec) ? true : placeRec(aNode.node1,aRec); 
                }
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
        RectanglesContainer bestRC = new RectanglesContainer();
        int                 bestArea = Integer.MAX_VALUE;;
        
        for(int i=0;i<1000;i++){
            curRC    = new RectanglesContainer();
            if(PS.getContainerHeight()>0) curRC.setForcedBoundingHeight(PS.getContainerHeight());
            
            mainNode = new Node(0,0,100+i,PS.getContainerHeight()>0 ? PS.getContainerHeight() : 100);

            Rectangle[] rectangles = PS.getRectangles();
            PriorityQueue<Rectangle> toPlace = new PriorityQueue<>(rectangles.length,new SortByArea());
            for(Rectangle curRec : rectangles) toPlace.add(curRec);

            while(toPlace.peek()!=null){
                if(curRC.getBoundingArea()>bestArea) break;
                if(!placeRec(mainNode,toPlace.poll())) break;
            }

            if(toPlace.isEmpty()){
                int newArea = curRC.getBoundingArea();
                if(newArea<bestArea){
                    bestArea = newArea;
                    bestRC = curRC.clone();
                }
            }
        }
        
        return bestRC;
    }
}

