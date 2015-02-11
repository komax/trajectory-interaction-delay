/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frechet;

/**
 *
 * @author max
 */
public class Shortcut {
    private Node from;
    private Node to;
    private double maxValue;
    private Direction incomingDirection;
    
    public Shortcut(Node from, Node to, double maxValue, Direction incomingDirection) {
        this.from = from;
        this.to = to;
        this.maxValue = maxValue;
        this.incomingDirection = incomingDirection;
    }
    
    public Node getFrom() {
        return from;
    }
    
    public Node getTo() {
        return to;
    }
    
    public double getMaxValue() {
        return maxValue;
    }
    
    public Direction getIncomingDirection() {
        return incomingDirection;
    }
    
    public void setFrom(Node from) {
        this.from = from;
    }
    
    public void setTo(Node to) {
        this.to = to;
    }
    
    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }
    
    public void setIncomingDirection(Direction incomingDirection) {
        this.incomingDirection = incomingDirection;
    }
    
}
