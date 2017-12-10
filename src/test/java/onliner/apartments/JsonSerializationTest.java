package onliner.apartments;

import com.fasterxml.jackson.databind.ObjectMapper;
import onliner.apartments.model.Apartment;
import onliner.apartments.model.Contact;
import onliner.apartments.model.Filter;
import onliner.apartments.model.Source;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

@Ignore
public class JsonSerializationTest {

    @Test
    public void testSerializeFilter() throws IOException {
        Filter filter = new Filter();
        filter.setActive(Filter.Active.ALL);
        filter.setOwner(Filter.Owner.ANY);
        filter.setName("test");

        ObjectMapper mapper = new ObjectMapper();

        String json = mapper.writeValueAsString(filter);
        Filter actual = mapper.readValue(json, Filter.class);

        assertNotNull(actual);
        assertEquals(filter.getName(), actual.getName());
    }

    @Test
    public void testSerializeApartment_ShouldSerializeSource() throws IOException {
        Apartment apartment = new Apartment();
        Source source = new Source();
        source.setName("test source");
        apartment.setSource(source);

        ObjectMapper mapper = new ObjectMapper();

        String json = mapper.writeValueAsString(apartment);
        Apartment actual = mapper.readValue(json, Apartment.class);

        assertNotNull(actual);
        assertEquals(apartment.getSource(), actual.getSource());
    }

    @Test
    public void testSerializeApartment_ShouldSerializeOwner() throws IOException {
        Apartment owner = new Apartment();
        owner.setContact(new Contact(Boolean.TRUE));
        Apartment notOwner = new Apartment();
        notOwner.setContact(new Contact(Boolean.FALSE));

        ObjectMapper mapper = new ObjectMapper();

        String ownerJson = mapper.writeValueAsString(owner);
        Apartment ownerActual = mapper.readValue(ownerJson, Apartment.class);
        String notOwnerJson = mapper.writeValueAsString(notOwner);
        Apartment notOwnerActual = mapper.readValue(notOwnerJson, Apartment.class);

        assertNotNull(ownerActual);
        assertTrue(ownerActual.getContact().getOwner());
        assertNotNull(notOwnerActual);
        assertFalse(notOwnerActual.getContact().getOwner());
    }

}
