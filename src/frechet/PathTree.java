package frechet;

import java.util.Set;
import frechet.Node.NodeType;

class PathTree {

    protected Node[][] grid;
    private double[][] gridValues;

    public PathTree(double[][] distanceTerrain, int numRows, int numColumns) {
        this.grid = new Node[numRows][numColumns];
        this.gridValues = distanceTerrain;
        Node root = new Node(null, 0, 0, distanceTerrain[0][0]);
        root.status = NodeType.GROWTHNODE;
        this.grid[0][0] = root;
    }

    private boolean isEmpty(int i, int j) {
        if (i >= grid.length) {
            return false;
        }
        if (j >= grid[0].length) {
            return false;
        }
        if (grid[i][j] == null) {
            return true;
        }
        return false;
    }

    protected void add(int i, int j) {
        /* add grid[i][j] to the tree */
        // three pairs of candidate parents: N+E, NE+E, N+NE 
        Node parent = selectParent(i, j);

        parent.status = NodeType.LIVINGNODE;

        // is [i-1][j-1] dead?
        if ((i != 0) && (j != 0)) {
            Node n = grid[i - 1][j - 1];
            if (!parent.equals(n) && !parent.parent.equals(n)) {
                n.status = NodeType.DEADNODE;
            }
        }
        Node newNode = new Node(parent, i, j, gridValues[i][j]);

        grid[i][j] = newNode;
        if ((parent.i < i) && (parent.j < j)) {
            parent.northEast = newNode;
        } else if (parent.i == i) {
            parent.north = newNode;
            assert (parent.j + 1) == j;
        } else {
            parent.east = newNode;
            assert parent.j == j && (parent.i + 1) == i;
        }
    }

    Node selectParent(int i, int j) {
        Node[] candidates = new Node[3];
        if (i > 0) {
            candidates[0] = grid[i - 1][j]; // West
        }
        if (i > 0 && j > 0) {
            candidates[1] = grid[i - 1][j - 1]; // South West
        }
        if (j > 0) {
            candidates[2] = grid[i][j - 1]; // South
        }
        for (int q = 0; q < 3; q++) {
            if ((candidates[(q) % 3] == null) && (candidates[(q + 1) % 3] == null)) {
                return candidates[(q + 2) % 3];
            }
        }

        for (int x = 0; x < 3; x++) {
            Node c = candidates[x];
            if (c == null) {
                continue;
            }
            boolean satisfies = true;
            for (int y = x + 1; y < 3; y++) {
                Node cPrime = candidates[y];
                Node result = compareParent(c, cPrime);

                if (result == null) {
					// need to break ties. Use ordering 0 < 1 < 2
                    // x will always be less than y
                    satisfies = false;
                }
                if (!c.equals(result)) {
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
        // lol c always wins
        if (cPrime == null) {
            return c;
        }
        if (c == null) {
            throw new NullPointerException("c should not be null");
        }
        Node nearestCommonAncestor = c.nearestCommonAncestor(cPrime);
        double maxValueOfCToNCA = c.maxValueOnPathTo(nearestCommonAncestor);
        double maxValueOfCPrimeToNCA = cPrime.maxValueOnPathTo(nearestCommonAncestor);
        if (maxValueOfCToNCA < maxValueOfCPrimeToNCA) {
            return c;
        } else {
            return cPrime;
        }
//        Set<Node> pathC = c.pathToRoot();
//        Set<Node> pathCPrime = cPrime.pathToRoot();
//
//        // dominant value on path from c to NCA of c and c'
//        double dominantC = 0;
//
//        pathC.removeAll(cPrime.pathToRoot());
//
//        for (Node n : pathC) {
//            double value = grid[n.i][n.j].value;
//            if (dominantC < value) {
//                dominantC = value;
//            }
//        }
//
//        // dominant value on path from c' to NCA of c and c'
//        double dominantCPrime = 0;
//        pathCPrime.removeAll(c.pathToRoot());
//        for (Node n : pathCPrime) {
//            double value = grid[n.i][n.j].value;
//            if (dominantCPrime < value) {
//                dominantCPrime = value;
//            }
//        }
//        if (dominantCPrime == dominantC) {
//            // Oh no! We need to break ties. Ummm. shit.
//            return null;
//        }
//        if (dominantC < dominantCPrime) {
//            return c;
//        }
//        return cPrime;
    }

}
