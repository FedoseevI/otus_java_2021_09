package ru.otus.processor.homework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class SecondExceptionProcessorTest {

    SecondExceptionProcessor secondExceptionProcessor;
    CurrentTimeProvider currentTimeProvider;

    @BeforeEach
    void init() {
        currentTimeProvider = mock(CurrentTimeProvider.class);
        secondExceptionProcessor = new SecondExceptionProcessor(currentTimeProvider);
    }

    @Test
    void second3_process_message() {

        var mockDateTime = LocalDateTime.of(2022, 1, 1, 0, 0, 3);
        when(currentTimeProvider.now()).thenReturn(mockDateTime);
        var id = 1;
        var prepMessage = new Message.Builder(id).build();
        Message processedMessage = secondExceptionProcessor.process(prepMessage);

        assertEquals(prepMessage, processedMessage);
        verify(currentTimeProvider, times(1)).now();
    }

    @Test
    void second4_process_message() {
        var mockDateTime = LocalDateTime.of(2022, 1, 1, 0, 0, 4);
        when(currentTimeProvider.now()).thenReturn(mockDateTime);
        var id = 1;
        var prepMessage = new Message.Builder(id).build();

        assertThrows(RuntimeException.class, () -> secondExceptionProcessor.process(prepMessage));
        verify(currentTimeProvider, times(1)).now();
    }
}