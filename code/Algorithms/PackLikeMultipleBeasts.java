public class PackLikeMultipleBeasts implements PackerStrategy{
    
    @Override
    public RectanglesContainer pack(ProblemStatement PS){
        
        long startTime = System.currentTimeMillis();
        
        int maximumHeight = 0;
        int minimumHeight = 0;
        
        for(Rectangle curRec : PS.getRectangles()){
            if(PS.getRotationAllowed()){
                maximumHeight += Math.max(curRec.sx,curRec.sy);
                minimumHeight = Math.max(minimumHeight,Math.min(curRec.sx,curRec.sy));
            }else{
                maximumHeight += curRec.getHeight();
                minimumHeight = Math.max(minimumHeight,curRec.getHeight());
            }
        }
        
        PackerStrategy PLAB = new PackLikeABeast();
        
        RectanglesContainer bestRC = null;
        int bestCost = Integer.MAX_VALUE;

        for(int h=minimumHeight;h<=maximumHeight; h++){
            if((System.currentTimeMillis() - startTime) > 290000) break; 
            
            ProblemStatement curPS = new ProblemStatement(
                h,
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
            } 

        }

        return bestRC;
    }
}

