package onliner.apartments.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.text.MessageFormat;
import java.util.Arrays;

@Entity
@Table(name = "sources")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Source {

    private static final String URL_ROOT = "https://ak.api.onliner.by/search/apartments?";
    private static final String PRICE_PARAMS = "price%5Bmin%5D={0}&price%5Bmax%5D={1}&currency={2}";
    private static final String BOUNDS_PARAMS = "bounds%5Blb%5D%5Blat%5D={0}&bounds%5Blb%5D%5Blong%5D={1}&bounds%5Brt%5D%5Blat%5D={2}&bounds%5Brt%5D%5Blong%5D={3}";

    @Id
    private String name;
    private Boolean active = Boolean.TRUE;
    @Embedded
    private PriceRange priceRange;
    @Embedded
    private Bounds bounds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return URL_ROOT + String.join("&", Arrays.asList(toParams(priceRange), toParams(bounds)));
    }

    private String toParams(PriceRange priceRange) {
        if (priceRange == null)
            return "";

        return MessageFormat.format(PRICE_PARAMS, priceRange.getMin(), priceRange.getMax(), priceRange.getCurrency());
    }

    private String toParams(Bounds bounds) {
        if (bounds == null || bounds.isEmpty())
            return "";

        return MessageFormat.format(BOUNDS_PARAMS,
                bounds.getNorthWestLatitude().toString(), bounds.getNorthWestLongitude().toString(),
                bounds.getSouthEastLatitude().toString(), bounds.getSouthEastLongitude().toString());
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public PriceRange getPriceRange() {
        return priceRange;
    }

    public Bounds getBounds() {
        return bounds;
    }

    public void setPriceRange(PriceRange priceRange) {
        this.priceRange = priceRange;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Source source = (Source) o;

        return name != null ? name.equals(source.name) : source.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
