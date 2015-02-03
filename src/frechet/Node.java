package frechet;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class Node implements Comparable<Node> {

    private final int i, j;
    double value;
    Node parent;
    private Node up;
    private Node right;
    private Node diagonal;
    
    private Shortcut shortcutUp;
    private Shortcut shortcutRight;

    public Node(Node parent, int i, int j, double value) {
        this.parent = parent;
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
    

    public LinkedHashSet<Node> pathToRoot() {
        Node temp = this;
        LinkedHashSet<Node> path = new LinkedHashSet<Node>();
        while (temp != null) {
            path.add(temp);
            temp = temp.parent;
        }
        return path;
    }
    
    public LinkedHashSet<Node> pathTo(Node ancestor) {
        Node currentNode = this;
        LinkedHashSet<Node> path = new LinkedHashSet<>();
        while (currentNode != null) {
            if (currentNode.equals(ancestor)) {
                break;
            }
            path.add(currentNode);
            currentNode = currentNode.parent;
        }
        return path;
    }
    
    public double maxValueOnPathTo(Node ancestor) {
        // FIXME Use shortcuts.
        double maxVal = Double.MIN_VALUE;
        for (Node currentNode : pathTo(ancestor)) {
            if (currentNode.value > maxVal) {
                maxVal = currentNode.value;
            }
        }
        return maxVal;
    }

    public Node nearestCommonAncestor(Node other) {
        // FIXME more efficient implementation.
        LinkedHashSet<Node> path1 = this.pathToRoot();
        Set<Node> path2 = other.pathToRoot();
        path1.retainAll(path2);
        Iterator<Node> it = path1.iterator();
        return it.next();
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

    public int compareTo(Node rhs) {
        // if this < obj return -1
        if (rhs == this) {
            return 0;
        } else if (rhs == null) {
            throw new NullPointerException("Node is null: " + rhs);
        } else {
            return Integer.valueOf(i + j).compareTo(Integer.valueOf(rhs.i + rhs.j));
        }
    }

    @Override
    public int hashCode() {
        return 31 * i + j;
    }
    
    public int getIndexTraject1() {
        return i;
    }
    
    public int getIndexTraject2() {
        return j;
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
    
}
