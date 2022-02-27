package ru.otus.protobuf.service;

import java.util.List;

public interface ServerNumberGenerator {

    List<Long> getNumbers(Long startNumber, Long endNumber);
}
