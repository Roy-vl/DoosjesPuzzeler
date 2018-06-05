import java.util.Comparator;

class Point{
    int x, y;
    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    @Override
    public Point clone(){
        Point clone = new Point(x, y);
        return clone;
    }
}

class SortByDistance implements Comparator<Point>
{
    public int compare(Point a, Point b)
    {
        return a.x*a.x+a.y*a.y - (b.x*b.x+b.y*b.y);
    }
}

class SortByLeftness implements Comparator<Point>
{
    public int compare(Point a, Point b)
    {
        return a.x*2000+a.y - (b.x*2000+b.y);
    }
}