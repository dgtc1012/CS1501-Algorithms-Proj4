/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Airline;
import java.util.*;
import java.io.*;

public class AllPaths<Vertex> {

    private Stack<Integer> path  = new Stack<Integer>();   // the current path
    private List<Integer> onPath  = new ArrayList<Integer>();     // the set of vertices on the path

    public AllPaths(Graph G, int s, int t) {
        enumerate(G, s, t);
    }

    // use DFS
    private void enumerate(Graph G, int v, int t) {

        // add node v to current path from s
        path.push(v);
        onPath.add(v);

        // found path from s to t - currently prints in reverse order because of stack
        if (v == t) 
            System.out.println(path);

        // consider all neighbors that would continue path with repeating a node
        else {
            for (DirectedEdge w : G.adjB(v)) {
                System.out.println(w);
                if (!onPath.contains(w.to())){ 
                    System.out.println(w);
                    enumerate(G, w.to(), t);
                }
            }
        }

        // done exploring from v, so remove from path
        path.pop();
        onPath.remove(v);
    }
/*
    public static void main(String[] args) {
        Graph G = new Graph();
        G.addEdge("A", "B");
        G.addEdge("A", "C");
        G.addEdge("C", "D");
        G.addEdge("D", "E");
        G.addEdge("C", "F");
        G.addEdge("B", "F");
        G.addEdge("F", "D");
        G.addEdge("D", "G");
        G.addEdge("E", "G");
        StdOut.println(G);
        new AllPaths(G, "A", "G");
        StdOut.println();
        new AllPaths(G, "B", "F");
    }
*/
}