import java.io.*;
import java.util.*;
import java.io.BufferedReader;

public class allTrips {
    public static String[][] searchAllTrips(int searchColumnIndex, String searchString) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader("inputs/stop_times.txt"));
        String line;

        ArrayList<Trip> tripsFound = new ArrayList<>();
        while ( (line = br.readLine()) != null ) {                          //searches stop_times.txt with arrival time given by user and returns
            String[] values = line.split(",");                        // the full line where a matching arrival time is found
            if(values[searchColumnIndex].equals(searchString)) {
                String foundTrip = line;
                String[] trip = foundTrip.split(",");
                tripsFound.add(new Trip(trip));
            }
        }
        Collections.sort(tripsFound, new SortByID());
        String[][] arrayForEmmet = new String[tripsFound.size()][];
        for(int i = 0; i<tripsFound.size(); i++){
                arrayForEmmet[i] = tripsFound.get(i).toArray();
        }
        br.close();

        return arrayForEmmet;
    }
  }
