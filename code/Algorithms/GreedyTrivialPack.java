import java.util.Arrays;
        
class DoublyLinkedNode{
    boolean filled;
    int x;
    int y;
    DoublyLinkedNode previous;
    DoublyLinkedNode next;
    
    public DoublyLinkedNode(int _x, int _y){
        filled = false;
        x = _x;
        y = _y;
        previous = null;
        next = null; 
    }
}

public class GreedyTrivialPack implements PackerStrategy{

    int width;
    int height;
    DoublyLinkedNode[][] spots;
 
    public boolean canBePlacedAt(int tx, int ty, Rectangle R){
        if(tx+R.getWidth()>width || ty+R.getHeight()>height) return false;//out of bounds
        for(int x = tx; x < tx+R.getWidth(); x++){
        for(int y = ty; y < ty+R.getHeight(); y++){
            if(spots[x][y].filled) return false;
        }
        }
        return true;
    }
    
    public void fillSpots(Rectangle R){
        for(int x = R.px; x < R.px+R.getWidth(); x++){
        for(int y = R.py; y < R.py+R.getHeight(); y++){
            spots[x][y].filled = true;
        }
        }
        
        for(int x = R.px; x < R.px+R.getWidth(); x++){
            spots[x][R.py].previous.next = spots[x][R.py+R.getHeight()-1].next;
            spots[x][R.py+R.getHeight()-1].next.previous = spots[x][R.py].previous;
        }
    }
    
    @Override
    public boolean applicable(ProblemStatement PS){
        if(PS.getContainerHeight()==0){
            return false;
        }   
        return true;
    }
    
    @Override
    public RectanglesContainer pack(ProblemStatement PS){
        
        width = 10000;
        height = PS.getContainerHeight();
              
        RectanglesContainer RC = new RectanglesContainer();
        RC.setForcedBoundingHeight(height);

        spots = new DoublyLinkedNode[width][height];
        
        //link the list
        DoublyLinkedNode begin =  new DoublyLinkedNode(0,-1);
        DoublyLinkedNode previousnode = begin;   
        for(int x=0;x<width;x++)
        for(int y=0;y<height;y++){
            spots[x][y] = new DoublyLinkedNode(x,y);
            
            previousnode.next = spots[x][y];
            spots[x][y].previous = previousnode;
            
            previousnode = spots[x][y];
        }
        
        //prepare the rectangles
        Rectangle[] rectangles = PS.getRectangles();
        if(PS.getRotationAllowed()){
            for(Rectangle curRec : rectangles) if(curRec.sy > curRec.sx) curRec.rotated = true;
        }
        Arrays.sort(rectangles,new SortByArea());
        Arrays.sort(rectangles,new SortByDecreasingWidth());
        
        for(Rectangle curRec : rectangles){

            boolean placed = false;
            DoublyLinkedNode n = begin.next;  
            while(!placed){                  
                if(canBePlacedAt(n.x,n.y,curRec)){
                    curRec.px = n.x;
                    curRec.py = n.y;
                    fillSpots(curRec);
                    RC.addRectangle(curRec);
                    placed = true;     
                }
                n = n.next;        
            }
             
            if(!placed){
                System.out.println("RECTANGLE "+curRec.id+" COULD NOT BE PLACED");
                System.out.println("Rectangle dimensions :"+curRec.sx+","+curRec.sy);
                System.out.println("Container dimensions :"+width+","+height);
            }

        }
         
        return RC;
    }
}