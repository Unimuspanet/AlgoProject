import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

/*
 *Ternary search tree (TST), takes in file of stops 
 *(moves keywords to provide meaningful searh functionality)
 *returns the full stop information for each stop matching 
 *input by returning an arraylist.
 */
public class TST {

    /*
     * create Node class to have a way to store 
     * bus stop info and location in the tree 
     */
    private class Node {
        private char key;           
        private int value;
        private Node right, left, middle; 

        public Node(char key, int value) {
            this.key = key;
            this.value = value;
            this.right = null;
            this.left = null;
            this.middle = null;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    private Node root;
    private boolean is_complete;
    private boolean is_word_match;
    

    /*
     * add functions allow for the stop to be correctly
     * placed within the tree
     */
    private void add (char[] stop_name) {

        if (stop_name.length != 0){
            if (root == null)
                root = new Node( stop_name[0], -1 );
            is_complete = false;
            add( stop_name, 0, root );    
        }    
    }

    private Node add (char[] stop_name, int i, Node node) {
        
        if (node == null) 
            node = new Node( stop_name[i], -1 );

        if (node.key < stop_name[i])
            node.left = add( stop_name, i, node.left );

        else if (node.key > stop_name[i])
            node.right = add( stop_name, i, node.right );

        else if (i < stop_name.length - 1)
            node.middle = add( stop_name, ++i, node.middle );

        if (i == stop_name.length - 1 && !is_complete){
            node.setValue(i);
            is_complete = true;
        }
        
        return node;
    }

    /*
     * printArrayList allows for easy access to find the stops 
     * that have matched the input
     */
    public static void printArrayList(ArrayList<String> array) {

        for (int i = 0; i < array.size(); i++)
            System.out.println(array.get(i));

    }
    
    /*
     * search functions first look for matching words, and
     * then look for the corresponding nodes to place in 
     * an ArrayList that stores all possible matches
     */
    public ArrayList<String> search (String input) {

        is_word_match = false;
        ArrayList<String> search_match = new ArrayList<String>();
        Node origin = search(input.toCharArray(), 0, root);

        if (is_word_match){
            //System.out.println("Exact match.");
            search_match.add(input);
        }

        if (origin != null){
            match("", origin.middle, search_match);
            //System.out.println( "Possible matches: " + search_match.size());
            for (int i = (is_word_match?1:0); i < search_match.size(); i++) {

                search_match.set( i, input + search_match.get(i) );
            }
        }

        //else
            //System.out.println("No matches." );

        return search_match;
    }

    private Node search (char[] stop_name, int i, Node node) {

        if (node != null){
            if (node.key < stop_name[i])
                node = search( stop_name, i, node.left );

            else if (node.key > stop_name[i])
                node = search( stop_name, i, node.right );

            else if (i < stop_name.length - 1)
                node = search(stop_name, ++i, node.middle );

            if (node != null && i == stop_name.length - 1 && node.value != -1)
                is_word_match = true;

            return node;
        }
        return null;

    }

    private void match(String first_word, Node node, ArrayList<String> matches) {

        if (node != null){

            match(first_word, node.left, matches );
            match(first_word + node.key, node.middle, matches );
            match(first_word, node.right, matches );

            if (node.value != -1){

               first_word += node.key;
                matches.add(first_word);
            }
        }
    }

    /*
     * start out by creating the TST tree with the stop file
     * and then access functions easily. Allows for the TST
     * only have to be made once--save space&time
     */
    TST(String file_name) {
        
        root = null;
        File file = new File(file_name);
        try {

            Scanner scanner = new Scanner(file);
            String currentLine = "";

            while (scanner.hasNextLine() ){

                currentLine = scanner.nextLine();
                Scanner tokenizer = new Scanner(currentLine);
                tokenizer.useDelimiter( "," );
    
                //skip over stop_id & stop_code
                tokenizer.next();
                tokenizer.next();
                
                String stop_name = tokenizer.next();
                String first_word = stop_name.substring(0,2); // no whitespace before stops

                if (first_word.equals("EB") || first_word.equals("NB") 
                    || first_word.equals("SB") || first_word.equals("WB") ){

                    stop_name = stop_name.substring(3).concat(" " +first_word);

                }
                else if(stop_name.substring(0,8).equals("FLAGSTOP")){
                    stop_name = stop_name.substring(9).concat(" " + "FLAGSTOP");
                }

                add(stop_name.toCharArray());
                tokenizer.close();
            }
    
            scanner.close();
        } 

        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }



}