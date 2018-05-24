public class MultipleGreedyCornerPack implements PackerStrategy{
    
    @Override
    public RectanglesContainer pack(ProblemStatement PS){
        
        long startTime = System.currentTimeMillis();
        
        int maximumHeight = 0;
        int minimumHeight = 0;
        
        int totalRectangleArea = 0;
        
        for(Rectangle curRec : PS.getRectangles()){
            totalRectangleArea+=curRec.getArea();
            
            if(PS.getRotationAllowed()){
                maximumHeight += Math.min(curRec.sx,curRec.sy);
                minimumHeight = Math.max(minimumHeight,Math.min(curRec.sx,curRec.sy));
            }else{
                maximumHeight += curRec.getHeight();
                minimumHeight = Math.max(minimumHeight,curRec.getHeight());
            }
        }
        
        PackerStrategy GCP = new GreedyCornerPack();
        
        RectanglesContainer bestRC = null;
        int bestCost = Integer.MAX_VALUE;

        for(int h=minimumHeight;h<=maximumHeight; h++){
            //pruning
            if(h-totalRectangleArea%h>bestCost) continue;
            
            if((System.currentTimeMillis() - startTime) > 10000) break; 
            
            ProblemStatement curPS = new ProblemStatement(
                h,
                PS.getRotationAllowed(),
                PS.getRectangleAmount(),
                PS.getRectangles()
            );

            RectanglesContainer curRC = GCP.pack(curPS);

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

