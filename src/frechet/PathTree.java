package frechet;


class PathTree {

    protected Node[][] grid;
    private final double[][] gridValues;

    public PathTree(double[][] distanceTerrain, int numRows, int numColumns) {
        this.grid = new Node[numRows][numColumns];
        this.gridValues = distanceTerrain;
        Node root = new Node(null, 0, 0, distanceTerrain[0][0]);
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

    // TODO Incorporate shortcuts into the adding procedure.
    protected void add(int i, int j) {
        /* add grid[i][j] to the tree */
        // three pairs of candidate parents: N+E, NE+E, N+NE 
        Node candidateParent = selectParent(i, j);

        // is [i-1][j-1] dead?
        if ((i != 0) && (j != 0)) {
            Node node = grid[i - 1][j - 1];
            if (!candidateParent.equals(node) && !candidateParent.isParentOf(node)) {
                //n.status = NodeType.DEADNODE;
            }
        }
        Node newNode = new Node(candidateParent, i, j, gridValues[i][j]);

        grid[i][j] = newNode;
        if ((candidateParent.getIndexTraject1() < i) && (candidateParent.getIndexTraject2() < j)) {
            candidateParent.setDiagonalNode(newNode);
        } else if (candidateParent.getIndexTraject1() == i) {
            candidateParent.setUpNode(newNode);
            assert (candidateParent.getIndexTraject2() + 1) == j;
        } else {
            candidateParent.setRightNode(newNode);
            assert candidateParent.getIndexTraject2() == j && (candidateParent.getIndexTraject1() + 1) == i;
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
