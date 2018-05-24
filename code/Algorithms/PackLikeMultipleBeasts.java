
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PackLikeMultipleBeasts implements PackerStrategy{
    
    public double getPotCost(int height, int area){
        return height - height*(((area*1.0)/height)%1) ;
    }
    
    @Override
    public RectanglesContainer pack(ProblemStatement PS){
        
        long startTime = System.currentTimeMillis();
        
        int maximumHeight = 0;
        int minimumHeight = 0;
        
        for(Rectangle curRec : PS.getRectangles()){
            if(PS.getRotationAllowed()){
                maximumHeight += Math.min(curRec.sx,curRec.sy);
                minimumHeight = Math.max(minimumHeight,Math.min(curRec.sx,curRec.sy));
            }else{
                maximumHeight += curRec.getHeight();
                minimumHeight = Math.max(minimumHeight,curRec.getHeight());
            }
        }
        
        class PotHeight{
            int potHeight;
            double potCost;
            PotHeight(int h, double c){
                potHeight = h;
                potCost = c;
            }
        }
        
        class SortPotentials implements Comparator<PotHeight> {
            public int compare(PotHeight a, PotHeight b) {
                if (a.potCost > b.potCost) {
                    return 1;
                }
                if (a.potCost < b.potCost) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }

        //calculate the optimal area
        int totalArea = 0;
        Rectangle[] rects = PS.getRectangles();
        for (Rectangle currec : rects) {
            totalArea = totalArea + currec.getArea();
        }
        
        Double smartMaxHeight = 1.05*(Math.sqrt(totalArea));
        
        //calculate optimals for each height and sort of cost
        ArrayList<PotHeight> potentials = new ArrayList<>();
        for (int h = minimumHeight; h <= smartMaxHeight; h++) {
            Double potCost = getPotCost(h, totalArea);
            PotHeight newest = new PotHeight(h, potCost);
            potentials.add(newest);
        }
        Collections.sort(potentials, new SortPotentials());
        
        
        
        PackerStrategy PLAB = new PackLikeABeast();
        
        RectanglesContainer bestRC = null;
        int bestCost = Integer.MAX_VALUE;

        for(PotHeight pot : potentials) {
            System.out.println("height: " + pot.potHeight + " potCost: " + pot.potCost);
            
        }
        
        for (PotHeight pot : potentials) {
            if ((System.currentTimeMillis() - startTime) > 10000) {
                break;
            }
            if(pot.potCost>bestCost) break;

            ProblemStatement curPS = new ProblemStatement(
                pot.potHeight,
                PS.getRotationAllowed(),
                PS.getRectangleAmount(),
                PS.getRectangles()
            );

            RectanglesContainer curRC = PLAB.pack(curPS);

            int curCost = curRC.getCost();
            if(curCost<bestCost){
                curRC.visualize(); 
                bestCost = curCost;
                bestRC = curRC.clone();
                
                if(bestCost==0) break;
            }    
        }
        return bestRC;
    }
}

