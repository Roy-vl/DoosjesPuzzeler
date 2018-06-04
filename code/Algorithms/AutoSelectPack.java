
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
        if(PS.getRotationAllowed()){
            for(Rectangle curRec : PS.getRectangles()){           
                if((curRec.sy > curRec.sx && curRec.sy > relativeS) || curRec.sy > PS.getContainerHeight()){
                    curRec.rotated = true;
                }
            }
        }  

        if(PS.getRectangleAmount()<25){
            System.out.println("Choosed BackTrackCornerPack");
            return new BacktrackCornerPack().pack(PS);
        }     
        
        int sorting = 1;
        switch (sorting){
            case 1:
                Arrays.sort(PS.getRectangles(), new SortByArea());
                break;
            case 2:
                Arrays.sort(PS.getRectangles(), new SortByArea());
                Arrays.sort(PS.getRectangles(), new SortByDecreasingWidth());
                break;
            case 3:
                Arrays.sort(PS.getRectangles(), new SortByArea());
                Arrays.sort(PS.getRectangles(), new SortByDecreasingHeight());
                break;
        }
        sorting++;
                
            if(PS.getContainerHeight()>0){
                System.out.println("Choosed GreedyCornerPack");
                return new GreedyCornerPack().pack(PS);               
            }

            System.out.println("Choosed MultipleGreedyCornerPack");
            return new MultipleGreedyCornerPack().pack(PS);
        
    }
}