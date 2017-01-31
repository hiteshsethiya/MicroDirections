package com.app.microd;

import com.app.microd.model.LatLng;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by racit-2105 on 25/01/17.
 */
public class CSVProcessor {

    public static final String CVS_SPLIT_BY = ",";

    public static void main(String[] args) {
        String filePath = "/Users/racit-2105/Desktop/lat_long.csv";
        File csvFile = new File(filePath);
        BufferedReader br = null;
        String line = "";

        List<LatLng> theLatLngs = new ArrayList<LatLng>();

        Double speed = 10.0;

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] latLong = line.split(CVS_SPLIT_BY);
                theLatLngs.add(
                        new LatLng(
                                Double.parseDouble(latLong[0]),
                                Double.parseDouble(latLong[1])));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        HashMap<Long,String> directionsMap = new HashMap<Long, String>();
        Long t = 0L;
        String lastAddedDirection = "";
        for(int i = 0; i + 1 < theLatLngs.size(); ++i) {
            LatLng sta = theLatLngs.get(i);
            LatLng end = theLatLngs.get(i + 1);
            Double distance = sta.distance(end);
            String direction = getDirectionString(sta,end,distance);
            if(!lastAddedDirection.equalsIgnoreCase(direction)) {
                directionsMap.put(t, direction);
                lastAddedDirection = getDirectionString(sta,end,distance);
            }
            t += Long.parseLong( String.format( "%.0f", distance / speed) ) ;
        }


        Long timeToReachDestination = Long.parseLong( String.format( "%.0f",theLatLngs.get(0).distance(theLatLngs.get(theLatLngs.size() - 1)) / speed));
        for(Long i = 0L; i < 2 * timeToReachDestination; ++i) {
            if(directionsMap.containsKey(i)) {
                System.out.println(directionsMap.get(i));
            }
        }
    }

    public static String getDirectionString(LatLng start, LatLng end, Double distance) {
        HaversineUtil.Direction direction = HaversineUtil.getTurn(start, end);
        if(direction == null) {
            return "Error";
        }
        String directionString;
        if(direction == HaversineUtil.Direction.Straight) {
            directionString = "Go straight for " + Math.round(distance) + " meters";
        } else if(direction == HaversineUtil.Direction.Slight_left) {
            directionString = "Take a slight left";
        } else if(direction == HaversineUtil.Direction.Slight_right) {
            directionString = "Take a slight right";
        } else {
            directionString = "Take a " + direction.name() + " turn";
        }
        return directionString;
    }
}
