/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visualization;

/**
 *
 * @author max
 */
public class EdgeCursor {
    public static final EdgeCursor INVALID_CURSOR = new EdgeCursor(-1, -1, -1);
    
    private final int indexTrajA;
    private final int indexTrajB;
    private final int position;
    
    public EdgeCursor(int indexTrajA, int indexTrajB, int position) {
        this.indexTrajA = indexTrajA;
        this.indexTrajB = indexTrajB;
        this.position = position;
    }
    
    public int getIndexTrajA() {
        return indexTrajA;
    }
    
    public int getIndexTrajB() {
        return indexTrajB;
    }
    
    public int getPosition() {
        return position;
    }
    
    public boolean isTrajAAhead() {
        if (this == INVALID_CURSOR) {
            return false;
        } else {
            return indexTrajA > indexTrajB;
        }
    }
    
    public boolean isTrajBAhead() {
        if (this == INVALID_CURSOR) {
            return false;
        } else {
            return indexTrajB > indexTrajA;
        }
    }
    
    public boolean isDelayLargerOrEqualThan(int threshold) {
        int delay = Math.abs(indexTrajA - indexTrajB);
        return delay >= threshold;
    }
    
    public boolean isValid() {
        return this != INVALID_CURSOR;
    }
    
}
