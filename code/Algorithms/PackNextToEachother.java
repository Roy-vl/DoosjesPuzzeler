public class PackNextToEachother implements PackerStrategy{
    
    @Override
    public RectanglesContainer pack(ProblemStatement PS){
        RectanglesContainer RC = new RectanglesContainer();
        
        int x=0;
        for (Rectangle curRec : PS.getRectangles()){
            curRec.px = x;
            curRec.rotated = PS.getRotationAllowed() && ( curRec.sy>PS.getContainerHeight() || (curRec.sx>curRec.sy && curRec.sx<=PS.getContainerHeight()) );       
            x += curRec.getWidth();
            RC.addRectangle(curRec);
        }
        
        return RC;
    }
}

