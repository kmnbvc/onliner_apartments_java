package onliner.apartments;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import onliner.apartments.model.*;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

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

    @Test
    public void testConvertSourceToUrl() {
        Source source = new Source();
        source.setActive(Boolean.FALSE);
        source.setName("nameasfsd");
        PriceRange priceRange = new PriceRange();
        priceRange.setCurrency(Currency.USD);
        priceRange.setMax(300);
        priceRange.setMin(180);
        source.setPriceRange(priceRange);
        Bounds bounds = new Bounds();
        bounds.setNorthWestLatitude(53.93258425698772);
        bounds.setNorthWestLongitude(27.66497585746782);
        bounds.setSouthEastLatitude(53.953156370259734);
        bounds.setSouthEastLongitude(27.701652665753826);
        source.setBounds(bounds);

        String expected = "https://ak.api.onliner.by/search/apartments?price%5Bmin%5D=180&price%5Bmax%5D=300" +
                "&currency=USD&bounds%5Blb%5D%5Blat%5D=53.93258425698772&bounds%5Blb%5D%5Blong%5D=27.66497585746782" +
                "&bounds%5Brt%5D%5Blat%5D=53.953156370259734&bounds%5Brt%5D%5Blong%5D=27.701652665753826";
        String actual = source.getUrl();

        assertEquals(expected, actual);
    }

    @Test
    public void testSerializeSource() throws JsonProcessingException {
        Source source = new Source();
        source.setActive(Boolean.FALSE);
        source.setName("nameasfsd");
        PriceRange priceRange = new PriceRange();
        priceRange.setCurrency(Currency.USD);
        priceRange.setMax(300);
        priceRange.setMin(180);
        source.setPriceRange(priceRange);
        Bounds bounds = new Bounds();
        bounds.setNorthWestLatitude(53.93258425698772);
        bounds.setNorthWestLongitude(27.66497585746782);
        bounds.setSouthEastLatitude(53.953156370259734);
        bounds.setSouthEastLongitude(27.701652665753826);
        source.setBounds(bounds);

        ObjectMapper mapper = new ObjectMapper();

        String actual = mapper.writeValueAsString(source);
        String expected = "{\"name\":\"nameasfsd\",\"active\":false,\"priceRange\":{\"min\":180,\"max\":300,\"currency\":\"USD\"},\"bounds\":{\"northWestLatitude\":53.93258425698772,\"northWestLongitude\":27.66497585746782,\"southEastLatitude\":53.953156370259734,\"southEastLongitude\":27.701652665753826,\"empty\":false},\"url\":\"https://ak.api.onliner.by/search/apartments?price%5Bmin%5D=180&price%5Bmax%5D=300&currency=USD&bounds%5Blb%5D%5Blat%5D=53.93258425698772&bounds%5Blb%5D%5Blong%5D=27.66497585746782&bounds%5Brt%5D%5Blat%5D=53.953156370259734&bounds%5Brt%5D%5Blong%5D=27.701652665753826\"}";

        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeSource() throws IOException {
        String data = "{\"name\":\"nameasfsd\",\"active\":false,\"priceRange\":{\"min\":180,\"max\":300," +
                "\"currency\":\"USD\"},\"bounds\":{\"northWestLatitude\":53.93258425698772," +
                "\"northWestLongitude\":27.66497585746782,\"southEastLatitude\":53.953156370259734," +
                "\"southEastLongitude\":27.701652665753826}}";
        ObjectMapper mapper = new ObjectMapper();

        Source actual = mapper.readValue(data, Source.class);

        assertNotNull(actual);
    }

}
