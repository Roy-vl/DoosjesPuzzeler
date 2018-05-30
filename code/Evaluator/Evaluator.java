public class Evaluator {
    
    
    public Evaluation Evaluate(PackerStrategy strategy, ProblemStatement PS, int tries){
        
        System.out.println("Evaluating " + strategy.getClass().getSimpleName());
        System.out.println("With : "+PS.getRectangleAmount()+" rectangles");
        System.out.println(tries+" times");
        
        float ct = 0;
        float cfr = 0;
        for(int i=0;i<tries;i++){
            long startTime = System.currentTimeMillis();
            RectanglesContainer packedRC = strategy.pack(PS);
            long estimatedTime = System.currentTimeMillis() - startTime;
            ct += estimatedTime;
            cfr += (float)(packedRC.getRectanglesArea())/packedRC.getBoundingArea();
        }
        
        return new Evaluation(ct/tries,cfr/tries);
    }
    
}
