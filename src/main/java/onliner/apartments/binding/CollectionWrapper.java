package onliner.apartments.binding;

import java.util.ArrayList;
import java.util.Collection;

public class CollectionWrapper<T> {
    private Collection<T> items = new ArrayList<>();

    public Collection<T> getItems() {
        return items;
    }

    public void setItems(Collection<T> items) {
        this.items = items;
    }
}
