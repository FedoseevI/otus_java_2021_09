package ru.otus.homework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;

public class NumberSequenceDemo {

    private static final Logger logger = LoggerFactory.getLogger(NumberSequenceDemo.class);
    private int currentThread = 1;
    private int currentValue = 1;
    private int plusValue = 1;
    private final int threadsNumber = 2;

    public static void main(String[] args) {
        NumberSequenceDemo numberSequenceDemo = new NumberSequenceDemo();
        var executor = Executors.newFixedThreadPool(numberSequenceDemo.threadsNumber);
        for (int i = 1; i <= numberSequenceDemo.threadsNumber; i++) {
            int finalI = i;
            executor.submit(() -> numberSequenceDemo.calcSequence(finalI));
        }
        executor.shutdown();
    }

    private synchronized void calcSequence(int currentThread) {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                //spurious wakeup https://en.wikipedia.org/wiki/Spurious_wakeup
                //поэтому не if
                while (currentThread != this.currentThread) {
                    this.wait();
                }

                this.currentThread += 1;

                logger.info(Integer.toString(this.currentValue));

                if (currentThread == this.threadsNumber) {
                    this.currentThread = 1;
                    var newValue = this.currentValue + this.plusValue;
                    if (newValue == 0 || newValue > 10) this.plusValue *= -1;
                    this.currentValue += this.plusValue;
                }
                sleep();
                notifyAll();

            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

}
