/*
A PackerStrategy takes in a ProblemStatement, packs the problem into a RectangleContainer and returns it.
*/

interface PackerStrategy {
    boolean applicable(ProblemStatement PS);
    
    QuadTree pack(ProblemStatement PS);
}
