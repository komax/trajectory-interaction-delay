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
    
    public boolean isEqual(int i, int j, int k) {
        return this.i == i && this.j == i && this.k == k;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + this.i;
        hash = 29 * hash + this.j;
        hash = 29 * hash + this.k;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final IntTriple other = (IntTriple) obj;
        if (this.i != other.i) {
            return false;
        }
        if (this.j != other.j) {
            return false;
        }
        if (this.k != other.k) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("(");
        builder.append(this.i);
        builder.append(", ");
        builder.append(this.j);
        builder.append(", ");
        builder.append(this.k);
        builder.append(")");
        return builder.toString();
    }
    

    public static IntTriple createIntTriple(int i, int j, int k) {
        return new IntTriple(i, j, k);
    }
}
