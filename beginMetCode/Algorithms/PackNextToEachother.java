public class PackNextToEachother implements PackerStrategy{

    @Override
    public void pack(Rectangle[] rectangles, boolean rotationAllowed, int containerHeight) {
        int x=0;
        for (Rectangle curRec : rectangles) {
            curRec.px=x;
            curRec.rotated = rotationAllowed && ( curRec.sy>containerHeight || (curRec.sx>curRec.sy && curRec.sx<=containerHeight) );         
            x += curRec.getWidth();
        }
    }
}

