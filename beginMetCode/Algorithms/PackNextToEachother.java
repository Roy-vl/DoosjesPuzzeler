public class PackNextToEachother implements PackerStrategy{
    
    @Override
    public void pack(RectanglesContainer RC){
        int x=0;
        for (Rectangle curRec : RC.rectangles) {
            curRec.px=x;
            curRec.rotated = RC.rotationAllowed && ( curRec.sy>RC.containerHeight || (curRec.sx>curRec.sy && curRec.sx<=RC.containerHeight) );         
            x += curRec.getWidth();
        }
    }
}

