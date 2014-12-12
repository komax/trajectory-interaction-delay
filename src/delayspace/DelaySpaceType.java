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
public enum DelaySpaceType {
    
    USUAL(false),
    DIRECTIONAL_DISTANCE(true),
    DISPLACEMENT(false),
    DYNAMIC_INTERACTION(true),
    HEADING(true);
    
    private final boolean directional;

    private DelaySpaceType(boolean directional) {
        this.directional = directional;
    }
    
    public boolean isDirectional() {
        return directional; 
    }
};
