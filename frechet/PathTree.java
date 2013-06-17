package frechet;
import java.util.TreeSet;
import java.lang.Math;
import frechet.Node;

class PathTree {
    private Node[][] grid;

    


    public PathTree(double[][] grid) {
        int n = grid[0].length;
        int m = grid.length;
        this.grid = new Node[n][m];
        Node root = new Node(null, 0, 0, grid[0][0]);

    }


    protected void add(int i, int j) {
        /* add grid[i][j] to the tree */
        // three pairs of candidate parents: N+E, NE+E, N+NE 
        Node parent = selectParent(i, j);
    }



    Node selectParent(int i,int j) {
        Node[] candidates = new Node[3];
        candidates[0] = grid[i-1][j]; // West
        candidates[1] = grid[i-1][j-1]; // South West
        candidates[2] = grid[i][j-1]; // South



        for (int x = 0; x < 3; x++) {
            Node c = candidates[x];
            boolean satisfies = true;
            for (int y = x+1; y < 3; y++) {
                Node cPrime = candidates[y];
                Node result = compareParent(c, cPrime);
                if (result != null) {
                    // need to break ties. Use ordering 0 < 1 < 2
                    // x will always be less than y
                    result = candidates[x];
                }
                if (!result.equals(c)) {
                    satisfies = false;
                }
            }
            if (satisfies) {
                return c;
            }
        }
        return null;
    }

    Node compareParent(Node c, Node cPrime) {
        Node nearestCommonAncestor = c.nearestCommonAncestor(cPrime);
        TreeSet<Node> pathC = c.pathToRoot();
        TreeSet<Node> pathCPrime = cPrime.pathToRoot();


        // dominant value on path from c to NCA of c and c'
        double dominantC = 0;

        pathC.removeAll(pathCPrime);
        for (Node n: pathC) {
            double value = grid[n.i][n.j].value;
                if (dominantC < value) {
                    dominantC = value;
                }
        }

        // dominant value on path from c' to NCA of c and c'
        double dominantCPrime = 0;

        pathCPrime.removeAll(c.pathToRoot());
        for (Node n: pathCPrime) {
            double value = grid[n.i][n.j].value;
                if (dominantCPrime < value) {
                    dominantCPrime = value;
                }
        }
        if (dominantCPrime == dominantC) {
            // Oh no! We need to break ties. Ummm. shit.
            return null;
        }
        if (dominantC < dominantCPrime) {
            return c;
        }
        return cPrime;

    }


    

}
