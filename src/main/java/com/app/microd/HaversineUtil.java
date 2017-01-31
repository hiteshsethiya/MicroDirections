package com.app.microd;

import com.app.microd.model.LatLng;

/**
 * Created by racit-2105 on 25/01/17.
 */
public class HaversineUtil {

    public static final double R = 6372.8; // In kilometers

    /**
     *
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @return radial distance between two lat longs in meters
     */
    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return (R * c) * 1000;
    }


    public static double bearing(double lat1, double lon1, double lat2, double lon2){
        double longitude1 = lon1;
        double longitude2 = lon2;
        double latitude1 = Math.toRadians(lat1);
        double latitude2 = Math.toRadians(lat2);
        double longDiff= Math.toRadians(longitude2-longitude1);
        double y= Math.sin(longDiff)*Math.cos(latitude2);
        double x=Math.cos(latitude1)*Math.sin(latitude2)-Math.sin(latitude1)*Math.cos(latitude2)*Math.cos(longDiff);

        return (Math.toDegrees(Math.atan2(y, x))+360)%360;
    }

//    public static Direction GetDirection(LatLng a, LatLng b, LatLng c)
//    {
//        double theta1 = GetAngle(a, b);
//        double theta2 = GetAngle(b, c);
//        double delta = NormalizeAngle(theta2 - theta1);
//
//        if (delta == 0)
//            return Direction.Straight;
//        else if ( delta == Math.PI )
//            return Direction.U;
//        else if ( delta < Math.PI )
//            return Direction.Left;
//        else return Direction.Right;
//    }
//
//    private static Double GetAngle(LatLng p1, LatLng p2)
//    {
//        Double m = p2.getLng() - p1.getLng() / (p2.getLat() - p1.getLat());
//        Double angleFromXAxis = Math.atan((p2.getLng() - p1.getLng()) / (p2.getLat() - p1.getLat())); // where y = m * x + K
//        return  (p2.getLat() - p1.getLat()) < 0 ? m + Math.PI : m ; // The will go to the correct Quadrant
//    }
//
//    private static Double NormalizeAngle(Double angle)
//    {
//        return angle < 0 ? angle + 2 * Math.PI : angle; //This will make sure angle is [0..2PI]
//    }

//    public static Direction GetTurnDirection(LatLng A, LatLng B, LatLng C)
//    {
//        LatLng v1 = new LatLng((B.getLat() - A.getLat()),(B.getLng() - A.getLng()));
//        LatLng v2 = new LatLng((C.getLat() - B.getLat()),(C.getLng() - B.getLng()));
//        double cross = v1.getLat()*v2.getLng() - v1.getLng()*v2.getLat();
//        if (cross > 0) { return Direction.Left ; }
//        if (cross < 0) { return Direction.Right ; }
//        double dot =  v1.getLat()*v2.getLat() + v1.getLng()*v2.getLng() ;
//        if (dot > 0) { return Direction.Straight ; }
//        return Direction.U ;
//    }

    public static Direction getTurn(LatLng centre,LatLng toPoint) {

        Double angle = getAngle(centre,toPoint);
        if(angleIsBetweenAngles(angle, 337.5, 23.5)) {
            return Direction.Straight;
        } else if(angleIsBetweenAngles(angle, 23.5, 46.0)) {
            return Direction.Slight_right;
        } else if(angleIsBetweenAngles(angle, 46.0, 91.0)) {
            return Direction.Right;
        } else if(angleIsBetweenAngles(angle, 91.0, 271.0)) {
            return Direction.U;
        } else if(angleIsBetweenAngles(angle, 271.0, 316.0)) {
            return Direction.Left;
        } else if(angleIsBetweenAngles(angle,316.0,337.0)){
            return Direction.Slight_left;
        }

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.replace(0,5,"");
        return null;
    }


    /**
     * To check if the angle 300 is between 180 and 10 degrees:

     BOOL isBetween=angle_is_between_angles( 300, 180,10);
     * @param N
     * @param a
     * @param b
     * @return
     */
    static boolean angleIsBetweenAngles(Double N,Double a,Double b) {
        N = angle_1to360(N); //normalize angles to be 1-360 degrees
        a = angle_1to360(a);
        b = angle_1to360(b);

        if (a < b)
            return a <= N && N <= b;
        return a <= N || N <= b;
    }

    static Double angle_1to360(double angle){
        angle=((int)angle % 360) + angle; //converts angle to range -360 + 360
        if(angle>0.0)
            return angle;
        else
            return angle + 360.0;
    }


    /**
     *
     * @param toPoint
     * @return 360 as 0 deg
     */
    public static Double getAngle(LatLng centre,LatLng toPoint) {
        Double mCentreX = centre.getLat();
        Double mCentreY = centre.getLng();
        double dx = toPoint.getLat() - mCentreX;
        // Minus to correct for coord re-mapping
        double dy = -(toPoint.getLng() - mCentreY);

        double inRads = Math.atan2(dy,dx);

        // We need to map to coord system when 0 degree is at 3 O'clock, 270 at 12 O'clock
        if (inRads < 0)
            inRads = Math.abs(inRads);
        else
            inRads = 2*Math.PI - inRads;

        return Math.toDegrees(inRads) % 360;
    }

    public static void main(String[] args) {
        System.out.println(haversine(12.9290566, 77.6093888, 12.9307966, 77.6097112));

        System.out.println(bearing(12.9290566, 77.6093888, 12.9307966, 77.6097112));


        System.out.println(getTurn(new LatLng(12.9290566, 77.6093888), new LatLng(12.9307966, 77.6097112)));

    }

    public enum Direction {
        Straight,
        Right,
        Left,
        U,
        Slight_right,
        Slight_left
    }
}
