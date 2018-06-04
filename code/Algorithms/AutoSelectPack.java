
import java.util.Arrays;



public class AutoSelectPack implements PackerStrategy{
    
    @Override
    public boolean applicable(ProblemStatement PS){
        return true;
    }
 
    @Override
    public RectanglesContainer pack(ProblemStatement PS){
        double relativeSize = (PS.getContainerHeight() / 20);
        int relativeS = (int) relativeSize;
        Rectangle[] rectangles = PS.getRectangles();
        if(PS.getRotationAllowed()){
            for(Rectangle curRec : rectangles){           
                if((curRec.sy > curRec.sx && curRec.sy > relativeS) || curRec.sy > PS.getContainerHeight()){
                    curRec.rotated = true;
                }
            }
        }  

        if(PS.getRectangleAmount()<25){
            System.out.println("Choosed BackTrackCornerPack");
            return new BacktrackCornerPack().pack(PS);
        }     
        
        for(int sorting = 1; sorting <= 3; sorting++){
            switch (sorting){
                case 1:
                    Arrays.sort(rectangles, new SortByArea());
                    break;
                case 2:
                    Arrays.sort(rectangles, new SortByArea());
                    Arrays.sort(rectangles, new SortByDecreasingWidth());
                    break;
                case 3:
                    Arrays.sort(rectangles, new SortByArea());
                    Arrays.sort(rectangles, new SortByDecreasingHeight());
                    break;
            }

                if(PS.getContainerHeight()>0){
                    System.out.println("Choosed GreedyCornerPack");
                    
                    ProblemStatement sortedPS = new ProblemStatement(
                        PS.containerHeight,
                        PS.rotationAllowed,
                        PS.rectangleAmount,
                        PS.rectanglesArea,
                        PS.maxDimension,
                        rectangles
                    )
                    
                    return new GreedyCornerPack().pack(PS);               
                }

                System.out.println("Choosed MultipleGreedyCornerPack");
                return new MultipleGreedyCornerPack().pack(PS);
        
        }
        
        return new RectanglesContainer();
    }
}