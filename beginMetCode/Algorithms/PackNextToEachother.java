public class PackNextToEachother implements PackerStrategy{
    
    @Override
    public RectanglesContainer pack(ProblemStatement PS){
        RectanglesContainer RC = new RectanglesContainer();
        int x=0;
        for (Rectangle curRec : PS.getRectangles()){
            Rectangle placRec = curRec.clone();
            placRec.px = x;
            placRec.rotated = PS.getRotationAllowed() && ( curRec.sy>PS.getContainerHeight() || (curRec.sx>curRec.sy && curRec.sx<=PS.getContainerHeight()) );       
            x += placRec.getWidth();
            RC.addRectangle(placRec);
        }
        return RC;
    }
}

