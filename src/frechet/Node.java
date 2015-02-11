package frechet;

import java.util.LinkedList;
import java.util.List;

public class Node {

    private final int i, j;
    private final double value;
    private Node parent;
    private Node up;
    private Node right;
    private Node diagonal;
    
    // Outgoing shortcuts
    private Shortcut shortcutUp;
    private Shortcut shortcutRight;
    
    // Incoming shortcuts
    private final List<Shortcut> sc_incs_up = new LinkedList<>();
    private final List<Shortcut> sc_incs_right = new LinkedList<>();
    private final List<Shortcut> sc_incs_diagup = new LinkedList<>();
    private final List<Shortcut> sc_incs_diagright = new LinkedList<>();

    public Node(int i, int j, double value) {
        this.parent = null;
        this.i = i;
        this.j = j;
        this.value = value;
        this.up = null;
        this.right = null;
        this.diagonal = null;
        this.shortcutUp = null;
        this.shortcutRight = null;
    }
    
    public boolean isRoot() {
        return i == 0 && j == 0;
    }
    
    public boolean isRightOf(Node otherNode) {
        return this.i > otherNode.i;
    }
    
    public boolean isInSameColumnAs(Node otherNode) {
        return this.i == otherNode.i;
    }
    
    public boolean isAboveOf(Node otherNode) {
        return isInSameColumnAs(otherNode) && this.j > otherNode.j;
    }
    
    public int outdegree() {
        int outgoingEdges = 0;
        
        if (up != null) {
            outgoingEdges += 1;
        }
        
        if (right != null) {
            outgoingEdges += 1;
        }
        
        if (diagonal != null) {
            outgoingEdges += 1;
        }
        
        return outgoingEdges;
    }
    
    public boolean isDead() {
        return !isRoot() && outdegree() == 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Node)) {
            return false;
        }

        Node rhs = (Node) obj;
        return (this.i == rhs.i) && (this.j == rhs.j);
    }

    @Override
    public int hashCode() {
        return 31 * i + j;
    }
    
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Node(i = ");
        stringBuilder.append(i);
        stringBuilder.append(" , j = ");
        stringBuilder.append(j);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
    
    public int getIndexTraject1() {
        return i;
    }
    
    public int getIndexTraject2() {
        return j;
    }
    
    public Node getParent() {
        return parent;
    }
    
    public double getValue() {
        return value;
    }
    
    public void setParent(Node parent) {
        this.parent = parent;
    }
    
    public void setDiagonalNode(Node diagonalNode) {
        this.diagonal = diagonalNode;
    }
    
    public void setRightNode(Node rightNode) {
        this.right = rightNode;
    }
    
    public boolean hasRightNode() {
        return right != null;
    }

    public void setUpNode(Node upNode) {
        this.up = upNode;
    }
    
    public boolean hasUpNode() {
        return up != null;
    }
    
    public Node getUpNode() {
        return up;
    }
    
    public boolean hasDiagonalNode() {
        return diagonal != null;
    }
    
    public Node getDiagonalNode() {
        return diagonal;
    }
    
    public Node getRightNode() {
        return right;
    }
    
    public void setShortcutUp(Shortcut shortcutUp) {
        this.shortcutUp = shortcutUp;
    }
    
    public void setShortcutRight(Shortcut shortcutRight) {
        this.shortcutRight = shortcutRight;
    }
    
    public boolean hasShortcutUp() {
        return shortcutUp != null;
    }
    
    public boolean hasShortcutRight() {
        return shortcutRight != null;
    }
    
    public Shortcut getShortcutUp() {
        return shortcutUp;
    }
    
    public Shortcut getShortcutRight() {
        return shortcutRight;
    }
    
    public List<Shortcut> getIncomingShortcuts(Direction direction) {
        switch (direction) {
            case UP:
                return sc_incs_up;
            case RIGHT:
                return sc_incs_right;
            case DIAG_UP:
                return sc_incs_diagup;
            case DIAG_RIGHT:
                return sc_incs_diagright;
            default:
                return new LinkedList<>();
        }
    }
    
    public boolean isParentOf(Node otherNode) {
        if (otherNode != null) {
            return otherNode.equals(this.parent);
        } else {
            return this.parent.equals(otherNode);
        }
        
    }
    
    public boolean isBetterThan(Node thatNode) {
        Node thisNode = this;
        
        if (thisNode.isParentOf(thatNode)) {
            return false;
        }
        
        if (thatNode.isParentOf(thisNode)) {
            return thatNode.value > 0.0;
        }
        
        if (thisNode.parent.equals(thatNode.parent)) {
            return thisNode.value < thatNode.value;
        }
        
        double thisMaxValue = Double.MIN_VALUE;
        double thatMaxValue = Double.MIN_VALUE;
        
        while (!thisNode.equals(thatNode)) {
            if (thisNode.isRightOf(thatNode) || (thisNode.isInSameColumnAs(thatNode) && thisNode.isAboveOf(thatNode))) {
                // thisNode is right or above of thatNode:
                // hence compare the right shortcut of thisNode to the value of thatNode
                if (thisNode.hasShortcutRight()) {
                    // Use the value of the own right shortcut and follow the shortcut.
                    thisMaxValue = Math.max(thisMaxValue, thisNode.shortcutRight.getMaxValue());
                    thisNode = thisNode.shortcutRight.getTo();
                } else {
                    // Use the right shortcut of the parent to compute the maxValue.
                    thisMaxValue = Math.max(thisMaxValue, thisNode.value);
                    if (thisNode.parent.isRoot()) {
                        // Do not follow the shortcut if the parent is already the root.
                        thisNode = thisNode.parent;
                    } else {
                        if (thisNode.parent.shortcutRight == null) {
                            System.out.println(thisNode);
                            System.out.println(thisNode.parent);
                        }
                        // Check whether the value of the parent's shortcut has a larger value.
                        thisMaxValue = Math.max(thisMaxValue, thisNode.parent.shortcutRight.getMaxValue());
                        // Follow the shorctut of the parent.
                        thisNode = thisNode.parent.shortcutRight.getTo();
                    }
                }
            } else {
                // thisNode is left or beneath of thatNode:
                // hence take the up shortcut of thatNode and compute the maximum on the path to the root.
                if (thatNode.hasShortcutUp()) {
                    // Use the value of the that up shortcut and follow it.
                    thatMaxValue = Math.max(thatMaxValue, thatNode.shortcutUp.getMaxValue());
                    thatNode = thatNode.shortcutUp.getTo();
                } else {
                    // thatNode does not have a shortcut: use the shortcut of the parent.
                    thatMaxValue = Math.max(thatMaxValue, thatNode.value);
                    if (thatNode.parent.isRoot()) {
                        // No need to follow as the parent is the root.
                        thatNode = thatNode.parent;
                    } else {
                        // Check if the value of the parent's up shortcut is larger than the current max value.
                        thatMaxValue = Math.max(thatMaxValue, thatNode.parent.shortcutUp.getMaxValue());
                        // Follow the parrent's up shortcut.
                        thatNode = thatNode.parent.shortcutUp.getTo();
                    }
                }
            }
        }
        
        return thisMaxValue < thatMaxValue + utils.Utils.EPSILON;
    }
    
    
}
