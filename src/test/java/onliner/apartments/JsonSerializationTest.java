package onliner.apartments;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import onliner.apartments.model.Filter;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

}
