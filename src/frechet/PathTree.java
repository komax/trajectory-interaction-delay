package frechet;

import java.util.Set;
import frechet.Node.NodeType;

class PathTree {
	protected Node[][] grid;
	private double[][] gridValues;

	public PathTree(double[][] grid, int numRows, int numColumns) {
		this.grid = new Node[numRows][numColumns];
		this.gridValues = grid;
		Node root = new Node(null, 0, 0, grid[0][0]);
		root.status = NodeType.GROWTHNODE;
		this.grid[0][0] = root;
	}
    

	private boolean isEmpty(int i, int j) {
		if (i >= grid.length) return false;
		if (j >= grid[0].length) return false;
		if (grid[i][j] == null) 
			return true;
		return false;
	}

	public boolean isGrowthNode(Node n) {
		if (n == null) {
			return true;
		}
		return n.status.equals(NodeType.GROWTHNODE);
	}

	public boolean isLivingNode(Node n) {
		if (n == null) {
			return true;
		}
		return n.status.equals(NodeType.LIVINGNODE);
	}

	public boolean isDeadNode(Node n) {
		if (n == null) {
			return true;
		}
		return n.status.equals(NodeType.DEADNODE);
	}

	protected void add(int i, int j) {
		///        System.out.printf("Adding node: (%d, %d)\n",i,j);
		/* add grid[i][j] to the tree */
		// three pairs of candidate parents: N+E, NE+E, N+NE 
		Node parent = selectParent(i, j);

		///        System.out.printf("rows: %d columns: %d\n",gridValues.length,gridValues[0].length);
		///        System.out.println(gridValues[i]);
		///        System.out.println(gridValues[i][j]);
		parent.status = NodeType.LIVINGNODE;

		// is [i-1][j-1] dead?
		if ((i != 0) && (j != 0)) {
			Node n = grid[i-1][j-1];
			if (!parent.equals(n) && !parent.parent.equals(n)) {
				n.status = NodeType.DEADNODE;
			}
		}
		Node newNode = new Node(parent, i, j, gridValues[i][j]);
///		System.out.printf("Joining nodes: (%d, %d) and (%d, %d)\n",parent.i, parent.j, i, j);

		grid[i][j] = newNode;
		if ((parent.i < i) && (parent.j < j)) {
			parent.northEast = newNode;
		}
		else if (parent.i == i) {
			parent.north = newNode;
			assert (parent.j + 1) == j;
		}
		else {
			parent.east = newNode;
			assert parent.j == j && (parent.i + 1) == i;
		}
///		System.out.printf("Adding node (%d, %d): %f with parent (%d, %d)\n",i,j, newNode.value, parent.i, parent.j);
		//        if (((i != 0) && (j != 0)) && isDeadNode(grid[i-1][j-1])) {
			//            // remove dead path ending in grid[i-1][j-1]
		//            System.out.printf("(%d, %d) is dead\n",i-1, j-1);
		//            Node current = grid[i-1][j-1];
		//            while ((current != null ) && isDeadNode(current)) {
		//                System.out.printf("Current: (%d, %d)\n",current.i,current.j); 
		//                current.dead = true;
		//                current = current.parent;
		//            }
		//
		//        }
	}

    Node selectParent2(int i, int j) {
        Node candidateSouth = grid[i-1][j];
        Node candidateSouthWest = grid[i-1][j];
        Node candidateWest = grid[i-1][j-1];
        
        Node currentCandidate = candidateSouth;
///        if (
// Need to add shortcuts. To start with, a shortcut is just a link to the
// parent. 
        return null;        
    }

	Node selectParent(int i,int j) {
		//System.out.printf("Selecting parent of (%d, %d)\n",i, j);
		Node[] candidates = new Node[3];
		if (i > 0)
			candidates[0] = grid[i-1][j]; // West
		if (i > 0 && j > 0)
			candidates[1] = grid[i-1][j-1]; // South West
		if (j > 0)
			candidates[2] = grid[i][j-1]; // South

///        System.out.printf("Candidates for parent of (%d, %d): ", i, j);
///        for (int q = 0; q < 3; q++) {
///            if (candidates[q] != null)
///                System.out.printf("(%d, %d) ",candidates[q].i, candidates[q].j);
///        }
///        System.out.println();

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
///				System.out.printf("Comparing c: (%d, %d) and cPrime: (%d, %d)\n", c.i, c.j, cPrime.i, cPrime.j);
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
///	    lol c always wins
///        System.out.printf("Comparing c: (%d, %d) and cPrime: (%d, %d)\n", c.i, c.j, cPrime.i, cPrime.j);
		if (cPrime == null) return c;
		if (c == null) System.out.println("This shouldn't happen");
		Node nearestCommonAncestor = c.nearestCommonAncestor(cPrime);
///		System.err.printf("NCA of %s and %s is (%d, %d)\n", c, cPrime, nearestCommonAncestor.i, nearestCommonAncestor.j);
		Set<Node> pathC = c.pathToRoot();
		Set<Node> pathCPrime = cPrime.pathToRoot();


		// dominant value on path from c to NCA of c and c'
		double dominantC = 0;

		pathC.removeAll(cPrime.pathToRoot());

///        System.out.println("pathC: ");
        
		for (Node n: pathC) {
///            System.out.printf("(%d, %d) ", n.i, n.j);
			double value = grid[n.i][n.j].value;
			if (dominantC < value) {
				dominantC = value;
			}
		}
///		System.out.println();
///		System.err.printf("dominantC: %f\n",dominantC);

		// dominant value on path from c' to NCA of c and c'
		double dominantCPrime = 0;

///        System.out.println("pathCPrime: ");
		pathCPrime.removeAll(c.pathToRoot());
		for (Node n: pathCPrime) {
		    
///            System.out.printf("(%d, %d) ", n.i, n.j);
			double value = grid[n.i][n.j].value;
			if (dominantCPrime < value) {
				dominantCPrime = value;
			}
		}
///		System.out.println();
///		System.err.printf("dominantCPrime: %f\n",dominantCPrime);
		if (dominantCPrime == dominantC) {
			// Oh no! We need to break ties. Ummm. shit.
			return null;
		}
		if (dominantC < dominantCPrime) {
///		    System.out.println("c wins");
			return c;
		}
///		System.out.println("cPrime wins");
		return cPrime;
	}

}
