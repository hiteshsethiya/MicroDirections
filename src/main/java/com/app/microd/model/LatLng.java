package com.app.microd.model;

import com.app.microd.HaversineUtil;

/**
 * Created by racit-2105 on 25/01/17.
 */
public final class LatLng {

    private Double lat;
    private Double lng;

    public LatLng() {
    }

    public LatLng(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double distance(Double lat, Double lng) {
        return HaversineUtil.haversine(this.lat,this.lng,lat,lng);
    }

    public Double distance(LatLng toPoint) {
        return HaversineUtil.haversine(this.lat,this.lng,toPoint.getLat(),toPoint.getLng());
    }

}
