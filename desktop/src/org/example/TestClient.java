package org.example;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class TestClient {

    private final ManagedChannel channel;
    private final MessageTestServiceGrpc.MessageTestServiceBlockingStub blockingStub;
    private final MessageTestServiceGrpc.MessageTestServiceStub asyncStub;

    public TestClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext());
    }

    public TestClient(ManagedChannelBuilder<?> channelBuilder) {
        channel = channelBuilder.build();
        blockingStub = MessageTestServiceGrpc.newBlockingStub(channel);
        asyncStub = MessageTestServiceGrpc.newStub(channel);
    }

    public static void main(String[] args) throws InterruptedException {

//        TestClient.simple();
        TestClient.chat();

    }

    public static void simple() throws InterruptedException {
        TestClient grpcClient = null;
        try {
            grpcClient = new TestClient("127.0.0.1", 99);
            TestProto.MessageA messageA = TestProto.MessageA.newBuilder().setData("this is the client message").build();
            TestProto.MessageB messageB = grpcClient.blockingStub.secondService(messageA);
            System.out.println(messageB);
        } catch (Exception e) {

        } finally {
            grpcClient.shutdown();
        }
    }

    public static void chat() {

        TestClient grpcClient = null;
        Scanner myInput = new Scanner(System.in);

        grpcClient = new TestClient("127.0.0.1", 99);


        StreamObserver<TestProto.MessageA> responseObserver = new StreamObserver<TestProto.MessageA>() {
            @Override
            public void onNext(TestProto.MessageA messageA) {

                System.out.println("<<< " + messageA.getData());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println();
                System.out.println("error " + throwable);
            }

            @Override
            public void onCompleted() {
                System.out.println("COMPLETE");
            }
        };

        StreamObserver<TestProto.MessageA> requestObserver = grpcClient.asyncStub.bidirectionStreamTest(responseObserver);

        System.out.println("Start chatting!");

        while (true) {
            String input = myInput.nextLine();
            TestProto.MessageA clientMessage = TestProto.MessageA.newBuilder().setData(input).build();
            System.out.println(">>> " + clientMessage.getData());
            requestObserver.onNext(clientMessage);
        }
    }

    public void shutdown() throws InterruptedException {
        System.out.println("shutdown");
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }
}

//package com.example.tutorial;
//
//import io.grpc.ManagedChannel;
//import io.grpc.ManagedChannelBuilder;
//
//import java.util.concurrent.TimeUnit;
//import java.util.logging.Logger;
//
//public class GrpcClient {
//
//    private final ManagedChannel channel;
//    private final AddressGuideGrpc.AddressGuideBlockingStub blockingStub;
//    private final AddressGuideGrpc.AddressGuideStub asyncStub;
//
//    public GrpcClient(String host, int port) {
//        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext());
//    }
//
//    public GrpcClient(ManagedChannelBuilder<?> channelBuilder) {
//        channel = channelBuilder.build();
//        blockingStub = AddressGuideGrpc.newBlockingStub(channel);
//        asyncStub = AddressGuideGrpc.newStub(channel);
//    }
//
//    public static void main(String[] args) throws InterruptedException {
//        GrpcClient grpcClient = null;
//        try{
//            grpcClient = new GrpcClient("127.0.0.1", 8980);
//            AddressBook addressBook = grpcClient.getAddressBook(Person.newBuilder().build());;
//        }catch (Exception e){
//
//        }finally {
//            grpcClient.shutdown();
//        }
//    }
//
//    public void shutdown() throws InterruptedException {
//        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
//    }
//
//    public AddressBook getAddressBook(Person person) {
//        return blockingStub.getAddressBook(person);
//    }
//}