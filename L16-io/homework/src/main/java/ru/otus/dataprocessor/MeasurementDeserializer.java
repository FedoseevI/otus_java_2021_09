package ru.otus.dataprocessor;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import ru.otus.model.Measurement;

import java.io.IOException;

public class MeasurementDeserializer extends JsonDeserializer<Measurement> {

    @Override
    public Measurement deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String EMPTY_STRING = "";
        JsonNode node = null;
        try {
            node = p.readValueAsTree();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String name = node.has("name") ? node.get("name").asText() : EMPTY_STRING;
        Double value = node.has("value") ? node.get("value").asDouble() : null;
        return new Measurement(name, value);
    }
}
