package avengers;

import java.util.*;

/**
 * Given a starting event and an Adjacency Matrix representing a graph of all possible 
 * events once Thanos arrives on Titan, determine the total possible number of timelines 
 * that could occur AND the number of timelines with a total Expected Utility (EU) at 
 * least the threshold value.
 * 
 * 
 * Steps to implement this class main method:
 * 
 * Step 1:
 * UseTimeStoneInputFile name is passed through the command line as args[0]
 * Read from UseTimeStoneInputFile with the format:
 *    1. t (int): expected utility (EU) threshold
 *    2. v (int): number of events (vertices in the graph)
 *    3. v lines, each with 2 values: (int) event number and (int) EU value
 *    4. v lines, each with v (int) edges: 1 means there is a direct edge between two vertices, 0 no edge
 * 
 * Note 1: the last v lines of the UseTimeStoneInputFile is an ajacency matrix for a directed
 * graph. 
 * The rows represent the "from" vertex and the columns represent the "to" vertex.
 * 
 * The matrix below has only two edges: (1) from vertex 1 to vertex 3 and, (2) from vertex 2 to vertex 0
 * 0 0 0 0
 * 0 0 0 1
 * 1 0 0 0
 * 0 0 0 0
 * 
 * Step 2:
 * UseTimeStoneOutputFile name is passed through the command line as args[1]
 * Assume the starting event is vertex 0 (zero)
 * Compute all the possible timelines, output this number to the output file.
 * Compute all the posssible timelines with Expected Utility higher than the EU threshold,
 * output this number to the output file.
 * 
 * Note 2: output these number the in above order, one per line.
 * 
 * Note 3: use the StdIn/StdOut libraries to read/write from/to file.
 * 
 *   To read from a file use StdIn:
 *     StdIn.setFile(inputfilename);
 *     StdIn.readInt();
 *     StdIn.readDouble();
 * 
 *   To write to a file use StdOut:
 *     StdOut.setFile(outputfilename);
 *     //Call StdOut.print() for total number of timelines
 *     //Call StdOut.print() for number of timelines with EU >= threshold EU 
 * 
 * Compiling and executing:
 *    1. Make sure you are in the ../InfinityWar directory
 *    2. javac -d bin src/avengers/*.java
 *    3. java -cp bin avengers/UseTimeStone usetimestone.in usetimestone.out
 * 
 * @author Yashas Ravi
 * 
 */

public class UseTimeStone {

        private static void dfs(HashMap<Integer, ArrayList<Integer>> G, int vertex, int sum, ArrayList<Integer> arrayList, int[] array){
            sum += array[vertex];
            arrayList.add(sum);
            for (int w : G.get(vertex)){
                dfs(G, w, sum, arrayList, array);
            }
        }
    




    public static void main (String [] args) {
    	
        if ( args.length < 2 ) {
            StdOut.println("Execute: java UseTimeStone <INput file> <OUTput file>");
            return;
        }
        StdIn.setFile(args[0]);
        StdOut.setFile(args[1]);
    	// WRITE YOUR CODE HERE
        int EU = StdIn.readInt();
        int nodes = StdIn.readInt();

        HashMap<Integer, ArrayList<Integer>> hash = new HashMap<Integer, ArrayList<Integer>>();
        int[] events = new int[nodes];
        for(int i = 0; i < nodes; i++){
            StdIn.readInt();
            events[i] = StdIn.readInt();
        }
        for(int i = 0; i < nodes; i++){
            ArrayList<Integer> mapFill = new ArrayList<Integer>();
            hash.put(i, mapFill);
        }
        for(int i = 0; i < nodes; i++){
            for(int j = 0; j < nodes; j++){
                if(StdIn.readInt() == 1){
                    hash.get(i).add(j);
                }
            }   
        }
        ArrayList<Integer> list = new ArrayList<Integer>();
        dfs(hash, 0, 0, list, events);
        int count = 0;
        for(int i = 0; i < list.size(); i++){
            if(list.get(i) >= EU){
                count++;
            }
        }
        StdOut.println(list.size());
        StdOut.println(count);
    }
}

