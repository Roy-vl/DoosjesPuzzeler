import java.util.Scanner;

/*
ProblemStatement is purely used for parsing the input and storing that data.
NO changing is allowed.
*/

public class ProblemStatement{
    //actual problem statement
    private int         containerHeight;
    private boolean     rotationAllowed;
    private int         rectangleAmount;
    private Rectangle[] rectangles;
    
    //helpers
    private int rectanglesArea;
    private int minPosContainerWidth;
    private int minPosContainerHeight;
    private int maxPosContainerWidth; 
    private int maxPosContainerHeight;
    
    
    public ProblemStatement(){
        containerHeight = 0;
        rotationAllowed = false;
        rectangleAmount = 0;
        rectangles = new Rectangle[rectangleAmount];
        
        rectanglesArea = 0;
        minPosContainerWidth = 0;
        minPosContainerHeight = 0;
        maxPosContainerWidth = 0;
        maxPosContainerHeight = 0;       
    }
    
    public ProblemStatement(int  _containerHeight, boolean _rotationAllowed, int  _rectangleAmount, Rectangle[] _rectangles){
        containerHeight = _containerHeight;
        rotationAllowed = _rotationAllowed;
        rectangleAmount = _rectangleAmount;
        rectangles = _rectangles;
        
        calculateHelpers();
    }
    
    public void calculateHelpers(){
        rectanglesArea = 0;
        minPosContainerWidth = 0;
        minPosContainerHeight = 0;
        maxPosContainerWidth = 0;
        maxPosContainerHeight = 0;
        
        for(Rectangle R : rectangles){
            rectanglesArea += R.getArea();
            if(rotationAllowed){
                minPosContainerWidth = Math.max(minPosContainerWidth, Math.min(R.sx,R.sy));
                minPosContainerHeight = Math.max(minPosContainerHeight, Math.min(R.sx,R.sy));
                maxPosContainerWidth += Math.max(R.sx,R.sy);
                maxPosContainerHeight += Math.max(R.sx,R.sy);;
            }else{
                minPosContainerWidth = Math.max(minPosContainerWidth, R.getWidth());
                minPosContainerHeight = Math.max(minPosContainerHeight, R.getHeight());
                maxPosContainerWidth += R.getWidth();
                maxPosContainerHeight += R.getHeight();
            }
        }
    }
    
    public int getContainerHeight(){
        return containerHeight;
    }
    
    public boolean getRotationAllowed(){
        return rotationAllowed;
    }
    
    public int getRectangleAmount(){
        return rectangleAmount;
    }
    
    public int getRectanglesArea(){
        return rectanglesArea;
    }
   
    public int getMinPosContainerWidth() {
        return minPosContainerWidth;
    }

    public int getMinPosContainerHeight() {
        return minPosContainerHeight;
    }

    public int getMaxPosContainerWidth() {
        return maxPosContainerWidth;
    }

    public int getMaxPosContainerHeight() {
        return maxPosContainerHeight;
    }
    
    //DEEP COPY OF RECTANGLES, DO NOT TOUCH
    public Rectangle[] getRectangles(){
        Rectangle[] clone = new Rectangle[rectangleAmount];
        for(int i=0;i<rectangleAmount;i++) clone[i] = rectangles[i].clone();
        return clone;
    }
    
    public void parseInput(Scanner scanner){
        //refresh
        containerHeight = 0;
        rotationAllowed = false;
        rectangleAmount = 0;
        rectangles = new Rectangle[rectangleAmount];
        
        //parse containerheight
        scanner.next();
        scanner.next();
        if(scanner.next().equals("fixed")){
            containerHeight = scanner.nextInt();
        }else{
            containerHeight = 0;
        }
        
        //parse rotation
        scanner.next();
        scanner.next();
        rotationAllowed = scanner.next().equals("yes");
        
        //parse rectangle amount
        scanner.next();
        scanner.next();
        scanner.next();
        rectangleAmount = scanner.nextInt();

        //parse rectangles
        rectangles = new Rectangle[rectangleAmount];
        for(int i=0;i<rectangleAmount;i++){
            Rectangle newRectangle = new Rectangle();
            newRectangle.sx = scanner.nextInt();
            newRectangle.sy = scanner.nextInt();
            newRectangle.id = i;
            rectangles[i] = newRectangle;
        }
        
        calculateHelpers();
    }
    
    public void generateRandomInput(int rectangleAmount, Boolean rotationAllowed, int containerHeight, int minWidth, int maxWidth, int minHeight, int maxHeight){
        this.rotationAllowed = rotationAllowed;
        this.containerHeight = containerHeight;
        this.rectangleAmount = rectangleAmount;
        rectangles = new Rectangle[rectangleAmount];
        
        for(int i = 0; i < rectangleAmount; i++){
            Rectangle newRectangle = new Rectangle();
            newRectangle.sx = (int)(Math.random()*maxWidth)+minWidth;
            newRectangle.sy = (int)(Math.random()*maxHeight)+minHeight;
            newRectangle.id = i;
            rectangles[i] = newRectangle;
        }
        
        calculateHelpers();
    }
    
    public void print(){
        //print output
        System.out.println("container height: "+(containerHeight==0?"free":("fixed "+containerHeight)));
        System.out.println("rotations allowed: "+(rotationAllowed?"yes":"no"));
        System.out.println("number of rectangles: "+rectangleAmount);
        for (Rectangle curRec : rectangles) {
            System.out.println(curRec.sx+" "+curRec.sy);
        }   
    }
}
