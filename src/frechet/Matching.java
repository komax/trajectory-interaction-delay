package frechet;

import java.io.Serializable;
import java.util.List;

public class Matching implements Serializable {

    public int[] i;
    public int[] j;
    private final double[][] trajectory1;
    private final double[][] trajectory2;

    public Matching(List<Node> path, double[][] trajectory1, double[][] trajectory2) {
        this.i = new int[path.size()];
        this.j = new int[path.size()];
        this.trajectory1 = trajectory1;
        this.trajectory2 = trajectory2;

        for (int p = 0; p < path.size(); p++) {
            Node node = path.get(p);
            i[p] = node.i;
            j[p] = node.j;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Matching(\ni <-> j");
        assert i.length == j.length;
        int length = i.length;
        for (int k = 0; k < length; k++) {
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

}
