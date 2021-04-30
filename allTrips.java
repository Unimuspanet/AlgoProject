import java.io.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.BufferedReader;

public class allTrips {
    public static String[][] searchAllTrips(int searchColumnIndex, String searchString) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader("inputs/stop_times.txt"));
        String line;

        ArrayList<String[]> tripsFound = new ArrayList<>();
        while ( (line = br.readLine()) != null ) {
            String[] values = line.split(",");
            if(values[searchColumnIndex].equals(searchString)) {
                String foundTrip = line;
                String[] trip = foundTrip.split(",");
                tripsFound.add(trip);


            }
        }
        String[][] arrayForEmmet = new String[tripsFound.size()][8];
        int count = 0;
        for(int i = 0; i<tripsFound.size(); i++){
            String[] arrayToTransfer = tripsFound.get(i);
            for(int j = 0; j<8; j++){
                arrayForEmmet[count][j] = arrayToTransfer[j];
            }
            count++;
        }
        br.close();
        String[] headers = {"Trip Id","Arrival Time","Departure time","Stop id","Stop Sequence","Stop Headsign","Pickup Type","Drop off Type","Shape Dist Traveled"};
        return arrayForEmmet;

    }


}
