/*
A PackerStrategy takes in a ProblemStatement, packs the problem into a RectangleContainer and returns it.
*/

interface PackerStrategy {
    RectanglesContainer pack(ProblemStatement PS);
}
