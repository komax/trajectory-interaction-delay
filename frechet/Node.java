package frechet;
import java.util.TreeSet;

public class Node implements Comparable<Node> {
        int i, j;
        double value;
        Node parent;
        Node north;
        Node east;
        Node northEast;

        public Node(Node parent, int i, int j, double value) {
            this.parent = parent;
            this.i = i;
            this.j = j;
            this.value = value;
            this.north = null;
            this.east = null;
            this.northEast = null;
        }




        TreeSet<Node> pathToRoot() {
            Node temp = this.parent;
            TreeSet<Node> path = new TreeSet<Node>();
            while ((temp.i != 0) && (temp.j != 0)) {
                path.add(temp);
            }
            path.add(temp); // should be root
            return path;
        }

        Node nearestCommonAncestor(Node other) {
            TreeSet<Node> path1 = this.pathToRoot();
            TreeSet<Node> path2 = other.pathToRoot();
            path1.retainAll(path2);
            return path1.first();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (obj == this) return true;
            if (!(obj instanceof Node)) return false;

            Node rhs = (Node) obj;
            return (this.i == rhs.i) && (this.j == rhs.j);
        }

        public int compareTo(Node rhs) {
            // if this < obj return -1
            if (rhs == null) throw new NullPointerException();
            if (rhs == this) return 0;

            if ((this.i == rhs.i) && (this.j == rhs.j)) {
                return 0;
            }
            if ((this.i <= rhs.i) && (this.j <= rhs.j)) {
                return -1;
            }
            return 1;
        }
        @Override
        public int hashCode() {
            return 10000 * i + j;
        }


    }
