/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Roy
 */
public class PackerHelper {
    private PackerStrategy strategy;
    
    public PackerHelper(PackerStrategy strategy) {
        this.strategy = strategy;
    }
    
    public Rectangle[] pack(Boolean rotatable, Rectangle[] rectangles, int containerHeight) {
        return strategy.pack(rotatable, rectangles, containerHeight);
    }
}

