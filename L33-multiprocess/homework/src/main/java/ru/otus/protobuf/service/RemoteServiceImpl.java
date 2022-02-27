package ru.otus.protobuf.service;

import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.ClientRequest;
import ru.otus.protobuf.generated.RemoteServiceGrpc;
import ru.otus.protobuf.generated.ServerResponse;

public class RemoteServiceImpl extends RemoteServiceGrpc.RemoteServiceImplBase {

    private final ServerNumberGenerator serverNumberGenerator;

    public RemoteServiceImpl(ServerNumberGenerator serverNumberGenerator) {
        this.serverNumberGenerator = serverNumberGenerator;
    }

    @Override
    public void getNumbers(ClientRequest request, StreamObserver<ServerResponse> responseObserver) {
        var generatorNumbers = serverNumberGenerator.getNumbers(request.getStartNumber(), request.getEndNumber());
        generatorNumbers.forEach(number -> {
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
            }
            responseObserver.onNext(response(number));
        });
        responseObserver.onCompleted();
    }

    private ServerResponse response(Long currentNumber) {
        return ServerResponse
                .newBuilder()
                .setCurrentNumber(currentNumber)
                .build();
    }
}
