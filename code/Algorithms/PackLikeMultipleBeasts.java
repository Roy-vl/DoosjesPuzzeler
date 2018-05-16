public class PackLikeMultipleBeasts implements PackerStrategy{
    
    @Override
    public RectanglesContainer pack(ProblemStatement PS){
        int maximumHeight;
        int maximalWidth = 0;
        int minimumHeight = 0;
        
        for(Rectangle curRec : PS.getRectangles()){
            
            if(maximalWidth < curRec.getWidth()){maximalWidth = curRec.getWidth();}
            if(minimumHeight < curRec.getHeight()){minimumHeight = curRec.getHeight();}
        }
        
        PackerStrategy PLAB = new PackLikeABeast();
        
        RectanglesContainer bestRC = null;
        maximumHeight = minimumHeight * 2;
        int bestCost = Integer.MAX_VALUE;
        int stepSize = maximumHeight - minimumHeight;
        int stepAmount = 10;

        for(int h=minimumHeight;h<=maximumHeight; h=h+30){
            System.out.println("New test: "+h);
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

