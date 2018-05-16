
import java.util.ArrayList;

public class Bucketeer {
    public ArrayList<ArrayList<Rectangle>> toBuckets(Rectangle[] rectangles){
        ArrayList<ArrayList<Rectangle>> buckets = new ArrayList<>();
        
        ArrayList<Rectangle> zandkorrels = new ArrayList<>();
        ArrayList<Rectangle> langwerpigejoekels = new ArrayList<>();
        ArrayList<Rectangle> mooiejoekels = new ArrayList<>();
        ArrayList<Rectangle> kleutzak     = new ArrayList<>();
        
        for(Rectangle curRec : rectangles){
            if(curRec.getArea()==1){
                zandkorrels.add(curRec);
            }else if(curRec.getAntiSquariness()>6){
                langwerpigejoekels.add(curRec);
            }else if(curRec.getArea()>100){
                mooiejoekels.add(curRec);
            }else{
                kleutzak.add(curRec);
            }
        }
        
        buckets.add(zandkorrels);
        buckets.add(langwerpigejoekels);
        buckets.add(mooiejoekels);
        buckets.add(kleutzak);
        
        return buckets;
    }
}
