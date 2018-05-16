import java.lang.Math;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Random;

public class PackASA implements PackerStrategy{

    double totalArea; //total area of rectangles R
    double wMin = 15; //minimal width = maximum of smaller dimensions of all rectangles
    double wMax = 1.05*Math.pow(totalArea,0.5); //Maximal width
    double[] widths = new double[wMax-wMin];
    double[] heights = new double[wMax-wMin];
    double[] fr = new double[wMax-wMin];
    double[] iter = new double[wMax-wMin];
    boolean first = true;
    ArrayList newR = new ArrayList(); // new sequence of rectangles
    PackerStrategy heuristicPacking = new PackTetris();
    RectanglesContainer container;
    Random random = new Random();

    //Calculate the set of candidate widths W
    public void candidateWidths(){ 
        for (int i = 0; i<=widths.length; i++){
            widths[i] = wMin+i;
        }
    }
    public void makeArrays(ProblemStatement PS){
        for (int i=0;i<=widths.length; i++){
            heights[i] = randomLS(PS,widths[i],1);
            fr[i] = totalArea/(widths[i]*heights[i]);
            iter[i] = 1;
        }
    }

    //Sort by filling ratio fr
    public void sortByFillingRatio(){

    }
    
    //RandomLS procedure
    public int randomLS(ProblemStatement PS, int iter){
        int minimumWidth; // minimum found width
        int currentWidth; // current found width
        Rectangle[] rectangles = PS.getRectangles(); // Array of rectangles
        Rectangle[] newOrder; // new order in rectangles
        Rule[][] sortingRules; // ArrayList of sorting rules on Wk

        if (first) {
            first = false;
            for (Rule[] r: sortingRules) {
                // Sort rectangles
                minimumWidth = getContainerWidth(PS, rectangles);
                r.width = minimumWidth;
            }
            //Sort sortingRules in decreasing order of the width returned by heuristicPacking
        }
        
        
        // Select the ith sorting rule from sortingRules randomly
        for (int i = 0; i < sortingRules.length; i++) {
            Rule[i][2] = (2*i)/(sortingRules.length*(sortingRules.length+1));
        }
        
        int totalSum = 0;
        
        for (Rule[] r: sortingRules) {
            totalSum += r[2];
        }
        
        int index = random.nextInt(totalSum);
        int sum = 0;
        int i = 0;
        while (sum < index) {
            sum += Rule[i++][2];
        }
        
        Rule[] chosen = Rule[Math.max(0, i-1)][];
        
        // Sort rectangles using this rule
        minimumWidth = getContainerWidth(PS, rectangles);
        for (int i = 1; i <= iter; i++) {
            int j = -1;
            int k = -1;
            Rectangle save = new Rectangle();
            
            while (j == k) {
                j = random.nextInt(rectangles.length);
                k = random.nextInt(rectangles.length);
            }
            
            save = rectangles[j];
            rectangles[j] = rectangles[k];
            rectangles[k] = save;
            
            currentWidth = getContainerWidth(PS, rectangles);
            if (currentWidth <= minimumWidth) {
                minimumWidth = currentWidth;
                rectangles = newOrder;
            }
        }
        // Update sortingRules to make sure it is sorted in decreasing order of height
        return minimumWidth;  
    }
    
    
    int r = (new Random()).nextInt(8)+3;
    
    Arrays.sort(rectangles, new SortByDecreasingWidth());
    Collections.sort(//)
    
    /** Solves problem statement with new array and returns width. */
    public int getContainerWidth(ProblemStatement PS, Rectangle[] rectangles) {
        ProblemStatement newPS = new ProblemStatement(PS.getContainerHeight(), PS.getRotationAllowed(), PS.getRectangleAmount(), rectangles);
        container = heuristicPacking.pack(newPS);
        return container.getBoundingWidth();
    }

    @Override
    public RectanglesContainer pack(ProblemStatement PS){
        RectanglesContainer RC = new RectanglesContainer();
        ProblemStatement newPS = new ProblemStatement();
        totalArea = 0;
        
        candidateWidths();
        for (Rectangle r : PS.getRectangles()) {
            totalArea += r.getArea();
        }
        
        for (int i = 0; i < widths.length; i++) {
            heights[i] = randomLS(newPS, 1);
            fr[i] = totalArea/(widths[i]*heights[i]);
            iter[i] = 1;
        }
        
        sortByFillingRatio();
        while (/** time limit is not exceeded */) {
            // select the kth width from W randomly
            
        }
        
        return RC;
    }
    
}
