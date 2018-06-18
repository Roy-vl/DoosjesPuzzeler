class AABB{
    float x1,y1,x2,y2;
    
    public AABB(float _x1, float _y1, float _x2, float _y2){
        x1 = _x1;
        y1 = _y1;
        x2 = _x2;
        y2 = _y2;
    }
    
    @Override
    public AABB clone(){
        return new AABB(x1,y1,x2,y2);
    }
    
    public boolean collides(Rectangle aRec){
        return x1 < aRec.px + aRec.getWidth() &&
               x2 > aRec.px &&
               y1 < aRec.py + aRec.getHeight() &&
               y2 > aRec.py;
    }
    
    public boolean encapsulates(Rectangle aRec){
        return aRec.px >= x1 &&
               aRec.py >= y1 && 
               aRec.px + aRec.getWidth() <= x2 && 
               aRec.py + aRec.getHeight() <= y2;
    }
    
    public void extend(Rectangle aRec){
        x1 = Math.min(x1,aRec.px);
        y1 = Math.min(y1,aRec.py);
        x2 = Math.max(x2,aRec.px+aRec.getWidth());
        y2 = Math.max(y2,aRec.py+aRec.getHeight());
    }
    
    public void extend(AABB aAABB){
        x1 = Math.min(x1,aAABB.x1);
        y1 = Math.min(y1,aAABB.y1);
        x2 = Math.max(x2,aAABB.x2);
        y2 = Math.max(y2,aAABB.y2);
    }
    
    public float getWidth(){
        return x2-x1;
    }
    
    public float getHeight(){
        return y2-y1;
    }
    
    public float getArea(){
        return getWidth()*getHeight();
    }
}