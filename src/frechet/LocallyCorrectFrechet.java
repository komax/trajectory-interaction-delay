package frechet;

public class LocallyCorrectFrechet {
    public static Matching compute(double[][] distanceTerrain, double[][] traject1, double[][] traject2,
            int numRows, int numColumns) {
        if ((distanceTerrain[0].length != numColumns) || (distanceTerrain.length != numRows)) {
            throw new RuntimeException("Size of grid and size of trajectories disagree\n");
        }
        
        LCFMTree tree = new LCFMTree(distanceTerrain, numRows, numColumns);
        tree.buildTree();
        Matching matching = new Matching(tree, traject1, traject2);
        return matching;
    }

}
