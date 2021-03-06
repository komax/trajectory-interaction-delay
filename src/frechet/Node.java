package frechet;

import static frechet.Shortcut.NO_SHORTCUT;
import java.util.LinkedList;
import java.util.List;

public class Node {
    
    public static final Node NULL_NODE = new Node(-1, -1, Double.MIN_VALUE);

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
        this.parent = NULL_NODE;
        this.i = i;
        this.j = j;
        this.value = value;
        this.up = NULL_NODE;
        this.right = NULL_NODE;
        this.diagonal = NULL_NODE;
        this.shortcutUp = NO_SHORTCUT;
        this.shortcutRight = NO_SHORTCUT;
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
        
        if (up != NULL_NODE) {
            outgoingEdges += 1;
        }
        
        if (right != NULL_NODE) {
            outgoingEdges += 1;
        }
        
        if (diagonal != NULL_NODE) {
            outgoingEdges += 1;
        }
        
        return outgoingEdges;
    }
    
    public boolean isDead() {
        return !isRoot() && outdegree() == 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == NULL_NODE) {
            return false;
        }
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
        if (this == NULL_NODE) {
            return "NullNode";
        }
        StringBuilder stringBuilder = new StringBuilder("Node(cell = (");
        stringBuilder.append(i);
        stringBuilder.append(" , ");
        stringBuilder.append(j);
        stringBuilder.append(") parent = ");
        if (isRoot()) {
            stringBuilder.append("(0, 0)");
        } else if (parent != NULL_NODE) {
            stringBuilder.append("(");
            stringBuilder.append(parent.i);
            stringBuilder.append(", ");
            stringBuilder.append(parent.j);
            stringBuilder.append(")");
        } else {
            stringBuilder.append("null");
        }
        stringBuilder.append("\nshortcutUp = ");
        if (hasShortcutUp()) {
            stringBuilder.append(shortcutUp.toString());
        } else {
            stringBuilder.append("null");
        }
        stringBuilder.append("\nshortcutRight = ");
        if (hasShortcutRight()) {
            stringBuilder.append(shortcutRight.toString());
        } else {
            stringBuilder.append("null");
        }
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
        if (this == NULL_NODE) {
            return NULL_NODE;
        }
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

    public void setUpNode(Node upNode) {
        this.up = upNode;
    }
    
    public Node getUpNode() {
        return up;
    }
    
    public Node getDiagonalNode() {
        return diagonal;
    }
    
    public Node getRightNode() {
        return right;
    }
    
    public boolean isLeftParent() {
        if (!isRoot() && parent.right != NULL_NODE) {
            return parent.right == this;
        } else {
            return false;
        }
    }
    
    public boolean isDiagnoalParent() {
        if (!isRoot() && parent.diagonal != NULL_NODE) {
            return parent.diagonal == this;
        } else {
            return false;
        }
    }
    
    public boolean isDownParent() {
        if (!isRoot() && parent.up != NULL_NODE) {
            return parent.up == this;
        } else {
            return false;
        }
    }
    
    public void setShortcutUp(Shortcut shortcutUp) {
        this.shortcutUp = shortcutUp;
    }
    
    public void setShortcutRight(Shortcut shortcutRight) {
        this.shortcutRight = shortcutRight;
    }
    
    public boolean hasShortcutUp() {
        return shortcutUp != NO_SHORTCUT;
    }
    
    public boolean hasShortcutRight() {
        return shortcutRight != NO_SHORTCUT;
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
    
    public static boolean isBetterThan(Node thisNode, Node thatNode) {
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
        
        if (thatNode == null) {
            throw new RuntimeException("this is null");
        }
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
                        //if (thisNode.parent.shortcutRight == null) {
                        //    System.out.println(thisNode);
                        //    System.out.println(thisNode.parent);
                        //}
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
            if (thisNode == NULL_NODE) {
                throw new RuntimeException("this node is null node");
            }
            if (thisNode == null) {
               break;
               // throw new RuntimeException("this node is null");
            }
        }
        
        return thisMaxValue < thatMaxValue + utils.Utils.EPSILON;
    }
    
    
}
