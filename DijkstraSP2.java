/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Airline;
import java.util.*;

public class DijkstraSP2 {
    private double[] distTo;          // distTo[v] = distance  of shortest s->v path
    private DirectedEdge[] edgeTo;    // edgeTo[v] = last edge on shortest s->v path
    private IndexMinPQ<Double> pq;    // priority queue of vertices
    private int type;

    /**
     * Computes a shortest-paths tree from the source vertex <tt>s</tt> to every other
     * vertex in the edge-weighted digraph <tt>G</tt>.
     *
     * @param  G the edge-weighted digraph
     * @param  s the source vertex
     * @throws IllegalArgumentException if an edge weight is negative
     * @throws IllegalArgumentException unless 0 &le; <tt>s</tt> &le; <tt>V</tt> - 1
     */
    public DijkstraSP2(Graph G, int s, int type) {
        for (DirectedEdge e : G.edgesB()) {
            if(type == 1){
                if (e.distance() < 0)
                    throw new IllegalArgumentException("edge " + e + " has negative weight");
            }
            else if(type == 2){
                if (e.price() < 0)
                    throw new IllegalArgumentException("edge " + e + " has negative weight");
            }
        }
        //System.out.println(G.V());
        distTo = new double[G.V()];
        edgeTo = new DirectedEdge[G.V()];
        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s-1] = 0.0;
        this.type = type;
        // relax vertices in order of distance from s
        pq = new IndexMinPQ<Double>(G.V()+1);
        pq.insert(s, distTo[s-1]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            //System.out.println("v: "+v);
            for (DirectedEdge e : G.adjB(v-1))
                relax(e);
            //System.out.println(pq.isEmpty());
        }
        
        // check optimality conditions
        assert check(G, s);
        //System.out.println("done");
    }

    // relax edge e and update pq if changed
    private void relax(DirectedEdge e) {
        int v = e.from(), w = e.to();
        if(type == 1){
            if (distTo[w-1] > distTo[v-1] + e.distance()) {
                distTo[w-1] = distTo[v-1] + e.distance();
                edgeTo[w-1] = e;
                //System.out.println("w: "+w);
                if (pq.contains(w)) pq.decreaseKey(w, distTo[w-1]);
                else                pq.insert(w, distTo[w-1]);
            }
        }
        else if(type==2){
            if (distTo[w-1] > distTo[v-1] + e.price()) {
                distTo[w-1] = distTo[v-1] + e.price();
                edgeTo[w-1] = e;
                //System.out.println("w: "+w);
                if (pq.contains(w)) pq.decreaseKey(w, distTo[w-1]);
                else                pq.insert(w, distTo[w-1]);
            }
        }
        else{
            if (distTo[w-1] > distTo[v-1] + 1) {
                distTo[w-1] = distTo[v-1] + 1;
                edgeTo[w-1] = e;
                //System.out.println("w: "+w);
                if (pq.contains(w)) pq.decreaseKey(w, distTo[w-1]);
                else                pq.insert(w, distTo[w-1]);
            }
        }
    }

    /**
     * Returns the length of a shortest path from the source vertex <tt>s</tt> to vertex <tt>v</tt>.
     * @param  v the destination vertex
     * @return the length of a shortest path from the source vertex <tt>s</tt> to vertex <tt>v</tt>;
     *         <tt>Double.POSITIVE_INFINITY</tt> if no such path
     */
    public double distTo(int v) {
        return distTo[v-1];
    }

    /**
     * Returns true if there is a path from the source vertex <tt>s</tt> to vertex <tt>v</tt>.
     *
     * @param  v the destination vertex
     * @return <tt>true</tt> if there is a path from the source vertex
     *         <tt>s</tt> to vertex <tt>v</tt>; <tt>false</tt> otherwise
     */
    public boolean hasPathTo(int v) {
        return distTo[v-1] < Double.POSITIVE_INFINITY;
    }

    /**
     * Returns a shortest path from the source vertex <tt>s</tt> to vertex <tt>v</tt>.
     *
     * @param  v the destination vertex
     * @return a shortest path from the source vertex <tt>s</tt> to vertex <tt>v</tt>
     *         as an iterable of edges, and <tt>null</tt> if no such path
     */
    public Iterable<DirectedEdge> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<DirectedEdge> path = new Stack<DirectedEdge>();
        for (DirectedEdge e = edgeTo[v-1]; e != null; e = edgeTo[e.from()-1]) {
            path.push(e);
            //System.out.println(e);
        }
        return path;
    }


    // check optimality conditions:
    // (i) for all edges e:            distTo[e.to()] <= distTo[e.from()] + e.weight()
    // (ii) for all edge e on the SPT: distTo[e.to()] == distTo[e.from()] + e.weight()
    private boolean check(Graph G, int s) {

        // check that edge weights are nonnegative
        for (DirectedEdge e : G.edgesB()) {
            if(type == 1){
                if (e.distance() < 0) {
                    System.err.println("negative edge distance detected");
                    return false;
                }
            }
            else if(type == 2){
                if (e.price() < 0) {
                    System.err.println("negative edge price detected");
                    return false;
                }
            }
        }

        // check that distTo[v] and edgeTo[v] are consistent
        if (distTo[s-1] != 0.0 || edgeTo[s-1] != null) {
            System.err.println("distTo[s] and edgeTo[s] inconsistent");
            return false;
        }
        for (int v = 0; v < G.V(); v++) {
            if (v == s) continue;
            if (edgeTo[v-1] == null && distTo[v-1] != Double.POSITIVE_INFINITY) {
                System.err.println("distTo[] and edgeTo[] inconsistent");
                return false;
            }
        }

        // check that all edges e = v->w satisfy distTo[w] <= distTo[v] + e.weight()
        for (int v = 0; v < G.V(); v++) {
            for (DirectedEdge e : G.adjB(v)) {
                int w = e.to();
                if(type == 1){
                    if (distTo[v-1] + e.distance() < distTo[w-1]) {
                        System.err.println("edge " + e + " not relaxed");
                        return false;
                    }
                }
                else if(type == 2){
                    if (distTo[v-1] + e.price() < distTo[w-1]) {
                        System.err.println("edge " + e + " not relaxed");
                        return false;
                    }
                }
                else{
                    if (distTo[v-1] + 1 < distTo[w-1]) {
                        System.err.println("edge " + e + " not relaxed");
                        return false;
                    }
                }
            }
        }

        // check that all edges e = v->w on SPT satisfy distTo[w] == distTo[v] + e.weight()
        for (int w = 0; w < G.V(); w++) {
            if (edgeTo[w-1] == null) continue;
            DirectedEdge e = edgeTo[w-1];
            int v = e.from();
            if (w != e.to()) return false;
            if(type == 1){
                if (distTo[v-1] + e.distance() != distTo[w-1]) {
                    System.err.println("edge " + e + " on shortest path not tight");
                    return false;
                }
            }
            else if(type == 2){
                if (distTo[v-1] + e.price() != distTo[w-1]) {
                    System.err.println("edge " + e + " on shortest path not tight");
                    return false;
                }
            }
            else{
                if (distTo[v-1] + 1 != distTo[w-1]) {
                    System.err.println("edge " + e + " on shortest path not tight");
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * Unit tests the <tt>DijkstraSP</tt> data type.
     */
    /*public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);
        int s = Integer.parseInt(args[1]);

        // compute shortest paths
        DijkstraSP sp = new DijkstraSP(G, s);


        // print shortest path
        for (int t = 0; t < G.V(); t++) {
            if (sp.hasPathTo(t)) {
                StdOut.printf("%d to %d (%.2f)  ", s, t, sp.distTo(t));
                for (DirectedEdge e : sp.pathTo(t)) {
                    StdOut.print(e + "   ");
                }
                StdOut.println();
            }
            else {
                StdOut.printf("%d to %d         no path\n", s, t);
            }
        }
    }*/

}