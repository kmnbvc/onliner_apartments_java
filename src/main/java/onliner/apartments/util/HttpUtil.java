package onliner.apartments.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.impl.client.AutoRetryHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

public class HttpUtil {

    static {
        /*AutoRetryHttpClient client = new AutoRetryHttpClient(new DefaultHttpClient(), new ServiceUnavailableRetryStrategy() {
            @Override
            public boolean retryRequest(HttpResponse response, int executionCount, HttpContext context) {
                return executionCount < 10;
            }

            @Override
            public long getRetryInterval() {
                return 500;
            }
        });
        Unirest.setHttpClient(client);*/
        Unirest.setObjectMapper(new JacksonMapper());
        Unirest.setDefaultHeader("Accept", "application/json");
    }

    public static <T> T get(String url, Class<T> type) {
        try {
            return Unirest.get(url).asObject(type).getBody();
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }
    }

    private static class JacksonMapper implements ObjectMapper {
        private com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();

        public JacksonMapper() {
            mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        }

        public <T> T readValue(String value, Class<T> valueType) {
            try {
                return mapper.readValue(value, valueType);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public String writeValue(Object value) {
            try {
                return mapper.writeValueAsString(value);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
