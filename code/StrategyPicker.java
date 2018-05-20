/*
StrategyPicker is a class that will pick and return an appropriate PackerStrategy for any ProblemStatement
*/

public class StrategyPicker {
    
    public PackerStrategy pick(ProblemStatement PS){
        
        //return new PackGuillotine();
        return new PackCorners();

    } 
}
