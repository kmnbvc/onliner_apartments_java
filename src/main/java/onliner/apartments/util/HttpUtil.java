package onliner.apartments.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.IOException;

public class HttpUtil {

    static {
        Unirest.setObjectMapper(new JacksonMapper());
        Unirest.setDefaultHeader("Accept", "application/json");
    }

    public static String get(String url) {
        try {
            return Unirest.get(url).asString().getBody();
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }
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
