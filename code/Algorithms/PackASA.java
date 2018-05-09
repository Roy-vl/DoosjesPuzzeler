import java.lang.Math;
import java.util.Arrays;

public class PackASA implements PackerStrategy{

double totalArea; //total area of rectangles R
double wMin = 15; //minimal width = maximum of smaller dimensions of all rectangles
double wMax = 1.05*Math.pow(totalArea,0.5); //Maximal width
double[] widths = new double[wMax-wMin];
double[] heights = new double[wMax-wMin];
double[] fr = new double[wMax-wMin];
double[] iter = new double[wMax-wMin];

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
public int RandomLS(){
    int minimumheight; //minimum found height
 return minimumheight;  
}

    @Override
    public RectanglesContainer pack(ProblemStatement PS){
        RectanglesContainer RC = new RectanglesContainer();
        
        
        
        return RC;
    }
    
}
