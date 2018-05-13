import java.lang.Math;
import java.util.Arrays;
import java.util.ArrayList;

public class PackASA implements PackerStrategy{

    double totalArea; //total area of rectangles R
    double wMin = 15; //minimal width = maximum of smaller dimensions of all rectangles
    double wMax = 1.05*Math.pow(totalArea,0.5); //Maximal width
    double[] widths = new double[wMax-wMin];
    double[] heights = new double[wMax-wMin];
    double[] fr = new double[wMax-wMin];
    double[] iter = new double[wMax-wMin];
    boolean first = true;
    ArrayList R = new ArrayList(); // sequence of rectangles
    ArrayList newR = new ArrayList(); // new sequence of rectangles

    //Calculate the set of candidate widths W
    public void candidateWidths(){ 
        for (int i = 0; i<=widths.length; i++){
            widths[i] = wMin+i;
        }
    }
    public void makeArrays(){
        for (int i=0;i<=widths.length; i++){
            heights[i] = randomLS(R,widths[i],1);
            fr[i] = totalArea/(widths[i]*heights[i]);
            iter[i] = 1;
        }
    }

    //Sort by filling ratio fr
    public void sortByFillingRatio(){

    }
    //RandomLS procedure
    public int randomLS(ArrayList R, double Wk, int iter){
        int minimumHeight; //minimum found height
        int currentHeight; //current found height
        ArrayList sr; // ArrayList of sorting rules on Wk

        if (first) {
            first = false;
            for (/** for each sorting rule */) {
                // Sort R
                minimumHeight = heuristicPacking(R, Wk);
                // add sorting rule to sr
            }
            //Sort sr in decreasing order of the heigth returned by heuristicPacking
        }
        // Select the ith sorting rule from sr randomly
        // Sort R using this rule
        minimumHeight = heuristicPacking(R, Wk);
        for (int i = 1; i <= iter; i++) {
            // Generate sequence newR from R by randomly swapping two rectangles
            currentHeight = heuristicPacking(newR, Wk);
            if (currentHeight <= minimumHeight) {
                minimumHeight = currentHeight;
                R = newR;
            }
        }
        // Update sr to make sure it is sorted in decreasing order of height
        return minimumHeight;  
    }

    //HeuristicPacking procedure
    public int heuristicPacking(ArrayList R, double Wc) {
        // initialize skyline
        while (/** R is not empty */) {
            // s = bottom-left segment of skyline
            // Select rectangle curRec from R using sorting rule
            if (/** curRec is not found */) {
                // remove s from the skyline and update the skyline
            } else {
                curRec.px = sx;
                curRec.py = sy;
                R.remove(curRec);
            }
        }
        
        // return the used height of the packing
    }

    @Override
    public RectanglesContainer pack(ProblemStatement PS){
        RectanglesContainer RC = new RectanglesContainer();
        totalArea = 0;
        
        candidateWidths();
        for (Rectangle r : PS.getRectangles()) {
            totalArea += r.getArea();
        }
        
        for (int i = 0; i < widths.length; i++) {
            heights[i] = randomLS(R, widths[i], 1);
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
