package com.eyeline.miniapps.connector;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 * Created by jeck on 24/07/17.
 */
public class AiApplication extends ResourceConfig {
    public AiApplication() {
        packages("com.eyeline.miniapps.connector");
        packages("com.eyeline.miniapps.ai.model");
        register(ObjectMapperProvider.class);
        register(JacksonFeature.class);
    }

    @Provider
    public static class ObjectMapperProvider implements ContextResolver<ObjectMapper> {

        final ObjectMapper defaultObjectMapper;

        public ObjectMapperProvider() {
            defaultObjectMapper = createDefaultMapper();
        }

        @Override
        public ObjectMapper getContext(final Class<?> type) {

            return defaultObjectMapper;
        }


        private static ObjectMapper createDefaultMapper() {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
          mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
          mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
          mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            return mapper;
        }
    }
}
