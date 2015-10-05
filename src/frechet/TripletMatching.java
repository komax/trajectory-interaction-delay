/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frechet;

/**
 *
 * @author max
 */
public class TripletMatching {
    public final int[] i;
    public final int[] j;
    public final int[] k;
    
    private final int length;
    
    private TripletMatching(int[] i, int[] j, int[] k, int length) {
        this.i = i;
        this.j = j;
        this.k = k;
        this.length = length;
    }
}
