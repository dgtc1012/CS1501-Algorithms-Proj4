package Airline;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dgtc1_000
 */
import java.util.*;
import java.io.*;

public class Graph {
    private int V;
    private int E;
    private String[] city;
    private ArrayList<DirectedEdge>[] adj;
    private ArrayList<DirectedEdge>[] adjBack;
    private ArrayList<Edge>[] adjMST;
    private int[] indegree;
    
    public Graph(int size){
        
        if (size < 0) throw new IllegalArgumentException("Number of vertices in a Digraph must be nonnegative");
        V = size;
        E = 0;
        city = new String[size];
        adj = (ArrayList<DirectedEdge>[]) new ArrayList[size];
        adjBack = (ArrayList<DirectedEdge>[]) new ArrayList[size];
        adjMST = (ArrayList<Edge>[]) new ArrayList[size];
        for(int i = 0; i < size; i++){
            adj[i] = new ArrayList<DirectedEdge>();
            adjBack[i] = new ArrayList<DirectedEdge>();
            adjMST[i] = new ArrayList<Edge>();
        }
        indegree = new int[V];
    }
    
    public void addVertex(String c, int index){
        city[index]=c;
    }
    
    public int V(){
        return V;
    }
    
    public int E(){
        return E;
    }
    
    private void validateVertex(int myV){
        if(myV < 0 || myV >= V){
            throw new IndexOutOfBoundsException("vertex "+myV+" is not between 0 and "+(V-1));
        }
    }
    
    private boolean exists(DirectedEdge myE){
        for(int i = 0; i<V; i++){
            for(DirectedEdge e : adjBack[i]){
                if(myE.from() == e.from() && myE.to() == e.to() && myE.distance() == e.distance() && myE.price() == e.price()){
                    return true;
                }
            }
        }
        return false;
    }
    
    public void addEdge(DirectedEdge e){
        int v = e.from();
        int w = e.to();
        
        if(!this.exists(e)){
            validateVertex(v-1);
            validateVertex(w-1);
            adj[v-1].add(e);
            adjBack[v-1].add(e);
            adjBack[w-1].add(new DirectedEdge(e.to(), e.from(), e.distance(), e.price()));
            E++;
            indegree[w-1]++;
            addEdgeMST(new Edge(v, w, e.distance()));
        }
        else{
            System.out.println("This flight already exists.");
        }
        
    }
    
    private void addEdgeMST(Edge e) {
        int v = e.either();
        int w = e.other(v);
        validateVertex(v-1);
        validateVertex(w-1);
        adjMST[v-1].add(e);
        adjMST[w-1].add(e);
    }
    
    public void removeEdge(int v, int w){
        DirectedEdge r = null;
        for(DirectedEdge e : adjBack[v-1]){
            if(e.from() == v && e.to() == w){
                r = e;
            }
        }
        if(r != null){
            adj[v-1].remove(r);
            adjBack[v-1].remove(r);
        }
        for(DirectedEdge e : adjBack[w-1]){
            if(e.from() == v && e.to() == w){
                r = e;
            }
        }
        if(r != null){
            adjBack[w-1].remove(r);
        }
        Edge r2 = null;
        for(Edge e : adjMST[v-1]){
            if(e.either() == v && e.other(e.either()) == w){
                r2 = e;
            }
        }
        if(r2 != null){
            adjMST[v-1].remove(r2);
        }
        for(Edge e : adjMST[w-1]){
            if(e.either() == v && e.other(e.either()) == w){
                r2 = e;
            }
        }
        if(r2 != null){
            adjMST[w-1].remove(r2);
        }
    }
    
    public int outdegree(int v){
        validateVertex(v);
        return adj[v].size();
    }
    
    public int indegree(int v){
        validateVertex(v);
        return indegree[v];
    }
    
    public Iterable<DirectedEdge> adj(int v) {
        validateVertex(v);
        return adj[v];
    }
    
    public Iterable<DirectedEdge> adjB(int v) {
        validateVertex(v);
        return adjBack[v];
    }
    
    public Iterable<Edge> adjMST(int v) {
        validateVertex(v);
        return adjMST[v];
    }
    
    public Iterable<DirectedEdge> edges() {
        ArrayList<DirectedEdge> list = new ArrayList<DirectedEdge>();
        for (int v = 0; v < V; v++) {
            for (DirectedEdge e : adj(v)) {
                list.add(e);
            }
        }
        return list;
    } 

    public Iterable<DirectedEdge> edgesB() {
        ArrayList<DirectedEdge> list = new ArrayList<DirectedEdge>();
        for (int v = 0; v < V; v++) {
            for (DirectedEdge e : adjB(v)) {
                list.add(e);
            }
        }
        return list;
    } 
    
    public Iterable<Edge> edgesMST() {
        ArrayList<Edge> list = new ArrayList<Edge>();
        for (int v = 0; v < V; v++) {
            for (Edge e : adjMST(v)) {
                list.add(e);
            }
        }
        return list;
    }
    
    public void printAllRoutes(){
        for(int i = 0; i<V; i++){
            for(DirectedEdge e : adj[i]){
                System.out.println(city[e.from()-1]+" -> " + city[e.to()-1] + " " + e.distance() + " miles @ $"+e.price());
            }
        }
    }
    
    public void minSpanningTree(){
        PrimMST mst = new PrimMST(this);
        ArrayList<Edge> printMST = (ArrayList<Edge>)mst.edges();
        for(Edge e : printMST){
            System.out.println(city[e.either()-1] + "->" + city[e.other(e.either())-1] + ": " + e.weight());
        }
    }  
    
    public int checkCity(String c){
        int check = -1;
        for(int i = 0; i<city.length; i++){
            if(city[i].equalsIgnoreCase(c)){
                check = i;
            }
        }
        return check;
    }
    
    /*public int checkDST(String dst){
        int check = -1;
        for(int i = 0; i<city.length; i++){
            if(city[i].equalsIgnoreCase(dst)){
                check = i;
            }
        }
        return check;
    }*/
    
    public void shortestPathByDist(String src, String dst){
        int a = checkCity(src);
        int b = checkCity(dst);
        if(a == -1 || b ==-1){
            System.out.println("Specified source and/or Destination are/is not serviced by this airline, sorry!");
        }
        else{
            //System.out.println((a+1)+" "+(b+1));
            DijkstraSP2 dsp = new DijkstraSP2(this, a+1, 1);
            Stack<DirectedEdge> al = (Stack<DirectedEdge>)dsp.pathTo(b+1);
            //System.out.println(dsp.hasPathTo(b+1));
            //System.out.println(al.get(1).either());
            //System.out.println(al.get());
            System.out.print(city[al.get(0).to()-1]);
            for(DirectedEdge e : al){
                System.out.print(" " + e.distance()+" "+city[e.from()-1]);
            }
        }
    }
    
    public void shortestPathByPrice(String src, String dst){
        int a = checkCity(src);
        int b = checkCity(dst);
        if(a == -1 || b ==-1){
            System.out.println("Specified source and/or Destination are/is not serviced by this airline, sorry!");
        }
        else{
            //System.out.println((a+1)+" "+(b+1));
            DijkstraSP2 dsp = new DijkstraSP2(this, a+1, 2);
            Stack<DirectedEdge> al = (Stack<DirectedEdge>)dsp.pathTo(b+1);
            //System.out.println(dsp.hasPathTo(b+1));
            //System.out.println(al.get(1).either());
            //System.out.println(al.get());
            System.out.print(city[al.get(0).to()-1]);
            for(DirectedEdge e : al){
                System.out.print(" " + e.price()+" "+city[e.from()-1]);
            }
        }
    }
    
    public void shortestPathByHops(String src, String dst){
        int a = checkCity(src);
        int b = checkCity(dst);
        if(a == -1 || b ==-1){
            System.out.println("Specified source and/or Destination are/is not serviced by this airline, sorry!");
        }
        else{
            //System.out.println((a+1)+" "+(b+1));
            DijkstraSP2 dsp = new DijkstraSP2(this, a+1, 3);
            Stack<DirectedEdge> al = (Stack<DirectedEdge>)dsp.pathTo(b+1);
            //System.out.println(dsp.hasPathTo(b+1));
            //System.out.println(al.get(1).either());
            //System.out.println(al.get());
            System.out.print("\n"+city[al.get(0).to()-1]);
            for(DirectedEdge e : al){
                System.out.print(" "+city[e.from()-1]);
            }
            System.out.print("\n");
        }
    }
    
    public void writeCities(BufferedWriter bw) throws IOException{
        for(int i = 0; i<V; i++){
            bw.write(city[i]+"\n");
        }
    }
    
    public void writeEdges(BufferedWriter bw) throws IOException{
        for(int i = 0; i<V; i++){
            for(DirectedEdge e : adj[i]){
                bw.write(e + "\n");
            }
        }
    }
    
    public void budgetTrips(double p){
        for(int i = 1; i<=V; i++){
            for(int j = 1; j<=V; j++){
                //System.out.println(i+" "+j);
                enumerate(i, j, p, 0);
            }
        }
    }
    
    private Stack<Integer> path  = new Stack<Integer>();   // the current path
    private ArrayList<String> onPath  = new ArrayList<String>();     // the set of vertices on the path
    private ArrayList<DirectedEdge> onPathE  = new ArrayList<DirectedEdge>();     // the set of vertices on the path

   /* private void AllPaths(int s, int t, double p) {
        enumerate(s, t);
    }*/

    // use DFS
    private void enumerate(int v, int t, double p, double myP) {

        // add node v to current path from s
        path.push(v);
        //System.out.println(city[v-1]);
        onPath.add(city[v-1]);

        // found path from s to t - currently prints in reverse order because of stack
        if ((v == t) && (p >= myP) && !onPathE.isEmpty()){
            System.out.print("Total Price: "+ myP+", Route: "+city[onPathE.get(0).from()-1]);
            for(int i = 0; i<onPathE.size(); i++){
                System.out.print(" " + onPathE.get(i).price() + " " + city[onPathE.get(i).to()-1]);
            }
            System.out.print("\n");
        }
        // consider all neighbors that would continue path with repeating a node
        else {
            for (DirectedEdge w : this.adjB(v-1)) {
                //System.out.println(city[w.to()-1]);
                if (!onPath.contains(city[w.to()-1])){ 
                    //System.out.println(w);
                    onPathE.add(w);
                    enumerate(w.to(), t, p, myP+w.price());
                    onPathE.remove(w);
                }
            }
        }

        // done exploring from v, so remove from path
        path.pop();
        onPath.remove(city[v-1]);
    }
}
