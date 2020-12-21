package org.example;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;

import java.io.IOException;

/**
 * Hello world!
 */
public class AppServer {
    public static void main(String[] args) throws IOException, InterruptedException {

        System.out.println("init server");

        Server server = ServerBuilder.forPort(99).addService(new BasicService()).addService(ProtoReflectionService.newInstance()).build().start();

        System.out.println("running server");

        server.awaitTermination();

        System.out.println("ended server");


    }

}
