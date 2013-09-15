package frechet;

import frechet.*;
import java.util.ArrayList;

public class Matching {
    public int[] i;
    public int[] j;
    public double[][] t1;
    public double[][] t2;

    public Matching(ArrayList<Node> path, double[][] t1, double[][] t2) {
        i = new int[path.size()];
        j = new int[path.size()];
        this.t1 = t1;
        this.t2 = t2;

        for (int p = 0; p < path.size(); p++) {
            Node node = path.get(p);
///            System.out.printf("(%d, %d)\n",node.i, node.j);
            i[p] = node.i;
            j[p] = node.j;
        } 
    }


    public double[][] getMatchedPoints() {
        double[][] results = new double[i.length][10];
        int idx1;
        int idx2;
        for (int match = 0; match < i.length; match++) {
            results[match] = new double[10];
            idx1 = this.i[match];
            idx2 = this.j[match];
            results[match][0] = t1[idx1][0]; // x1
            results[match][1] = t1[idx1][1]; // y1
            results[match][2] = t1[idx1][2]; // z1
            results[match][3] = t1[idx1][3]; // frame1
            results[match][4] = t1[idx1][4]; // bird_id1
            results[match][5] = t2[idx2][0]; // x2
            results[match][6] = t2[idx2][1]; // y2
            results[match][7] = t2[idx2][2]; // z2
            results[match][8] = t2[idx2][3]; // frame2
            results[match][9] = t2[idx2][4]; // bird_id2
        }

        return results;
    }

///    /*
///     * return a matrix with a row for each match, of the form:
///     * x1 y1 z1 frame_id1 bird_id1 x2 y2 z2 frame_id2 bird_id2
///     *
///     * (i.e. a M by 10 matrix)
///     * */
///    public int[][] toMatrix() {

///        int[][] results = new int[i.length][10];
///        for 
///    }

}

