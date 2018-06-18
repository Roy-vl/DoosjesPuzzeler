class DoublyLinkedNode{
    boolean filled;
    int x;
    int y;
    DoublyLinkedNode previous;
    DoublyLinkedNode next;
    
    public DoublyLinkedNode(int _x, int _y){
        filled = false;
        x = _x;
        y = _y;
        previous = null;
        next = null; 
    }
}