package frechet;


class LCFMTree {
    private final Node[][] grid;
    private final double[][] gridValues;
    private final int columns;
    private final int rows;

    public LCFMTree(double[][] distanceTerrain, int numRows, int numColumns) {
        this.rows = numRows;
        this.columns = numColumns;
        this.grid = new Node[numRows][numColumns];
        this.gridValues = distanceTerrain;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                grid[i][j] = new Node(i, j, gridValues[i][j]);
            }
        }
        
        for (int i = 1; i < numRows; i++) {
            grid[i][0].setParent(grid[i-1][0]);
            grid[i-1][0].setRightNode(grid[i][0]);
        }
        
        for (int j = 1; j < numColumns; j++) {
            grid[0][j].setParent(grid[0][j-1]);
            grid[0][j-1].setUpNode(grid[0][j]);
        }
    }
    
    public Node getNode(int i, int j) {
        assert i >= 0 && i <= rows;
        assert j >= 0 && j <= columns;
        return grid[i][j];
    }
    
    public void buildTree() {
        int numRows = grid.length;
        int numColumns = grid[0].length;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                expandTree(i, j);
            }
        }
    }
    
    private Node bestCandidateParent(int i, int j) {
        Node down = grid[i][j-1];
        Node diagonal = grid[i-1][j-1];
        Node left = grid[i-1][j];
        
        Node bestCandidate = down;
        if (diagonal.isBetterThan(bestCandidate)) {
            bestCandidate = diagonal;
        }
        
        if (left.isBetterThan(bestCandidate)) {
            bestCandidate = left;
        }
        return bestCandidate;
    }
    
    private void expandTree(int i, int j) {
        Node node = grid[i][j];
        Node down = grid[i][j-1];
        Node diagonal = grid[i-1][j-1];
        Node left = grid[i-1][j];
        
        // Find the best parent and update the references.
        Node bestParent = bestCandidateParent(i, j);
        node.setParent(bestParent);
        if (down.equals(bestParent)) {
            bestParent.setUpNode(node);
        } else if (diagonal.equals(bestParent)) {
            bestParent.setDiagonalNode(node);
        } else {
            bestParent.setRightNode(node);
        }
        
        // if the parent beneath does not have a shortcut, create one, or use it.
        if (!down.hasShortcutUp()) {
            Node shortcutFrom = down;
            Node shortcutTo;
            Direction incoming;
            double maxValue;
            
            if (down.getParent().isRoot()) {
                // If down is the root, create a shortcut to the root.
                shortcutTo = down.getParent();
                incoming = Direction.RIGHT;
                maxValue = down.getValue();
            } else if (down.getParent().outdegree() == 1) {
                // if down has only one shortcut, use this shortcut information.
                Shortcut parentsUpShortcut = down.getParent().getShortcutUp();
                shortcutTo = parentsUpShortcut.getTo();
                incoming = parentsUpShortcut.getIncomingDirection();
                maxValue = Math.max(down.getValue(), parentsUpShortcut.getMaxValue());
            } else {
                // There is no shortcut from the parent, create a new one.
                shortcutTo = down.getParent();
                incoming = Direction.RIGHT;
                maxValue = down.getValue();
            }
            // Create the shortcut using the information from above.
            Shortcut shortcutDown = new Shortcut(shortcutFrom, shortcutTo, maxValue, incoming);
            // Add the new shortcut as incoming shortcut to the to target.
            shortcutTo.getIncomingShortcuts(incoming).add(shortcutDown);
        }

//
//        // make right shortcut for nodes[i - 1][j] if necessary
//        if (left.sc_right == null) {
//            Shortcut sc = new Shortcut();
//            left.sc_right = sc;
//
//            sc.from = left;
//            if (left.pred.isRoot()) {
//                sc.to = left.pred;
//                sc.inc = Incoming.UP;
//                sc.to.getIncs(sc.inc).add(sc);
//                sc.max = left.node.value;
//            } else if (left.pred.outdegree() == 1) {
//                sc.to = left.pred.sc_right.to;
//                sc.inc = left.pred.sc_right.inc;
//                sc.to.getIncs(sc.inc).add(sc);
//                sc.max = Math.max(left.node.value, left.pred.sc_right.max);
//            } else {
//                sc.to = left.pred;
//                sc.inc = Incoming.UP;
//                sc.to.getIncs(sc.inc).add(sc);
//                sc.max = left.node.value;
//            }
//        }
//
//        // make shortcuts for nodes[i][j] where necessary
//        if (best == down) {
//            // went up, make up shortcut
//            Shortcut sc = new Shortcut();
//            node.sc_up = sc;
//
//            sc.from = node;
//            sc.to = down.sc_up.to;
//            sc.inc = down.sc_up.inc;
//            sc.to.getIncs(sc.inc).add(sc);
//            sc.max = Math.max(node.node.value, down.sc_up.max);
//        } else if (best == diagonal) {
//            // diagonal, make both shortcuts
//            Shortcut sc_right = new Shortcut();
//            node.sc_right = sc_right;
//
//            sc_right.from = node;
//            if (diagonal.right == null) {
//                sc_right.to = diagonal.sc_right.to;
//                sc_right.inc = diagonal.sc_right.inc;
//                sc_right.to.getIncs(sc_right.inc).add(sc_right);
//                sc_right.max = Math.max(node.node.value, diagonal.sc_right.max);
//            } else {
//                sc_right.to = diagonal;
//                sc_right.inc = Incoming.DIAGRIGHT;
//                sc_right.to.getIncs(sc_right.inc).add(sc_right);
//                sc_right.max = node.node.value;
//            }
//
//            Shortcut sc_up = new Shortcut();
//            node.sc_up = sc_up;
//
//            sc_up.from = node;
//            if (diagonal.up == null) {
//                sc_up.to = diagonal.sc_up.to;
//                sc_up.inc = diagonal.sc_up.inc;
//                sc_up.to.getIncs(sc_up.inc).add(sc_up);
//                sc_up.max = Math.max(node.node.value, diagonal.sc_up.max);
//            } else {
//                sc_up.to = diagonal;
//                sc_up.inc = Incoming.DIAGUP;
//                sc_up.to.getIncs(sc_up.inc).add(sc_up);
//                sc_up.max = node.node.value;
//            }
//        } else {
//            // went right, make right shortcut
//            Shortcut sc = new Shortcut();
//            node.sc_right = sc;
//
//            sc.from = node;
//            sc.to = left.sc_right.to;
//            sc.inc = left.sc_right.inc;
//            sc.to.getIncs(sc.inc).add(sc);
//            sc.max = Math.max(node.node.value, left.sc_right.max);
//        }
//
//        if (diagonal.outdegree() == 0) {
//            // kill diagonal branch
//            SolutionNode dead = diagonal;
//            SolutionNode alive = diagonal.pred;
//            while (alive.outdegree() == 1) {
//                // kill alive
//                alive.up = alive.diagonal = alive.right = null;
//                dead = alive;
//                alive = alive.pred;
//            }
//
//            // alive is the branching node, dead is its now dead child
//            if (alive.up == dead) {
//                alive.up = null;
//                List<Shortcut> extend;
//                Shortcut with = alive.sc_up;
//                if (alive.diagonal != null) {
//                    extend = alive.getIncs(Incoming.DIAGUP);
//                } else { // alive.right != null
//                    extend = alive.getIncs(Incoming.RIGHT);
//                }
//                Iterator<Shortcut> it = extend.iterator();
//                while (it.hasNext()) {
//                    Shortcut sc = it.next();
//                    if (sc.from.outdegree() > 1 || sc.from.onWorkingBoundary(next_i, next_j)) {
//                        // extend
//                        sc.to = with.to;
//                        sc.inc = with.inc;
//                        sc.max = Math.max(sc.max, with.max);
//                        sc.to.getIncs(sc.inc).add(sc);
//                    } else {
//                        // remove
//                        sc.from.sc_up = null;
//                    }
//                    it.remove();
//                }
//            } else if (alive.diagonal == dead) {
//                alive.diagonal = null;
//                if (alive.up != null && alive.right != null) {
//                    // no extensions needed
//                } else if (alive.up != null) {
//                    // extend those from UP
//                    List<Shortcut> extend = alive.getIncs(Incoming.UP);
//                    Shortcut with = alive.sc_right;
//                    Iterator<Shortcut> it = extend.iterator();
//                    while (it.hasNext()) {
//                        Shortcut sc = it.next();
//                        if (sc.from.outdegree() > 1 || sc.from.onWorkingBoundary(next_i, next_j)) {
//                            // extend
//                            sc.to = with.to;
//                            sc.inc = with.inc;
//                            sc.max = Math.max(sc.max, with.max);
//                            sc.to.getIncs(sc.inc).add(sc);
//                        } else {
//                            // remove
//                            sc.from.sc_right = null;
//                        }
//                        it.remove();
//                    }
//
//                } else { // alive.right != null
//                    // extend those from RIGHT
//                    List<Shortcut> extend = alive.getIncs(Incoming.RIGHT);
//                    Shortcut with = alive.sc_up;
//                    Iterator<Shortcut> it = extend.iterator();
//                    while (it.hasNext()) {
//                        Shortcut sc = it.next();
//                        if (sc.from.outdegree() > 1 || sc.from.onWorkingBoundary(next_i, next_j)) {
//                            // extend
//                            sc.to = with.to;
//                            sc.inc = with.inc;
//                            sc.max = Math.max(sc.max, with.max);
//                            sc.to.getIncs(sc.inc).add(sc);
//                        } else {
//                            // remove
//                            sc.from.sc_up = null;
//                        }
//                        it.remove();
//                    }
//                }
//            } else if (alive.right == dead) {
//                alive.right = null;
//
//                List<Shortcut> extend;
//                Shortcut with = alive.sc_right;
//                if (alive.diagonal != null) {
//                    extend = alive.getIncs(Incoming.DIAGRIGHT);
//                } else { // alive.up != null
//                    extend = alive.getIncs(Incoming.UP);
//                }
//                Iterator<Shortcut> it = extend.iterator();
//                while (it.hasNext()) {
//                    Shortcut sc = it.next();
//                    if (sc.from.outdegree() > 1 || sc.from.onWorkingBoundary(next_i, next_j)) {
//                        // extend
//                        sc.to = with.to;
//                        sc.inc = with.inc;
//                        sc.max = Math.max(sc.max, with.max);
//                        sc.to.getIncs(sc.inc).add(sc);
//                    } else {
//                        // remove
//                        sc.from.sc_right = null;
//                    }
//                    it.remove();
//                }
//            }
//        }
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
        Node newNode = new Node(i, j, gridValues[i][j]);
        newNode.setParent(candidateParent);

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
