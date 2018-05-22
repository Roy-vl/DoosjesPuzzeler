import java.lang.Math;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Random;
import java.util.Comparator;

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
    
    class SortingRule {
        Comparator<Rectangle> rule;
        int corWidth;
        double probability;
        
        SortingRule(Comparator<Rectangle> rule, int corWidth, double probability) {
            this.rule = rule;
            this.corWidth = corWidth;
            this.probability = probability;
        }
        
        void updateWidth(int corWidth) {
            this.corWidth = corWidth;
        }
        
        void updateProbability(double probability) {
            this.probability = probability;
        }
    }

    //Calculate the set of candidate widths W
    public void candidateWidths(){ 
        for (int i = 0; i<=widths.length; i++){
            widths[i] = wMin+i;
        }
    }
    public void makeArrays(ProblemStatement PS){
        for (int i=0;i<=widths.length; i++){
            heights[i] = randomLS(PS,1);
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
        
        ArrayList<SortingRule> sortingRules = new ArrayList(); // ArrayList of sorting rules on Wk
        
        SortingRule area = new SortingRule(new SortByArea(), 0, 0);
        SortingRule height = new SortingRule(new SortByDecreasingHeight(), 0, 0);
        SortingRule width = new SortingRule(new SortByDecreasingWidth(), 0, 0);
        SortingRule shuffle = new SortingRule(new Shuffle(), 0, 0); // shuffle not implemented yet
        
        sortingRules.set(0, area);
        sortingRules.set(1, height);
        sortingRules.set(2, width);
        sortingRules.set(3, shuffle);

        if (first) { // if first call on current set height
            first = false;
            
            // find corresponding widths to sorting rules
            for (SortingRule sr : sortingRules) {
                Arrays.sort(rectangles, sr.rule);
                sr.updateWidth(getContainerWidth(PS, rectangles));
            }
            //Sort sortingRules in decreasing order of the width returned by heuristicPacking
            
        }
        
        
        /** Select the ith SortingRule from sortingRules "randomly". */
        // Calculate probabilities
        for (int i = 0; i < 4; i++) {
            sortingRules.get(i).updateProbability((2*i)/(4*(4+1)));
        }
        
        double totalSum = 0;
        // Calculate totalSum
        for (SortingRule sr: sortingRules) {
            totalSum += sr.probability;
        }
        
        // Select SortingRule
        double index = totalSum * random.nextDouble();
        double sum = 0;
        int i = 0;
        while (sum < index) {
            sum += sortingRules.get(i++).probability;
        }
        
        SortingRule chosen = sortingRules.get(i-1);
        
        // Sort rectangles using this rule
        Arrays.sort(rectangles, chosen.rule);
        newOrder = rectangles;
        minimumWidth = getContainerWidth(PS, rectangles);
        for (int l = 1; l <= iter; l++) {
            int j = -1;
            int k = -1;
            
            // Pick two random index values that are different form each other.
            while (j == k) {
                j = random.nextInt(rectangles.length);
                k = random.nextInt(rectangles.length);
            }
            
            // Swap the rectangles at the two picked indexes.
            newOrder[j] = rectangles[k];
            newOrder[k] = rectangles[j];
            
            currentWidth = getContainerWidth(PS, newOrder);
            
            /** If the currentWidth is less or equal to minimumWidth,
            update currentWidth and rectangles. */
            if (currentWidth <= minimumWidth) {
                minimumWidth = currentWidth;
                rectangles = newOrder;
            }
        }
        // Update sortingRules to make sure it is sorted in decreasing order of height
        return minimumWidth;  
    }
    
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
