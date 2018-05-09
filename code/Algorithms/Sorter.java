
public class Sorter{
    
    public void kaas(ProblemStatement PS){
        
        Rectangle[] rectangles = PS.getRectangles();
        
        for(Rectangle curRec : rectangles){
            
            curRec.getAntiSquariness();
            curRec.getArea();
        
        
        
        }     
    }
    
}

//public int getAntiSqauriness(){
//        return (int)(sx>sy ? (float)sx/sy: (float)sy/sx);
//    }