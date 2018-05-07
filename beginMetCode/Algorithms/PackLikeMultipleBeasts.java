public class PackLikeMultipleBeasts implements PackerStrategy{
    
    @Override
    public void pack(RectanglesContainer RC){
        int maximumHeight = 0;
        int maximalWidth = 0;
        int minimumHeight = 0;
        
        for(Rectangle curRec : RC.rectangles){
            maximumHeight += curRec.getHeight();
            if(maximalWidth < curRec.getWidth()){maximalWidth = curRec.getWidth();}
            if(minimumHeight < curRec.getHeight()){minimumHeight = curRec.getHeight();}
        }
        
        PackerStrategy PLAB = new PackLikeABeast();
        
        int bestCost = 10000000;
        int bestHeight  = 0;
        
        for(int h=minimumHeight;h<=maximumHeight;h++){
            RC.containerHeight = h;
            
            RC.resetRectangles();
            PLAB.pack(RC);
            
            int curCost = RC.getCost();
            if(curCost<bestCost){
                bestCost = curCost;
                bestHeight = RC.containerHeight;
            } 

            //RC.visualize();
        }

        RC.containerHeight = bestHeight;
        RC.resetRectangles();
        PLAB.pack(RC);
        RC.containerHeight = 0;
    }
}

