package frechet;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class Node implements Comparable<Node> {

    public enum NodeType {
        
        GROWTHNODE, LIVINGNODE, DEADNODE
    };

    int i, j;
    double value;
    Node parent;
    Node north;
    Node east;
    Node northEast;
    NodeType status;

    public Node(Node parent, int i, int j, double value) {
        this.parent = parent;
        this.i = i;
        this.j = j;
        this.value = value;
        this.north = null;
        this.east = null;
        this.northEast = null;
        this.status = NodeType.GROWTHNODE;
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
            if (currentNode == ancestor) {
                break;
            }
            path.add(currentNode);
            currentNode = currentNode.parent;
        }
        return path;
    }
    
    public double maxValueOnPathTo(Node ancestor) {
        double maxVal = Double.MIN_VALUE;
        for (Node currentNode : pathTo(ancestor)) {
            if (currentNode.value > maxVal) {
                maxVal = currentNode.value;
            }
        }
        return maxVal;
    }

    public Node nearestCommonAncestor(Node other) {
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
}
