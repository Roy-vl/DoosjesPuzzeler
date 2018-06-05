import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class MultipleGreedyTrivialPack implements PackerStrategy{
    
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
        int minimumWidth = 0;
        
        int rectanglesArea = PS.getRectanglesArea();
        
        for(Rectangle curRec : PS.getRectangles()){
            if(PS.getRotationAllowed()){
                maximumHeight += Math.max(curRec.sx,curRec.sy);
                minimumHeight = Math.max(minimumHeight,Math.min(curRec.sx,curRec.sy));
                minimumWidth = Math.max(curRec.sx, minimumWidth);
            }else{
                maximumHeight += curRec.getHeight();
                minimumHeight = Math.max(minimumHeight,curRec.getHeight());
                minimumWidth = Math.max(curRec.sx, minimumWidth);
            }
        }
        
        PackerStrategy GCP = new GreedyTrivialPack();
        
        RectanglesContainer bestRC = null;
        int bestCost = Integer.MAX_VALUE;
        
        ArrayList<PotHeight> potentials = new ArrayList<>();
        for(int h=minimumHeight;h<=(rectanglesArea/minimumWidth); h++){
            PotHeight p = new PotHeight(h, h-rectanglesArea%h);
            potentials.add(p);
        }
        
        Collections.sort(potentials, new SortPotentials());

        for(PotHeight p : potentials){
            System.out.println("testing heigth: "+p.potHeight+" for cost: "+p.potCost);
            //pruning
            if(p.potCost>=bestCost) {
                break;
            }
            
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
            System.out.println("resulting cost: "+curRC.getCost()+" best cost: "+bestCost);
            
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


 
            

 
            
        