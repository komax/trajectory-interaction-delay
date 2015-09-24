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
public class IntPair {
        public final int i;
        public final int j;
        
        public IntPair(int i, int j) {
            this.i = i;
            this.j = j;
        }
        
        public static IntPair createIntTuple(int i, int j) {
            return new  IntPair(i, j);
        }
    }