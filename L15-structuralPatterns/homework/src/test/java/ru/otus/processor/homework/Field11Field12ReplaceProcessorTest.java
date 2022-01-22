package ru.otus.processor.homework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.processor.Processor;

import static org.junit.jupiter.api.Assertions.*;

class Field11Field12ReplaceProcessorTest {

    Field11Field12ReplaceProcessor replaceProcessor;

    @BeforeEach
    void setUp() {
        replaceProcessor = new Field11Field12ReplaceProcessor();
    }

    @Test
    void process() {

        var field11startVal = "field_11_startval";
        var field12startval = "field_12_startval";

        var startMessage = new Message.Builder(1L)
                .field11(field11startVal)
                .field12(field12startval)
                .build();

        Message afterMessage = replaceProcessor.process(startMessage);

        assertEquals(field11startVal, afterMessage.getField12());
        assertEquals(field12startval, afterMessage.getField11());
    }
}