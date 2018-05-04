public class PackingSolver{
    
    public static void main(String[] args) {
        RectanglesContainer RC = new RectanglesContainer();
        RC.parseInput();
        
        RC.sortRectangles(new SortByPackingScore());
     
    
        long startTime = System.currentTimeMillis();
        (new StrategyPicker()).pick(RC).pack(RC);
        long estimatedTime = System.currentTimeMillis() - startTime;
        
        RC.printOutput();
        
        System.out.println();
        System.out.println("time passed = "+estimatedTime+"ms");
        RC.visualize();
    }
    
}
