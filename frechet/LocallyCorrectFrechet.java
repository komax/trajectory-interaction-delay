package frechet;
import java.util.ArrayList;
import java.util.Collections;
import frechet.*;

public class LocallyCorrectFrechet {

    public static Matching compute(double[][] grid, int[][] t1, int[][] t2) {
        int n = grid[0].length;
        int m = grid.length;
        PathTree tree = new PathTree(grid, n, m);

        
        for (int i = 1; i < m; i++ ) {
            tree.add(i,0);
        }
        for (int j = 1; j < n; j++) {
            tree.add(0,j);
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < m; j++) {
                tree.add(i,j);
            }
        }


        ArrayList<Node> path = new ArrayList<Node>();
        Node node = tree.grid[n-1][m-1];
        while (node != null) {
            path.add(node);
            node = node.parent;
        }
        Collections.reverse(path);
        return new Matching(path);
    }

}
