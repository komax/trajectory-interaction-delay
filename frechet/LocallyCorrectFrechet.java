package frechet;
import java.util.ArrayList;
import java.util.Collections;
import frechet.*;

public class LocallyCorrectFrechet {

    public static Matching compute(double[][] grid, int[][] t1, int[][] t2, int numRows, int numColumns) {
        if ((grid[0].length != numColumns) || (grid.length != numRows)) {
            System.err.printf("Size of grid and size of trajectories disagree\n");
            return null;
        }
        if ((t1.length != numColumns) || (t2.length != numRows)) {
            System.err.printf("Size of trajectories something blah huh?\n");
        }
///        System.out.printf("Rows: %d columns: %d\n",numRows,numColumns);
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


        ArrayList<Node> path = new ArrayList<Node>();
        Node node = tree.grid[numRows-1][numColumns-1];
        while (node != null) {
            path.add(node);
            node = node.parent;
        }
        Collections.reverse(path);
        return new Matching(path);
    }

}
