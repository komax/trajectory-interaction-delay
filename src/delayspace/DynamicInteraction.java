/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package delayspace;

/**
 *
 * @author max
 */
public final class DynamicInteraction extends DelaySpace {
    private final double[][] displacements;
    private final double[][] headingDelaySpace;

    public DynamicInteraction(double[][] trajectory1, double[][] trajectory2, double alpha) {
        super(trajectory1, trajectory2);
        
        this.displacements = (new Displacement(trajectory1, trajectory2, alpha)).delaySpace;
        this.headingDelaySpace = (new Heading(trajectory1, trajectory2)).delaySpace;
    }

    @Override
    protected void computeDelaySpace() {
        for (int i = 0; i < trajectory1.length; i++) {
            for (int j = 0; j < trajectory2.length; j++) {
                double dynamicInteractionValue = 1.0 - (displacements[i][j] * (headingDelaySpace[i][j] - 1.0));
                delaySpace[i][j] = dynamicInteractionValue;
            }
        }
        
    }

    @Override
    public boolean isDirectional() {
        return true;
    }
    
}
