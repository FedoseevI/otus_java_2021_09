package ru.otus.services.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.lib.SensorDataBufferedWriter;
import ru.otus.api.SensorDataProcessor;
import ru.otus.api.model.SensorData;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

// Этот класс нужно реализовать
public class SensorDataProcessorBuffered implements SensorDataProcessor {
    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);

    private final int bufferSize;
    private final SensorDataBufferedWriter writer;
    List<SensorData> dataBuffer = new CopyOnWriteArrayList<>();

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
    }

    @Override
    public void process(SensorData data) {
        dataBuffer.add(data);
        synchronized (writer) {
            if (dataBuffer.size() >= bufferSize) {
                flush();
            }
        }
    }

    public void flush() {
        synchronized (writer) {
            try {
                if (dataBuffer.size() > 0) {
                    writer.writeBufferedData(dataBuffer.stream().sorted(Comparator.comparing(SensorData::getMeasurementTime)).collect(Collectors.toList()));
                    dataBuffer.clear();
                }
            } catch (Exception e) {
                log.error("Ошибка в процессе записи буфера", e);
            }
        }
    }

    @Override
    public void onProcessingEnd() {
        flush();
    }
}
