import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


class PotHeight{
    int potHeight;
    int potCost;
    PotHeight(int h, int c){
        potHeight = h;
        potCost = c;
    }
}

class SortPotentials implements Comparator<PotHeight> {
    public int compare(PotHeight a, PotHeight b) {
        return a.potCost - b.potCost;
    }
}


public class MultipleGreedyCornerPack implements PackerStrategy{
    
    @Override
    public boolean applicable(ProblemStatement PS){
        if(PS.getContainerHeight()>0){
            return false;
        }   
        return true;
    }
    
    
    @Override
    public RectanglesContainer pack(ProblemStatement PS){
        
        
        long startTime = System.currentTimeMillis();
        
        int maximumHeight = 0;
        int minimumHeight = 0;
        
        int rectanglesArea = PS.getRectanglesArea();
        
        for(Rectangle curRec : PS.getRectangles()){
            if(PS.getRotationAllowed()){
                maximumHeight += Math.max(curRec.sx,curRec.sy);
                minimumHeight = Math.max(minimumHeight,Math.min(curRec.sx,curRec.sy));
            }else{
                maximumHeight += curRec.getHeight();
                minimumHeight = Math.max(minimumHeight,curRec.getHeight());
            }
        }
        
        PackerStrategy GCP = new GreedyCornerPack();
        
        RectanglesContainer bestRC = null;
        int bestCost = Integer.MAX_VALUE;
        
        ArrayList<PotHeight> potentials = new ArrayList<>();
        for(int h=minimumHeight;h<=maximumHeight; h++){
            PotHeight p = new PotHeight(h, h-rectanglesArea%h);
            potentials.add(p);
        }
        
        Collections.sort(potentials, new SortPotentials());

        for(PotHeight p : potentials){

            //pruning
            if(p.potCost>bestCost) continue;
            
            if((System.currentTimeMillis() - startTime) > 20000) break; 
            
            ProblemStatement curPS = new ProblemStatement(
                p.potHeight,
                PS.getRotationAllowed(),
                PS.getRectangleAmount(),
                PS.getRectanglesArea(),
                PS.getMaxDimension(),
                PS.getRectangles()

            );

            RectanglesContainer curRC = GCP.pack(curPS);
            
            int curCost = curRC.getCost();
            if(curCost<bestCost){
                bestCost = curCost;
                bestRC = curRC.clone();
                
                if(bestCost==0) break;
            } 
 
        }
       
        return bestRC;
    }
}


 
            

 
            
        