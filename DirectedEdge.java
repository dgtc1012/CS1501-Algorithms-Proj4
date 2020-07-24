package Airline;

/******************************************************************************
 *  Compilation:  javac DirectedEdge.java
 *  Execution:    java DirectedEdge
 *  Dependencies: StdOut.java
 *
 *  Immutable weighted directed edge.
 *
 ******************************************************************************/
/**
 *  The <tt>DirectedEdge</tt> class represents a weighted edge in an 
 *  {@link EdgeWeightedDigraph}. Each edge consists of two integers
 *  (naming the two vertices) and a real-value weight. The data type
 *  provides methods for accessing the two endpoints of the directed edge and
 *  the weight.
 *  <p>
 *  For additional documentation, see <a href="http://algs4.cs.princeton.edu/44sp">Section 4.4</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 * 
 * modified by Dannah Gersh
 */

public class DirectedEdge { 
    private final int v;
    private final int w;
    private final double distance;
    private final double price;

    /**
     * Initializes a directed edge from vertex <tt>v</tt> to vertex <tt>w</tt> with
     * the given <tt>distance</tt>.
     * @param v the tail vertex
     * @param w the head vertex
     * @param distance the distance of the directed edge
     * @param price the price of the directed edge
     * @throws IndexOutOfBoundsException if either <tt>v</tt> or <tt>w</tt>
     *    is a negative integer
     * @throws IllegalArgumentException if <tt>distance</tt> is <tt>NaN</tt>
     */
    public DirectedEdge(int v, int w, double distance, double price) {
        if (v < 0) throw new IndexOutOfBoundsException("Vertex names must be nonnegative integers");
        if (w < 0) throw new IndexOutOfBoundsException("Vertex names must be nonnegative integers");
        if (Double.isNaN(distance)) throw new IllegalArgumentException("Distance is NaN");
        if (Double.isNaN(price)) throw new IllegalArgumentException("Price is NaN");
        this.v = v;
        this.w = w;
        this.distance = distance;
        this.price = price;
    }

    /**
     * Returns the tail vertex of the directed edge.
     * @return the tail vertex of the directed edge
     */
    public int from() {
        return v;
    }

    /**
     * Returns the head vertex of the directed edge.
     * @return the head vertex of the directed edge
     */
    public int to() {
        return w;
    }
    
    public int other(int vertex) {
        if      (vertex == v) return w;
        else if (vertex == w) return v;
        else throw new IllegalArgumentException("Illegal endpoint");
    }

    /**
     * Returns the distance of the directed edge.
     * @return the distance of the directed edge
     */
    public double distance() {
        return distance;
    }

    /**
     * Returns the distance of the directed edge.
     * @return the distance of the directed edge
     */
    public double price() {
        return price;
    }
    
    public boolean exists(DirectedEdge e){
        return (this.from() == e.from() && this.to() == e.to() && this.distance() == e.distance() && this.price() == e.price());
    }
    
    /**
     * Returns a string representation of the directed edge.
     * @return a string representation of the directed edge
     */
    public String toString() {
        return v + " " + w + " " + String.format("%5.2f", distance) + " " + String.format("%5.2f", price);
    }

    /**
     * Unit tests the <tt>DirectedEdge</tt> data type.
     */
    public static void main(String[] args) {
        DirectedEdge e = new DirectedEdge(12, 34, 5.67, 59);
        System.out.println(e);
    }
}
