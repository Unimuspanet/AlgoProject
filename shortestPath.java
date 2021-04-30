package uiDesign;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
//import java.util.Collections;
import java.util.Scanner;

public class shortestPath {
    private int V;
    private double[][] graph;
    private int[][] paths;
    private ArrayList<Integer> conversions; //translation between stop index in graph and stop id
    
    /**
     * @throws IOException 
     * @throws ClassNotFoundException 
     */
    shortestPath() throws IOException, ClassNotFoundException{
    	File stops, stopTimes, transfers;
    	stops = new File("stops.txt");
    	stopTimes = new File("stop_times.txt");
    	transfers = new File("transfers.txt");
    	
    	//-1s here, as all input files have a header
    	this.V = numberOfLines(stops) - 1;
    	int transferEdges = numberOfLines(transfers) - 1;

    	this.graph = new double[V][V];
    	
    	conversions = new ArrayList<Integer>(V);
    	System.out.println(V + " Stops & " + (transferEdges) + " Transfers");
    	
    	// Set all values in graph to Integer.MAX_VALUE, so that Floyd Warshall will work
    	for(int i = 0; i < V; i++) {
    		for(int j = 0; j < V; j++) {
    			if(i == j) { graph[i][j] = 0; }
    			else { graph[i][j] = Integer.MAX_VALUE; }
    		}
    	}
    	
    	// Generating translation key from stop id to index in graph
    	Scanner stopScanner;
    	try {
    		stopScanner = new Scanner(stops);
    	} catch (FileNotFoundException e) {
    		e.printStackTrace();
    		return;
    	}
    	stopScanner.useDelimiter(",");
    	stopScanner.nextLine(); // dump headers
    	for(int i = 0; i < V; i++) {
    		int stopID = (int) stopScanner.nextDouble();
    		conversions.add(stopID);
    		stopScanner.nextLine();
    	}
    	
    	stopScanner.close();
    	
    	//TODO: assign Stops & Edges to the graph
    	// stop numbers will have to be assigned to integers, and reconverted at the end
    	
    	Scanner tranScanner;
		try {
			tranScanner = new Scanner(transfers);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
    	//Assigning costs of all edges defined in transfers.txt
		tranScanner.nextLine(); // dump headers
		tranScanner.useDelimiter(",");
    	for(int i = 0; i < transferEdges; i++) {
    		int from = conversions.indexOf(Integer.parseInt(tranScanner.next()));
    		int to = conversions.indexOf(Integer.parseInt(tranScanner.next()));
    		int transType = (int) tranScanner.nextDouble();
    		if(transType == 2) {
    			graph[from][to] = ((conversions.indexOf(Integer.parseInt(tranScanner.nextLine().replace(',', ' ').trim()))) / 100.0);
    		}
    		else if(transType == 0) {
    			graph[from][to] = 2;
        		tranScanner.nextLine();
    		}
    		else {
    			System.out.println("INVALID transfer_type: " + transType);
    			tranScanner.close();
    			return;
    		}
    	}
    	tranScanner.close();
    	Scanner timeScanner;
    	try {
    		timeScanner = new Scanner(stopTimes);
    	} catch (FileNotFoundException e) {
    		e.printStackTrace();
    		return;
    	}
    	//Assigning costs of all edges defined in stop_times.txt
		timeScanner.nextLine();
    	
    	String[] line1 = timeScanner.nextLine().split(",");
		String[] line2 = timeScanner.nextLine().split(",");
	
    	while(timeScanner.hasNextLine()) {
    		if(Integer.parseInt(line1[0]) == Integer.parseInt(line2[0])) {
    			int from = conversions.indexOf(Integer.parseInt(line1[3]));
    			int to = conversions.indexOf(Integer.parseInt(line2[3]));
    			graph[from][to] = 1;
    		}
    		line1 = line2;
    		line2 = timeScanner.nextLine().split(",");
    	}   	
    	timeScanner.close();
    	
    	this.paths = new int[V][V];
    	for(int i = 0; i < V; i++) {
    		for(int j = 0; j < V; j++) {
    			paths[i][j] = j;
    		}
    	}
    	File graphFile = new File("graph.txt");
    	File pathsFile = new File("paths.txt");
    	if (graphFile.exists() && pathsFile.exists())
    	{
    		System.out.println("retrieving graph files...");
    		ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("graph.txt"));
    		graph = (double[][])inputStream.readObject();
    		inputStream.close();
    		inputStream = new ObjectInputStream(new FileInputStream("paths.txt"));
    		paths = (int[][])inputStream.readObject();
    		inputStream.close();
    		System.out.println("graph files retrieved");
    	} else {
	    	System.out.println("Warshalling Floyds...");
	    	for (int k = 0; k < V; k++)
	    	{
	    		if(k % 100 == 0) {System.out.println(k + " / " + V); }
	        	for (int i = 0; i < V; i++)
	        	{
	        		for (int j = 0; j < V; j++)
	                {
	        			if (graph[i][k] + graph[k][j] < graph[i][j]) {
	        				graph[i][j] = graph[i][k] + graph[k][j]; // Cost(i to j) now Cost(i to k) + Cost(k to j) 
	        				paths[i][j] = k; //Path from (i to j), now goes (i to k to j)
	        			}
	                }
	            }
	        }
	    	System.out.println("Floyds Wharshalled!");
	    	
	    	System.out.println("generating files...");
	    	ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("graph.txt"));
	    	outputStream.writeObject(graph);
	    	outputStream.close();
	    	outputStream = new ObjectOutputStream(new FileOutputStream("paths.txt"));
	    	outputStream.writeObject(paths);
	    	outputStream.close();
	    	System.out.println("files generated");
    	}
    }

    /**
     * @return double: shortest path cost from stop1 to stop2
     */
    public ArrayList<Double> findPath(int stop1, int stop2){
    	ArrayList<Double> result = new ArrayList<Double>();
		ArrayList<Integer> path = new ArrayList<Integer>();
    	int vertex1 = conversions.indexOf(stop1);
    	int vertex2 = conversions.indexOf(stop2);
    	path = tracePath(stop1, stop2);    	
    	result.add(graph[vertex1][vertex2]);
    	for(int i = 0; i < path.size(); i++) {
    		result.add((double) path.get(i));
    	}
    	return result;
    }
    
    public ArrayList<Integer> tracePath(int stop1, int stop2) {
    	if(conversions.get(paths[conversions.indexOf(stop1)][conversions.indexOf(stop2)]) == stop2) {
    		ArrayList<Integer> path = new ArrayList<Integer>();
    		path.add(stop1);
    		path.add(stop2);
    		return path;
    	}
    	else {
    		ArrayList<Integer> path = tracePath(stop1, conversions.get(paths[conversions.indexOf(stop1)][conversions.indexOf(stop2)]));
    		path.add(stop2);
    		return path;
    	}
    }
    
    /** 
     * @param file: files to count the lines of
     * @return lines: number of lines in file
     * @throws IOException 
     */
    public int numberOfLines(File file) throws IOException {
    	BufferedReader reader = new BufferedReader(new FileReader(file));
    	int lines = 0;
    	while (reader.readLine() != null) lines++;
    	reader.close();
    	return lines;
    }

}
