package onliner.apartments.model;

import javax.persistence.Embeddable;

@Embeddable
public class Bounds {
    private Double northWestLatitude;
    private Double northWestLongitude;
    private Double southEastLatitude;
    private Double southEastLongitude;

    public Double getNorthWestLatitude() {
        return northWestLatitude;
    }

    public void setNorthWestLatitude(Double northWestLatitude) {
        this.northWestLatitude = northWestLatitude;
    }

    public Double getNorthWestLongitude() {
        return northWestLongitude;
    }

    public void setNorthWestLongitude(Double northWestLongitude) {
        this.northWestLongitude = northWestLongitude;
    }

    public Double getSouthEastLatitude() {
        return southEastLatitude;
    }

    public void setSouthEastLatitude(Double southEastLatitude) {
        this.southEastLatitude = southEastLatitude;
    }

    public Double getSouthEastLongitude() {
        return southEastLongitude;
    }

    public void setSouthEastLongitude(Double southEastLongitude) {
        this.southEastLongitude = southEastLongitude;
    }

    public boolean isEmpty() {
        return northWestLatitude == null || northWestLongitude == null
                || southEastLatitude == null || southEastLongitude == null;
    }
}