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
    public QuadTree pack(ProblemStatement PS){
          
        long startTime = System.currentTimeMillis();
  
        int minimumHeight = PS.getMinPosContainerHeight();
        int maximumHeight = PS.getRectanglesArea()/PS.getMinPosContainerWidth();
        int rectanglesArea = PS.getRectanglesArea();

        PackerStrategy GCP = new GreedyCornerPack();
        
        QuadTree bestQT = null;
        int bestCost = Integer.MAX_VALUE;
        
        ArrayList<PotHeight> potentials = new ArrayList<>();
        for(int h=minimumHeight;h<=maximumHeight ; h++){
            PotHeight p = new PotHeight(h, (h-rectanglesArea%h)%h);
            potentials.add(p);
        }
        
        Collections.sort(potentials, new SortPotentials());

        for(PotHeight p : potentials){

            //pruning
            if(p.potCost>=bestCost) continue;
            
            if((System.currentTimeMillis() - startTime) > 20000) break; 
            
            ProblemStatement curPS = new ProblemStatement(
                p.potHeight,
                PS.getRotationAllowed(),
                PS.getRectangleAmount(),
                PS.getRectangles()
            );

            QuadTree curQT = GCP.pack(curPS);
            int curCost = curQT.getCost();
            
            if(curCost<bestCost){
                bestCost = curCost;
                bestQT = curQT.clone();
                if(bestCost==0) break;
            } 
        }
        return bestQT;
    }
}

