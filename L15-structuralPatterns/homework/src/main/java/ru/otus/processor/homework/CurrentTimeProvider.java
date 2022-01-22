package ru.otus.processor.homework;

import java.time.LocalDateTime;

public interface CurrentTimeProvider {

    LocalDateTime now();
}
