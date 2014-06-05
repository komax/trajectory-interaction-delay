package frechet;

import frechet.Node.NodeType;

class PathTree {

    protected Node[][] grid;
    private final double[][] gridValues;

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
        boolean hasWestNeighbor = i > 0;
        boolean hasSouthNeighbor = j > 0;
        Node parent = null;
        if (hasWestNeighbor) {
            parent = grid[i - 1][j];
        }
        if (hasWestNeighbor && hasSouthNeighbor) {
            if (parent != null) {
                parent = compareParent(parent, grid[i - 1][j - 1]);
            } else {
                parent = grid[i - 1][j - 1];
            }
        }
        if (hasSouthNeighbor) {
            if (parent != null) {
                parent = compareParent(parent, grid[i][j - 1]);
            } else {
                parent = grid[i][j - 1];
            }
        }
        return parent;
    }

    Node compareParent(Node c, Node cPrime) {
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
    }

}
