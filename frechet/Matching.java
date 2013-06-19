package frechet;

import frechet.*;
import java.util.ArrayList;

public class Matching {
    public int[] i;
    public int[] j;


    public Matching(ArrayList<Node> path) {
        i = new int[path.size()];
        j = new int[path.size()];

        for (int p = 0; p < path.size(); p++) {
            Node node = path.get(p);
            System.out.printf("(%d, %d)\n",node.i, node.j);
            i[p] = node.i;
            j[p] = node.j;
        } 
    }

}

