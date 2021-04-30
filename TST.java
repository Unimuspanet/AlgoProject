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
        private StopDetails stop_details;

        public Node(char key, int value) {
            this.key = key;
            this.value = value;
            this.right = null;
            this.left = null;
            this.middle = null;
            this.stop_details = null;
        }

        public void setValue(int value) {
            this.value = value;
        }
        public void setStopDetails(StopDetails info) {
            this.stop_details = info;
        }
    }
    
    protected class StopDetails {
        protected String[] details;

        protected StopDetails( String[] deliminated_info ) {
            details = deliminated_info;
        }             
    }

    private Node root;
    private boolean is_complete;
    private boolean is_word_match;
    

    /*
     * add functions allow for the stop to be correctly
     * placed within the tree
     */    
    private void add (char[] stop_name, StopDetails info) {

         if (stop_name.length != 0){
             if (root == null)
                 root = new Node( stop_name[0], -1);
             is_complete = false;
             add(stop_name, 0, root,info);    
         }    
     }

     private Node add (char[] stop_name, int i, Node node,  StopDetails info) {
         
         if (node == null) 
             node = new Node( stop_name[i], -1);

         if (node.key < stop_name[i])
             node.left = add( stop_name, i, node.left,info);

         else if (node.key > stop_name[i])
             node.right = add( stop_name, i, node.right,info);

         else if (i < stop_name.length - 1)
             node.middle = add( stop_name, ++i, node.middle ,info);

         if (i == stop_name.length - 1 && !is_complete){
             node.setValue(i);
             node.setStopDetails(info);
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
    
    
    
    public String getStopId(String input) {
        Node matching_string = search(input.toCharArray(), 0, root);
        if(is_word_match) {
        	StopDetails stop = matching_string.stop_details;
        	return stop.details[0];
        }
        
        return null;
    }
    

    public ArrayList<String[]> searchForDetails (String input){

        is_word_match = false;
        ArrayList<String> search_name_matches = new ArrayList<String>();
        ArrayList<String[]> search_detail_matches = new ArrayList<String[]>();
        Node origin = search(input.toCharArray(), 0, root);

        if (is_word_match)
            search_name_matches.add(input);

        if (origin != null){
            matchst("", origin.middle, search_name_matches, search_detail_matches);
            
        }

        return search_detail_matches;
    
    }
    
    public ArrayList<String> searchForNameWithId (String input){

        is_word_match = false;
        ArrayList<String> search_name_matches = new ArrayList<String>();
        ArrayList<String[]> search_detail_matches = new ArrayList<String[]>();
        Node origin = search(input.toCharArray(), 0, root);

        if (is_word_match)
            search_name_matches.add(input);

        if (origin != null){
            matchst("", origin.middle, search_name_matches, search_detail_matches);
            
            for (int i = (is_word_match?1:0); i < search_name_matches.size(); i++) {
                String[]current_details = search_detail_matches.get(i);
                search_name_matches.set( i, current_details[0] + "," + input + search_name_matches.get(i));
            }
        }

        return search_name_matches;
    
    }
    
    public static void main(String[] args) {
        
    }
    /*
     * search functions first look for matching words, and
     * then look for the corresponding nodes to place in 
     * an ArrayList that stores all possible matches
     */
    public ArrayList<String> searchForNames (String input) {

        is_word_match = false;
        ArrayList<String> search_match = new ArrayList<String>();
        Node origin = search(input.toCharArray(), 0, root);

        if (is_word_match)
            search_match.add(input);

        if (origin != null){
            match("", origin.middle, search_match);
            
            for (int i = (is_word_match?1:0); i < search_match.size(); i++)
                search_match.set( i, input + search_match.get(i) );
        }

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

            if ((node != null) && (i == stop_name.length - 1) && (node.value != -1))
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
    
    private void matchst(String first_word, Node node, ArrayList<String> matches, ArrayList<String[]> details) {

         if (node != null)
         {
                 matchst( first_word, node.left, matches, details);
                 matchst( first_word+node.key, node.middle, matches, details);
                 matchst( first_word,node.right, matches, details);

                 if ( node.value != -1 )
                 {
                     first_word += node.key;
                     matches.add(first_word);
                     details.add(node.stop_details.details);
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
            String current_line = "";
            String stop_name, first_word;

            while (scanner.hasNextLine() ){

                current_line = scanner.nextLine();
                Scanner tokenizer = new Scanner(current_line);
                tokenizer.useDelimiter( "," );
    
                String[] stop_details = new String[10];
                for (int i = 0; i < 10; i++){
                    if (!tokenizer.hasNext())
                        break;
                    stop_details[i] = tokenizer.next();
                }
                
                //skip over stop_id & stop_code
                stop_name = stop_details[2];
                first_word = stop_name.substring(0,2); // no whitespace before stops

                if (first_word.equals("EB") || first_word.equals("NB") 
                    || first_word.equals("SB") || first_word.equals("WB") ){

                    stop_name = stop_name.substring(3).concat(" " + first_word);
                }
                
                else if(stop_name.substring(0,8).equals("FLAGSTOP")){
                    stop_name = stop_name.substring(9).concat(" FLAGSTOP");
                }

                StopDetails newInfo = new StopDetails(stop_details);
                add(stop_name.toCharArray(), newInfo);
                tokenizer.close();
            }
    
            scanner.close();
        } 

        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }



}