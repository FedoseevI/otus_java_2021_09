package ru.otus.protobuf.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

public class ServerNumberGeneratorImpl implements ServerNumberGenerator {

    private final List<Long> numbersList = new ArrayList<>();

    @Override
    public List<Long> getNumbers(Long startNumber, Long endNumber) {
        LongStream.rangeClosed(startNumber, endNumber).forEach(numbersList::add);
        return numbersList;
    }
}
