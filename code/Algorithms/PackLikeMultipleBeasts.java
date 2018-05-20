public class PackLikeMultipleBeasts implements PackerStrategy{
    
    @Override
    public QuadTree pack(ProblemStatement PS){
        int maximumHeight = 0;
        int maximalWidth = 0;
        int minimumHeight = 0;
        
        for(Rectangle curRec : PS.getRectangles()){
            maximumHeight += curRec.getHeight();
            if(maximalWidth < curRec.getWidth()){maximalWidth = curRec.getWidth();}
            if(minimumHeight < curRec.getHeight()){minimumHeight = curRec.getHeight();}
        }
        
        PackerStrategy PLAB = new PackLikeABeast();
        
        QuadTree bestQT = null;
        float bestCost = Integer.MAX_VALUE;

        for(int h=minimumHeight;h<=maximumHeight;h++){
            ProblemStatement curPS = new ProblemStatement(
                h,
                PS.getRotationAllowed(),
                PS.getRectangleAmount(),
                PS.getRectangles()
            );

            QuadTree curQT = PLAB.pack(curPS);
   
            float curCost = curQT.getCost();
            if(curCost<bestCost){
                bestCost = curCost;
                bestQT = curQT.clone();
            } 

        }

        return bestQT;
    }
}

