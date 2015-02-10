package frechet;

import java.util.Iterator;
import java.util.List;


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
        for (int i = 1; i < numRows; i++) {
            for (int j = 1; j < numColumns; j++) {
                System.out.println("i = "+i+" j = "+j);
                expandTree(i, j);
            }
        }
    }
    
    private Node bestCandidateParent(int i, int j) {
        Node down = grid[i][j-1];
        Node diagonal = grid[i-1][j-1];
        Node left = grid[i-1][j];
        
        Node bestCandidate = down;
        if (diagonal != null && diagonal.isBetterThan(bestCandidate)) {
            bestCandidate = diagonal;
        }
        
        if (left != null && left.isBetterThan(bestCandidate)) {
            bestCandidate = left;
        }
        return bestCandidate;
    }
    
    private boolean isNodeOnWorkingBoundary(Node node, int currentI, int currentJ) {
        int nextI;
        int nextJ;
        if (currentJ == columns - 1) {
            nextI = currentI + 1;
            nextJ = 1;
        } else {
            nextI = currentI;
            nextJ = currentJ + 1;
        }
        int nodeI = node.getIndexTraject1();
        int nodeJ = node.getIndexTraject2();
        if (nodeJ >= nextJ) {
            return nodeI >= nextI - 1;
        } else {
            return nodeI >= nextI;
        }
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
        
        // if [i,j-1] does not have a shortcut, create one, or use the up shortcut.
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
            down.setShortcutUp(shortcutDown);
            // Add the new shortcut as incoming shortcut to the to target.
            shortcutTo.getIncomingShortcuts(incoming).add(shortcutDown);
        }
        
        // if left [i,j-1] does not have a shortcut, create one, or use the right shortcut.
        if (!left.hasShortcutRight()) {
            Node shortcutFrom = left;
            Node shortcutTo;
            Direction incoming;
            double maxValue;
            
            if (left.getParent().isRoot()) {
                // If left is the root, create a shortcut to the root.
                shortcutTo = left.getParent();
                incoming = Direction.UP;
                maxValue = left.getValue();
            } else if (left.getParent().outdegree() == 1) {
                // if down has only one shortcut, use this shortcut information.
                Shortcut parentsRightShortcut = left.getParent().getShortcutRight();
                shortcutTo = parentsRightShortcut.getTo();
                incoming = parentsRightShortcut.getIncomingDirection();
                maxValue = Math.max(down.getValue(), parentsRightShortcut.getMaxValue());
            } else {
                // There is no shortcut from the parent, create a new one.
                shortcutTo = left.getParent();
                incoming = Direction.UP;
                maxValue = left.getValue();
            }
            // Create the shortcut using the information from above.
            Shortcut shortcut = new Shortcut(shortcutFrom, shortcutTo, maxValue, incoming);
            left.setShortcutRight(shortcut);
            // Add the new shortcut as incoming shortcut to the to target.
            shortcutTo.getIncomingShortcuts(incoming).add(shortcut);
        }

        // Create shortcuts for [i,j].
        // Preference order is [i,j-1] (down) > [i-1,j-1] (diagonal) > [i-1,j] (left).
        if (bestParent.equals(down)) {
            // Use the up shortcut from down as shortcut for the node.
            Shortcut downsUpShortcut = down.getShortcutUp();
            Node shortcutFrom = node;
            Node shortcutTo = downsUpShortcut.getTo();
            Direction shortcutIncomingDirection = downsUpShortcut.getIncomingDirection();
            double maxShortcutValue = Math.max(node.getValue(), downsUpShortcut.getMaxValue());
            
            // Create the shortcut and put it as up shortcut to the node.
            Shortcut shortcut = new Shortcut(shortcutFrom, shortcutTo, maxShortcutValue, shortcutIncomingDirection);
            node.setShortcutUp(shortcut);
            // Add the shortcut to the target as incoming shortcut.
            shortcutTo.getIncomingShortcuts(shortcutIncomingDirection).add(shortcut);
        } else if (bestParent.equals(diagonal)) {
            // If the best parent is diagonal, create two shortcuts for the current node.
            Node shortcutFrom = node;
            Node shortcutTo;
            double maxValue;
            Direction incomingDirection;
            // Create the right shortcut.
            if (diagonal.hasRightNode()) {
                shortcutTo = diagonal;
                incomingDirection = Direction.DIAG_RIGHT;
                maxValue = node.getValue();                
            } else {
                Shortcut diagonalsRightShortcut = diagonal.getShortcutRight();
                shortcutTo = diagonalsRightShortcut.getTo();
                incomingDirection = diagonalsRightShortcut.getIncomingDirection();
                maxValue = Math.max(node.getValue(), diagonalsRightShortcut.getMaxValue());
            }
            // Set right shortcut of the current node.
            Shortcut shortcutRight = new Shortcut(shortcutFrom, shortcutTo, maxValue, incomingDirection);
            node.setShortcutRight(shortcutRight);
            // Set the incoming references correctly.
            shortcutTo.getIncomingShortcuts(incomingDirection).add(shortcutRight);
            
            // Create the up shortcut.
            shortcutTo = null;
            maxValue = 0.0;
            incomingDirection = null;
            if (diagonal.hasUpNode()) {
                shortcutTo = diagonal;
                incomingDirection = Direction.DIAG_UP;
                maxValue = node.getValue();                
            } else {
                Shortcut diagonalsUpshortcut = diagonal.getShortcutUp();
                shortcutTo = diagonalsUpshortcut.getTo();
                incomingDirection = diagonalsUpshortcut.getIncomingDirection();
                maxValue = Math.max(node.getValue(), diagonalsUpshortcut.getMaxValue());
            }
            // Create the up shortcut for the current node.
            Shortcut shortcutUp = new Shortcut(shortcutFrom, shortcutTo, maxValue, incomingDirection);
            node.setShortcutUp(shortcutUp);
            // Set the new shortcut as incoming reference to the target node.
            shortcutTo.getIncomingShortcuts(incomingDirection).add(shortcutUp);
            
        } else {
            // Use the right shortcut from left as shortcut for the node.
            Shortcut leftsRightShortcut = left.getShortcutRight();
            Node shortcutFrom = node;
            Node shortcutTo = leftsRightShortcut.getTo();
            Direction shortcutIncomingDirection = leftsRightShortcut.getIncomingDirection();
            double maxShortcutValue = Math.max(node.getValue(), leftsRightShortcut.getMaxValue());
            
            // Create the shortcut and put it as right shortcut to the node.
            Shortcut shortcut = new Shortcut(shortcutFrom, shortcutTo, maxShortcutValue, shortcutIncomingDirection);
            node.setShortcutRight(shortcut);
            // Add the shortcut to the target as incoming shortcut.
            shortcutTo.getIncomingShortcuts(shortcutIncomingDirection).add(shortcut);      
        }
        
        if (diagonal.isDead()) {
            // Compress the tree if the diagonal node has no out going edges.
            removeDeadPaths(diagonal, i, j);
        }
    }

    private void removeDeadPaths(Node diagonal, int i, int j) {
        // Kill the diagonal dead branch.
        Node deadNode = diagonal;
        Node aliveNode = diagonal.getParent();
        while (aliveNode.outdegree() == 1) {
            // Kill alive and move up in the tree.
            aliveNode.setUpNode(null);
            aliveNode.setDiagonalNode(null);
            aliveNode.setRightNode(null);
            deadNode = aliveNode;
            aliveNode = aliveNode.getParent();
        }

        if (deadNode.equals(aliveNode.getUpNode())) {
            // dead node is above alive node
            aliveNode.setUpNode(null);
            List<Shortcut> extendShortcuts;
            Shortcut with = aliveNode.getShortcutUp();
            if (aliveNode.hasDiagonalNode()) {
                extendShortcuts = aliveNode.getIncomingShortcuts(Direction.DIAG_UP);
            } else {
                // alive has incoming shortcuts from right.
                extendShortcuts = aliveNode.getIncomingShortcuts(Direction.RIGHT);
            }
            Iterator<Shortcut> it = extendShortcuts.iterator();
            while (it.hasNext()) {
                Shortcut shortcut = it.next();
                Node from = shortcut.getFrom();
                if (from.outdegree() > 1 || isNodeOnWorkingBoundary(from, i, j)) {
                    // Extend the shortcuts.
                    double maxValue = Math.max(shortcut.getMaxValue(), with.getMaxValue());
                    Node to = with.getTo();
                    Direction incomingDirection = with.getIncomingDirection();
                    Shortcut newShortcut = new Shortcut(from, to, maxValue, incomingDirection);
                    to.getIncomingShortcuts(incomingDirection).add(newShortcut);
                } else {
                    // If from has only one up shortcut, delete it.
                    from.setShortcutUp(null);
                }
                // Make sure to remove the old shortcut.
                it.remove();
            }
        } else if (deadNode.equals(aliveNode.getDiagonalNode())) {
            // The dead node is diagonal to the alive node.
            aliveNode.setDiagonalNode(null);
            if (aliveNode.hasUpNode() && aliveNode.hasRightNode()) {
                // Nothing to do. No extensions are needed.
            } else if (aliveNode.hasUpNode()) {
                // extend the shortcuts from up.
                List<Shortcut> extendShortcuts = aliveNode.getIncomingShortcuts(Direction.UP);
                Shortcut with = aliveNode.getShortcutRight();
                Iterator<Shortcut> it = extendShortcuts.iterator();
                while (it.hasNext()) {
                    Shortcut shortcut = it.next();
                    Node from = shortcut.getFrom();
                    if (from.outdegree() > 1 || isNodeOnWorkingBoundary(from, i, j)) {
                        // Extend the shortcuts.
                        double maxValue = Math.max(shortcut.getMaxValue(), with.getMaxValue());
                        Node to = with.getTo();
                        Direction incomingDirection = with.getIncomingDirection();
                        Shortcut newShortcut = new Shortcut(from, to, maxValue, incomingDirection);
                        to.getIncomingShortcuts(incomingDirection).add(newShortcut);
                    } else {
                        // If from has only one up shortcut, delete it.
                        from.setShortcutRight(null);
                    }
                    // Make sure to remove the old shortcut.
                    it.remove();
                }
            } else {
                // alive.hasRightNode() == true
                List<Shortcut> extendShortcuts = aliveNode.getIncomingShortcuts(Direction.RIGHT);
                Shortcut with = aliveNode.getShortcutUp();
                Iterator<Shortcut> it = extendShortcuts.iterator();
                while (it.hasNext()) {
                    Shortcut shortcut = it.next();
                    Node from = shortcut.getFrom();
                    if (from.outdegree() > 1 || isNodeOnWorkingBoundary(from, i, j)) {
                        // Extend the shortcuts.
                        double maxValue = Math.max(shortcut.getMaxValue(), with.getMaxValue());
                        Node to = with.getTo();
                        Direction incomingDirection = with.getIncomingDirection();
                        Shortcut newShortcut = new Shortcut(from, to, maxValue, incomingDirection);
                        to.getIncomingShortcuts(incomingDirection).add(newShortcut);
                    } else {
                        // If from has only one up shortcut, delete it.
                        from.setShortcutUp(null);
                    }
                    // Make sure to remove the old shortcut.
                    it.remove();
                }
            }
        } else if (deadNode.equals(aliveNode.getRightNode())) {
            // dead node right from the alive node
            aliveNode.setRightNode(null);
            List<Shortcut> extendShortcuts;
            Shortcut with = aliveNode.getShortcutRight();
            if (aliveNode.hasDiagonalNode()) {
                extendShortcuts = aliveNode.getIncomingShortcuts(Direction.DIAG_RIGHT);
            } else {
                // alive has incoming shortcuts from up.
                extendShortcuts = aliveNode.getIncomingShortcuts(Direction.UP);
            }
            Iterator<Shortcut> it = extendShortcuts.iterator();
            while (it.hasNext()) {
                Shortcut shortcut = it.next();
                Node from = shortcut.getFrom();
                if (from.outdegree() > 1 || isNodeOnWorkingBoundary(from, i, j)) {
                    // Extend the shortcuts.
                    double maxValue = Math.max(shortcut.getMaxValue(), with.getMaxValue());
                    Node to = with.getTo();
                    Direction incomingDirection = with.getIncomingDirection();
                    Shortcut newShortcut = new Shortcut(from, to, maxValue, incomingDirection);
                    to.getIncomingShortcuts(incomingDirection).add(newShortcut);
                } else {
                    // If from has only one up shortcut, delete it.
                    from.setShortcutUp(null);
                }
                // Make sure to remove the old shortcut.
                it.remove();
            }
        }
    }
}
