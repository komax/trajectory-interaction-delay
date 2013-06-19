package frechet;
import java.util.TreeSet;
import java.util.Stack;
import java.lang.Math;
import frechet.Node;

class PathTree {
    protected Node[][] grid;
    private double[][] gridValues;

    


    public PathTree(double[][] grid, int n, int m) {
        
        this.grid = new Node[n+1][m+1];
        this.gridValues = grid;
        Node root = new Node(null, 0, 0, grid[0][0]);
        this.grid[0][0] = root;

    }

    private boolean isEmpty(int i, int j) {
        if (grid[i][j] == null) 
            return true;
        return false;
    }

    public boolean isGrowthNode(Node n) {
        if (n == null) return false;
        if ((n.north == null) && this.isEmpty(n.i,n.j+1))
            return true;
        if ((n.east == null) && this.isEmpty(n.i+1,n.j))
            return true;
        if ((n.northEast == null) && this.isEmpty(n.i+1, n.j+1))
            return true;
        return false;
    }

    public boolean isDeadNode(Node n) {
    	
        if (n.dead) {
            return true;
        }
///        System.out.printf("Performing DFS for growth nodes from (%d,%d)\n",n.i,n.j);
        // depth first search
        
        Stack<Node> fringe = new Stack<Node>();
        
        fringe.add(n);
        while (!fringe.isEmpty()) {
            Node current = fringe.pop();
            if (isGrowthNode(current)) {
                return false;
            }
            if (current == null) {
            	continue;
			}
            if (current.north != null) {
                fringe.add(current.north);
            }
            if (current.northEast != null) {
                fringe.add(current.northEast);
            }
            if (current.east != null) {
                fringe.add(current.northEast);
            }
        }
        return true;
    }

    protected void add(int i, int j) {
///        System.out.printf("Adding node: (%d, %d)\n",i,j);
        /* add grid[i][j] to the tree */
        // three pairs of candidate parents: N+E, NE+E, N+NE 
        Node parent = selectParent(i, j);
        Node newNode = new Node(parent, i, j, gridValues[i][j]);
        grid[i][j] = newNode;
        if ((parent.i < i) && (parent.j < j)) {
            parent.northEast = newNode;
        }
        else if (parent.i == i) {
            parent.north = newNode;
        }
        else {
            parent.east = newNode;
        }
        if (((i != 0) && (j != 0)) && isDeadNode(grid[i-1][j-1])) {
            // remove dead path ending in grid[i-1][j-1]
///            System.out.printf("(%d, %d) is dead\n",i-1, j-1);
            Node current = grid[i-1][j-1];
            while (isDeadNode(current)) {
                current.dead = true;
                current = current.parent;
            }

        }
    }



    Node selectParent(int i,int j) {
///        System.out.printf("Selecting parent of (%d, %d)\n",i, j);
        Node[] candidates = new Node[3];
        if (i > 0)
            candidates[0] = grid[i-1][j]; // West
        if (i > 0 && j > 0)
            candidates[1] = grid[i-1][j-1]; // South West
        if (j > 0)
            candidates[2] = grid[i][j-1]; // South

///        for (int q = 0; q < 3; q++) {
///            if (candidates[q] != null)
///                System.out.printf("(%d, %d)\n",candidates[q].i, candidates[q].j);
///        }

        for (int q = 0; q < 3; q++) {
            if ((candidates[(q) % 3] == null) && (candidates[(q+1) % 3] == null)) {

                return candidates[(q+2) % 3];
            }
        }

        for (int x = 0; x < 3; x++) {
            Node c = candidates[x];
            if (c == null) {
                continue;
            }
            boolean satisfies = true;
            for (int y = x+1; y < 3; y++) {
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
        System.out.printf("Error: no candidates satisfy.\n");
        return null;
    }

    Node compareParent(Node c, Node cPrime) {
        if (cPrime == null) return c;
        if (c == null) System.out.println("This shouldn't happen");
        Node nearestCommonAncestor = c.nearestCommonAncestor(cPrime);
        TreeSet<Node> pathC = c.pathToRoot();
        TreeSet<Node> pathCPrime = cPrime.pathToRoot();


        // dominant value on path from c to NCA of c and c'
        double dominantC = 0;

        pathC.removeAll(pathCPrime);
        for (Node n: pathC) {
            double value = grid[n.i][n.j].value;
                if (dominantC < value) {
                    dominantC = value;
                }
        }

        // dominant value on path from c' to NCA of c and c'
        double dominantCPrime = 0;

        pathCPrime.removeAll(c.pathToRoot());
        for (Node n: pathCPrime) {
            double value = grid[n.i][n.j].value;
                if (dominantCPrime < value) {
                    dominantCPrime = value;
                }
        }
        if (dominantCPrime == dominantC) {
            // Oh no! We need to break ties. Ummm. shit.
            return null;
        }
        if (dominantC < dominantCPrime) {
            return c;
        }
        return cPrime;

    }


    

}
