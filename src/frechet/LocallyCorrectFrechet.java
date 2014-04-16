package frechet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocallyCorrectFrechet {

    public static Matching compute(double[][] grid, double[][] t1, double[][] t2, int numRows, int numColumns) {
        if ((grid[0].length != numColumns) || (grid.length != numRows)) {
            throw new RuntimeException("Size of grid and size of trajectories disagree\n");
        }
        PathTree tree = new PathTree(grid, numRows, numColumns);

        
        for (int i = 1; i < numRows ; i++ ) {
            tree.add(i,0);
        }
        for (int j = 1; j < numColumns ; j++) {
            tree.add(0,j);
        }
        for (int i = 1; i < numRows ; i++) {
            for (int j = 1; j < numColumns ; j++) {
                tree.add(i,j);
            }
        }


        List<Node> path = new ArrayList<Node>();
        Node node = tree.grid[numRows-1][numColumns-1];
        while (node != null) {
            path.add(node);
            node = node.parent;
        }
        Collections.reverse(path);
        return new Matching(path, t1, t2);
    }

}
