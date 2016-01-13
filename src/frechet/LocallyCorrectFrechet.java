package frechet;

import delayspace.DelaySpace;
import utils.Trajectory;

public class LocallyCorrectFrechet {
    public static Matching compute(DelaySpace delaySpace, Trajectory traject1, Trajectory traject2) {
        if (traject1 == Trajectory.EMPTY_TRAJECTORY && traject2 == Trajectory.EMPTY_TRAJECTORY) {
            return Matching.EMPTY_MATCHING;
        }
        
        LCFMTree tree = new LCFMTree(delaySpace);
        tree.buildTree();
        Matching matching = Matching.createFrechetMatching(tree, traject1, traject2, delaySpace.isDirectional());
        return matching;
    }

}
