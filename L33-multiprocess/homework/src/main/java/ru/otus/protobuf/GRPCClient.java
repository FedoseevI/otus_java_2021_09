package ru.otus.protobuf;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.protobuf.generated.ClientRequest;
import ru.otus.protobuf.generated.RemoteServiceGrpc;
import ru.otus.protobuf.generated.ServerResponse;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

public class GRPCClient {

    private static final Logger logger = LoggerFactory.getLogger(GRPCClient.class);

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;
    private static final long SERVER_START_NUMBER = 1;
    private static final long SERVER_END_NUMBER = 30;
    private static final long CLIENT_LOOP_COUNT = 50;
    private static final ReentrantLock locker = new ReentrantLock();

    private static long lastNumber = 0;

    public static void main(String[] args) throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        RemoteServiceGrpc.RemoteServiceStub newStub = RemoteServiceGrpc.newStub(channel);

        CountDownLatch latch = new CountDownLatch(1);

        var clientRequest = ClientRequest
                .newBuilder()
                .setStartNumber(SERVER_START_NUMBER)
                .setEndNumber(SERVER_END_NUMBER)
                .build();

        newStub.getNumbers(clientRequest, new StreamObserver<>() {
            @Override
            public void onNext(ServerResponse response) {
                locker.lock();
                try {
                    lastNumber = response.getCurrentNumber();
                    logger.info("new value = {}", lastNumber);
                } catch (Exception e) {
                    logger.error("Ошибка в процессе обработки", e);
                } finally {
                    locker.unlock();
                }
            }

            @Override
            public void onError(Throwable t) {
                logger.error("error", t);
            }

            @Override
            public void onCompleted() {
                logger.info("completed");
                latch.countDown();
            }
        });


        var currentValue = 0L;

        for (long i = 1; i <= CLIENT_LOOP_COUNT; i++) {
            locker.lock();
            try {
                currentValue = currentValue + lastNumber + 1;
                lastNumber = 0;
                logger.info("currentValue: {}", currentValue);
            } catch (Exception e) {
                logger.error("Ошибка в процессе обработки", e);
            } finally {
                locker.unlock();
            }
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                logger.error("Ошибка в процессе обработки", e);
            }
        }


        latch.await();

        channel.shutdown();
    }

}
