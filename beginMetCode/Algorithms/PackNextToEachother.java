/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Roy
 */
public class PackNextToEachother implements PackerStrategy{
    boolean     rotationAllowed;
    Rectangle[] rectangles;
    
    public PackNextToEachother(Boolean rotationAllowed, 
            Rectangle[] rectangles){
        this.rotationAllowed = rotationAllowed;
        this.rectangles = rectangles;
    }

    @Override
    public Rectangle[] pack(Boolean rotatable, Rectangle[] rectangles) {
        int x=0;
        for (Rectangle curRec : rectangles) {
            curRec.px=x;
            curRec.rotated = rotationAllowed?(curRec.sx>curRec.sy):false;
            x += curRec.getWidth();
        }
        return rectangles;
    }
}
