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
public void CandidateWidths(){ 
    for (int i = 0; i<=widths.length; i++){
        widths[i] = wMin+i;
    }
}
public void MakeArrays(){
    for (int i=0;i<=widths.length; i++){
        heights[i] = RandomLS(R,widths[i],1);
        fr[i] = totalArea/(widths[i]*heights[i]);
        iter[i] = 1;
    }
}

//Sort by filling ratio fr
public void SortByFillingRatio(){

}
//RandomLS procedure
public int RandomLS(ArrayList R, double Wk, int iter){
    int minimumHeight; //minimum found height
    int currentHeight; //current found height
    ArrayList sr; // ArrayList of sorting rules on Wk
    
    if (first) {
        first = false;
        for(/** for each sorting rule */) {
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

    @Override
    public RectanglesContainer pack(ProblemStatement PS){
        RectanglesContainer RC = new RectanglesContainer();
        
        
        
        return RC;
    }
    
}
