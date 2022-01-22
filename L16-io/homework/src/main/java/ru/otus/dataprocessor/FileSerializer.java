package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class FileSerializer implements Serializer {

    private final ObjectMapper mapper;
    private final String fileName;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
        this.mapper = new ObjectMapper();
    }

    @Override
    public void serialize(Map<String, Double> data) {
        //формирует результирующий json и сохраняет его в файл
        try {
            var file = new File(fileName);
            mapper.writeValue(file, data);
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}
