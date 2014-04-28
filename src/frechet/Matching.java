package frechet;

import java.io.Serializable;
import java.util.List;

public class Matching implements Serializable {
    public int[] i;
    public int[] j;
    private double[][] trajectory1;
    private double[][] trajectory2;

    public Matching(List<Node> path, double[][] trajectory1, double[][] trajectory2) {
        this.i = new int[path.size()];
        this.j = new int[path.size()];
        this.trajectory1 = trajectory1;
        this.trajectory2 = trajectory2;

        for (int p = 0; p < path.size(); p++) {
            Node node = path.get(p);
//            double x1 = this.trajectory1[node.i][0];
//            double y1 = this.trajectory1[node.i][1];
//            double z1 = this.trajectory1[node.i][2];
//            double x2 = this.trajectory2[node.j][0];
//            double y2 = this.trajectory2[node.j][1];
//            double z2 = this.trajectory2[node.j][2];
//            double xDelta = x1 - x2;
//            double yDelta = y1 - y2;
//            double zDelta = z1 - z2;
//            // FIXME Why is distance not stored?
//            double distance = Math.sqrt(xDelta * xDelta + yDelta * yDelta + zDelta * zDelta);
            i[p] = node.i;
            j[p] = node.j;
        } 
    }


    // FIXME method is erroneous
    public double[][] getMatchedPoints() {
        double[][] results = new double[i.length][10];
        int idx1;
        int idx2;
        for (int match = 0; match < i.length; match++) {
            results[match] = new double[10];
            idx1 = this.i[match];
            idx2 = this.j[match];
            results[match][0] = trajectory1[idx1][0]; // x1
            results[match][1] = trajectory1[idx1][1]; // y1
            results[match][2] = trajectory1[idx1][2]; // z1
            results[match][3] = trajectory1[idx1][3]; // frame1
            results[match][4] = trajectory1[idx1][4]; // bird_id1
            results[match][5] = trajectory2[idx2][0]; // x2
            results[match][6] = trajectory2[idx2][1]; // y2
            results[match][7] = trajectory2[idx2][2]; // z2
            results[match][8] = trajectory2[idx2][3]; // frame2
            results[match][9] = trajectory2[idx2][4]; // bird_id2
        }

        return results;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Matching(\ni <-> j");
        assert i.length == j.length;
        int length = i.length;
        for (int k=0; k<length; k++) {
            builder.append('\n');
            builder.append(i[k]);
            builder.append(" <-> ");
            builder.append(j[k]);
        }
        builder.append("\n)");
        return builder.toString();
    }

    public double[][] getTrajectory1() {
        return trajectory1;
    }

    public double[][] getTrajectory2() {
        return trajectory2;
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

