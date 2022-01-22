package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

import java.time.LocalDateTime;

public class SecondExceptionProcessor implements Processor {

    private final CurrentTimeProvider currentTimeProvider;

    public SecondExceptionProcessor(CurrentTimeProvider dateTimeProvider) {
        this.currentTimeProvider = dateTimeProvider;
    }


    @Override
    public Message process(Message message) {

        var currenttime = currentTimeProvider.now();

        if (currenttime.getSecond() % 2 == 0) {
            throw new RuntimeException("внимание: секунда четная! продолжение невозможжно!");
        }

        return message.toBuilder().build();
    }
}
