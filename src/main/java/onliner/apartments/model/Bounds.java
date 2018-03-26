package onliner.apartments.model;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class Bounds {
    @NotNull
    private Double northWestLatitude;
    @NotNull
    private Double northWestLongitude;
    @NotNull
    private Double southEastLatitude;
    @NotNull
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

}