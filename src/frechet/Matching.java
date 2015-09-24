package frechet;

import delayspace.DelaySpace;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import utils.DynamicTimeWarpingMatching;
import utils.IntPair;
import utils.Trajectory;

public class Matching implements Serializable {

    public final int[] i;
    public final int[] j;
    private final int length;
    
    public static Matching createFrechetMatching(LCFMTree tree, Trajectory trajectory1, Trajectory trajectory2, boolean isDirectional) {
        return new Matching(tree, trajectory1, trajectory2, isDirectional);
    }
    
    public static Matching createOneToOneMatching(Trajectory trajectory1, Trajectory trajectory2) {
        int length = trajectory1.length();
        int[] i = new int[length];
        int[] j = new int[length];
        for (int k = 0; k < length; k++) {
            i[k] = k;
            j[k] = k;
        }
        return new Matching(i, j, length);
    }
    
    public static Matching createDTWMaching(Trajectory trajectory1, Trajectory trajectory2, DelaySpace delayspace) {
        List<IntPair> matchingIndices = DynamicTimeWarpingMatching.computeDTWMatching(trajectory1, trajectory2, delayspace);
        int lengthMatching = matchingIndices.size();
        int[] i = new int[lengthMatching];
        int[] j = new int[lengthMatching];
        for (int k = 1; k < lengthMatching; k++) {
            IntPair edge = matchingIndices.get(k);
            i[k] = edge.i - 1;
            j[k] = edge.j - 1;
        }
        return new Matching(i, j, lengthMatching);
    }
    
    private Matching(int[] i, int[] j, int length) {
        this.i = i;
        this.j = j;
        this.length = length;
    }
    
    private Matching(LCFMTree tree, Trajectory trajectory1, Trajectory trajectory2, boolean isDirectional) {
        // Compute a path from the end of both trajectories towards the root.
        List<Node> path = new ArrayList<>();
        int numRows = trajectory1.length();
        int numColumns = trajectory2.length();
        if (isDirectional) {
            path.add(new Node(numRows - 1, numColumns - 1, Double.NaN));
        //    System.out.println(path.get(0));
        }
        Node currentNode;
        if (isDirectional) {
            currentNode = tree.getNode(numRows - 2, numColumns - 2);
        } else {
            currentNode = tree.getNode(numRows - 1, numColumns - 1);
        }
        path.add(currentNode);
        //System.out.println(currentNode);
        while (!currentNode.isRoot()) {
            currentNode = currentNode.getParent();
            path.add(currentNode);
          //  System.out.println(currentNode);
        }
        
       // System.out.println(tree.getNode(2,1));
        
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
    
    public int getLength() {
        return length;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Matching(\ni <-> j");
        assert i.length == j.length;
        for (int k = 0; k < length; k++) {
            builder.append('\n');
            builder.append(i[k]);
            builder.append(" <-> ");
            builder.append(j[k]);
        }
        builder.append("\n)");
        return builder.toString();
    }
}
