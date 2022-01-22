package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.CollectionType;
import ru.otus.model.Measurement;

import java.io.InputStream;
import java.util.List;

public class ResourcesFileLoader implements Loader {

    private final ObjectMapper mapper;
    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
        this.mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Measurement.class, new MeasurementDeserializer());
        mapper.registerModule(module);
    }

    @Override
    public List<Measurement> load() {
        ClassLoader classLoader = this.getClass().getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(fileName)) {
            CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class, Measurement.class);
            return mapper.readValue(inputStream.readAllBytes(), listType);
        } catch (Exception e) {
            throw new FileProcessException(e);
        }
    }

}
