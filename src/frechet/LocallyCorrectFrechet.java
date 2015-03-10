package frechet;

import delayspace.DelaySpace;
import utils.Trajectory;

public class LocallyCorrectFrechet {
    public static Matching compute(DelaySpace delaySpace, Trajectory traject1, Trajectory traject2) {
        int numRows = traject1.length();
        int numColumns = traject2.length();
        
        if ((delaySpace.numberColumns() != numColumns) || (delaySpace.numberRows() != numRows)) {
            throw new RuntimeException("Size of grid and size of trajectories disagree\n");
        }
        
        LCFMTree tree = new LCFMTree(delaySpace);
        tree.buildTree();
        System.out.println(tree);
        Matching matching = new Matching(tree, traject1, traject2, delaySpace.isDirectional());
        return matching;
    }

}
