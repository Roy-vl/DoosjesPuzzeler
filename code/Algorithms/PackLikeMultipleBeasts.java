public class PackLikeMultipleBeasts implements PackerStrategy{
    
    @Override
    public RectanglesContainer pack(ProblemStatement PS){
        int maximumHeight = 0;
        int maximalWidth = 0;
        int minimumHeight = 0;
        
        for(Rectangle curRec : PS.getRectangles()){
            maximumHeight += curRec.getHeight();
            if(maximalWidth < curRec.getWidth()){maximalWidth = curRec.getWidth();}
            if(minimumHeight < curRec.getHeight()){minimumHeight = curRec.getHeight();}
        }
        
        PackerStrategy PLAB = new PackLikeABeast();
        
        RectanglesContainer bestRC = null;
        int bestCost = 10000000;

        for(int h=minimumHeight;h<=maximumHeight;h++){
            ProblemStatement curPS = new ProblemStatement(
                h,
                PS.getRotationAllowed(),
                PS.getRectangleAmount(),
                PS.getRectangles()
            );

            RectanglesContainer curRC = PLAB.pack(curPS);
   
            int curCost = curRC.getCost();
            if(curCost<bestCost){
                bestCost = curCost;
                bestRC = curRC.clone();
            } 

        }

        return bestRC;
    }
}
