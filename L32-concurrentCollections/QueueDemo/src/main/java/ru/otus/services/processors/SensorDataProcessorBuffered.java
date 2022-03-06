package ru.otus.services.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.lib.SensorDataBufferedWriter;
import ru.otus.api.SensorDataProcessor;
import ru.otus.api.model.SensorData;

import java.util.Comparator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

// Этот класс нужно реализовать
public class SensorDataProcessorBuffered implements SensorDataProcessor {
    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);

    private final int bufferSize;
    private final SensorDataBufferedWriter writer;
    private final ConcurrentLinkedQueue<SensorData> dataBuffer = new ConcurrentLinkedQueue<>();
    private final ReentrantLock locker = new ReentrantLock();

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
    }

    @Override
    public void process(SensorData data) {
        dataBuffer.add(data);
        locker.lock();
        try {
            if (dataBuffer.size() >= bufferSize) {
                flush();
            }
        } catch (Exception e) {
            log.error("Ошибка в процессе обработки", e);
        } finally {
            locker.unlock();
        }
    }

    public void flush() {
        locker.lock();
        try {
            if (dataBuffer.size() > 0) {
                writer.writeBufferedData(dataBuffer.stream().sorted(Comparator.comparing(SensorData::getMeasurementTime)).collect(Collectors.toList()));
                dataBuffer.clear();
            }
        } catch (Exception e) {
            log.error("Ошибка в процессе записи буфера", e);
        } finally {
            locker.unlock();
        }
    }

    @Override
    public void onProcessingEnd() {
        flush();
    }
}
