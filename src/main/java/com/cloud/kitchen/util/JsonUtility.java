package com.cloud.kitchen.util;

import com.cloud.kitchen.models.Order;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.cloud.kitchen.constants.StringConstants.ORDERS_FILE;

/**
 * The JsonUtility class provides utility methods for working with JSON data,
 * specifically for deserializing orders from a JSON file.
 */
public class JsonUtility {

    private final static Logger logger = LogManager.getLogger(JsonUtility.class);

    /**
     * Retrieves an ObjectMapper instance configured for JSON processing.
     *
     * @return Configured ObjectMapper instance.
     */
    public static ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    /**
     * Reads and deserializes a list of Order objects from a JSON file.
     *
     * @return List of Order objects read from the JSON file.
     */
    public static List<Order> readOrders() {
        List<Order> orders = new ArrayList<>();
        try {
            InputStream inputStream = JsonUtility.class.getClassLoader().getResourceAsStream(ORDERS_FILE);
            if(inputStream == null) {
                logger.error("File not found!");
                return orders;
            }
            orders = getObjectMapper().readValue(inputStream, new TypeReference<>() {
            });
        } catch (Exception exception) {
            logger.error("errorMessage={}. stackTrace={}", exception, ExceptionUtils.getMessage(exception));
        }
        return orders;
    }
}
