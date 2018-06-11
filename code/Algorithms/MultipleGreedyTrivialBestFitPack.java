import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MultipleGreedyTrivialBestFitPack implements PackerStrategy{
    
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
  
        int minimumHeight = PS.getMinPosContainerHeight();
        int maximumHeight = PS.getRectanglesArea()/PS.getMinPosContainerWidth()*2;
        int rectanglesArea = PS.getRectanglesArea();

        PackerStrategy GTPP = new GreedyTrivialBestFitPack();
        
        RectanglesContainer bestRC = new RectanglesContainer();
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

            RectanglesContainer curRC = GTPP.pack(curPS);
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

