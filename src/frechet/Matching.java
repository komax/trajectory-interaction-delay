package frechet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Matching implements Serializable {

    public final int[] i;
    public final int[] j;
    private final double[][] trajectory1;
    private final double[][] trajectory2;
    private final int length;
    
    public Matching(LCFMTree tree, double[][] trajectory1, double[][] trajectory2) {
        this.trajectory1 = trajectory1;
        this.trajectory2 = trajectory2;
        
        // Compute a path from the end of both trajectories towards the root.
        List<Node> path = new ArrayList<>();
        int numRows = trajectory1.length;
        int numColumns = trajectory2.length;
        Node currentNode = tree.getNode(numRows - 1, numColumns - 1);
        path.add(currentNode);
        while (!currentNode.isRoot()) {
            currentNode = currentNode.getParent();
            path.add(currentNode);
        }
        // Add the root to the matching.
        path.add(currentNode);
        
        // Store the path in the fields i and j.
        this.length = path.size();
        i = new int[length];
        j = new int[length];
        for (int k = 0; k < length; k++) {
            Node matchingNode = path.get(k);
            int reversedIndex = length - 1 - k;
            i[reversedIndex] = matchingNode.getIndexTraject1();
            j[reversedIndex] = matchingNode.getIndexTraject2();
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
