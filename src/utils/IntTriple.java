/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author max
 */
public class IntTriple {
    public final int i;
    public final int j;
    public final int k;
        
    public IntTriple(int i, int j, int k) {
        this.i = i;
        this.j = j;
        this.k = k;
    }

    public static IntTriple createIntTriple(int i, int j, int k) {
        return new IntTriple(i, j, k);
    }
}
