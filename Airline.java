/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Airline;

import java.util.*;
import java.io.*;


/**
 *
 * @author dgtc1_000
 */
public class Airline {
    public static void main(String[] args) throws IOException{
        Scanner s = new Scanner(System.in); 
        Scanner inFile = null;
        String filename, c, src, dst;
        File in;
        int numC, v, w;
        double d, p;
        char menu1, menu2;
        boolean done = false;
        
        System.out.print("INPUT FILE:\t");
        filename = s.next();
        
        in = new File(filename);
        if(!in.exists()){
            System.out.print("The \""+filename+"\" file could not be found.");
            System.exit(0);
        }
        try{
            inFile = new Scanner(in);
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        
        numC = inFile.nextInt();
        Graph airline = new Graph(numC);
        
        for(int i = 0; i < numC; i++){
            c = inFile.next();
            airline.addVertex(c, i);
        }
        while(inFile.hasNext()){
            v = inFile.nextInt();
                //System.out.println(v);
            w = inFile.nextInt();
                //System.out.println(w);
            d = inFile.nextDouble();
                //System.out.println(d);
            p = inFile.nextDouble();
                //System.out.println(p);
            DirectedEdge edge = new DirectedEdge(v, w, d, p);
            //System.out.print(edge.toString());
            airline.addEdge(edge);
        }
        
        //build map
        while(!done){
            System.out.print("\nSelect one of the following:"+
                    "\n\t1) Print all direct routes with distances and prices"+
                    "\n\t2) Show minimum spanning tree for the service routes based on distance"+
                    "\n\t3) Shortest path search"+
                    "\n\t4) Print all trip options below user specified price"+
                    "\n\t5) Add a route to the schedule"+
                    "\n\t6) Remove a route from the schedule"+
                    "\n\t7) Quit"+
                    "\nSelection:\t");
            menu1 = s.next().charAt(0);
            if(menu1 == '1'){
                airline.printAllRoutes();
                done = false;
            }
            else if(menu1 == '2'){
                airline.minSpanningTree();
                done = false;
            }
            else if(menu1 == '3'){
                System.out.print("\nSearch Shortest path based on:"+
                    "\n\t1) Total miles from source to destination"+
                    "\n\t2) Total price from source to destination"+
                    "\n\t3) Least number of hops from source to destination"+
                    "\n\t4) Nevermind, I don't want to do this"+
                    "\nSelection:\t");
                menu2 = s.next().charAt(0);
                
                while(menu2 < '1' || menu2 > '4'){
                    System.out.println("You must select 1, 2, 3, or 4");
                    System.out.print("\nSearch Shortest path based on:"+
                        "\n\t1) Total miles from source to destination"+
                        "\n\t2) Total price from source to destination"+
                        "\n\t3) Least number of hops from source to destination"+
                        "\n\t4) Nevermind, I don't want to do this"+
                        "\nSelection:\t");
                    menu2 = s.next().charAt(0);
                }
                
                System.out.print("Enter the source location:\t");
                src = s.next();
                System.out.print("Enter the destination location:\t"); 
                dst = s.next();
                
                if(menu2 == '1'){
                    airline.shortestPathByDist(src, dst);
                }
                else if(menu2 == '2'){
                    airline.shortestPathByPrice(src, dst);
                }
                else if(menu2 == '3'){
                    airline.shortestPathByHops(src, dst);
                }
                
                done = false;
            }
/**/        else if(menu1 == '4'){
                System.out.print("\nWhat is your maximum price:\t");
                p = s.nextDouble();
                while(p<0){
                    System.out.print("Price cannot be negative, try again:\t");
                    p = s.nextDouble();
                }
                
                airline.budgetTrips(p);
                
                done = false;
            }
            else if(menu1 == '5'){
                System.out.print("What is the source city for the route you wish to add:\t");
                src = s.next();
                int checkSRC = airline.checkCity(src);
                while(checkSRC == -1){
                    System.out.print("The city you have entered is not serviced by this airline, please try again:\t");
                    src = s.next();
                    checkSRC = airline.checkCity(src);
                }
                
                System.out.print("What is the destination city for the route you wish to add:\t");
                dst = s.next();
                int checkDST = airline.checkCity(dst);
                while(checkDST == -1){
                    System.out.print("The city you have entered is not serviced by this airline, please try again:\t");
                    dst = s.next();
                    checkDST = airline.checkCity(dst);
                }
                
                System.out.print("Distance (miles):\t");
                d = s.nextDouble();
                while(d<0){
                    System.out.print("Distance cannot be negative, try again:\t");
                    d = s.nextDouble();
                }
                
                System.out.print("Price (American $):\t");
                p = s.nextDouble();
                while(d<0){
                    System.out.print("Price cannot be negative, try again:\t");
                    p = s.nextDouble();
                }
                
                airline.addEdge(new DirectedEdge(checkSRC+1, checkDST+1, d, p));
                
                done = false;
            }
            else if(menu1 == '6'){
                System.out.print("What is the source city for the route you wish to remove:\t");
                src = s.next();
                int checkSRC = airline.checkCity(src);
                while(checkSRC == -1){
                    System.out.print("The city you have entered is not serviced by this airline, please try again:\t");
                    src = s.next();
                    checkSRC = airline.checkCity(src);
                }
                
                System.out.print("What is the destination city for the route you wish to remove:\t");
                dst = s.next();
                int checkDST = airline.checkCity(dst);
                while(checkDST == -1){
                    System.out.print("The city you have entered is not serviced by this airline, please try again:\t");
                    dst = s.next();
                    checkDST = airline.checkCity(dst);
                }
                
                airline.removeEdge(checkSRC+1, checkDST+1);
                
                done = false;
            }
            else if(menu1 == '7'){
                FileWriter fw = new FileWriter(in.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                
                bw.write(airline.V()+"\n");
                airline.writeCities(bw);
                airline.writeEdges(bw);
                bw.close();
                
                done = true;
            }
            else{
                System.out.println("\nThat was an invalid entry, try again");
                done = false;
            }
        
        }
        
    }
}
