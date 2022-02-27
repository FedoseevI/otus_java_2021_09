package ru.otus.protobuf;


import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.protobuf.service.RemoteServiceImpl;
import ru.otus.protobuf.service.ServerNumberGenerator;
import ru.otus.protobuf.service.ServerNumberGeneratorImpl;

import java.io.IOException;

public class GRPCServer {

    private static final Logger logger = LoggerFactory.getLogger(GRPCServer.class);

    public static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws IOException, InterruptedException {

        ServerNumberGenerator serverNumberGenerator = new ServerNumberGeneratorImpl();
        RemoteServiceImpl remoteService = new RemoteServiceImpl(serverNumberGenerator);

        Server server = ServerBuilder
                .forPort(SERVER_PORT)
                .addService(remoteService).build();
        server.start();
        logger.info("server waiting for client connections...");
        server.awaitTermination();
    }
}
