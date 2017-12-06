package onliner.apartments.binding;

import com.fasterxml.jackson.annotation.JsonProperty;
import onliner.apartments.model.Apartment;
import onliner.apartments.model.Source;

import java.util.ArrayList;
import java.util.List;

public class PageData {
    private List<Apartment> apartments = new ArrayList<>();
    private int total;
    @JsonProperty("page")
    private PageInfo info;

    public List<Apartment> getApartments() {
        return apartments;
    }

    public PageInfo getInfo() {
        return info;
    }

    public PageData setSource(Source source) {
        apartments.forEach(a -> a.setSource(source));
        return this;
    }
}
