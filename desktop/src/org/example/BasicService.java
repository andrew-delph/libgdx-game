package org.example;

import io.grpc.stub.StreamObserver;

public class BasicService extends MessageTestServiceGrpc.MessageTestServiceImplBase {

//    @Override
//    public void testGetAndrewMessage(TestProto.MessageA request, StreamObserver<TestProto.MessageB> responseObserver) {
//        System.out.println(request.getData());
//        responseObserver.onNext(TestProto.MessageB.newBuilder().setData(request.getData() + " added on top!").build());
//        responseObserver.onCompleted();
//    }

    @Override
    public void secondService(TestProto.MessageA request, StreamObserver<TestProto.MessageB> responseObserver) {
        responseObserver.onNext(TestProto.MessageB.newBuilder().setData(request.getData() + " (just returned from server)").build());
        responseObserver.onCompleted();
    }

//    @Override
//    public void streamMessage(TestProto.AndrewMessage request, StreamObserver<TestProto.MessageB> responseObserver) {
//        try {
//            responseObserver.onNext(TestProto.MessageB.newBuilder().setData("111!").build());
//            TimeUnit.SECONDS.sleep(1);
//            responseObserver.onNext(TestProto.MessageB.newBuilder().setData("222!").build());
//
//            TimeUnit.SECONDS.sleep(3);
//
//            responseObserver.onNext(TestProto.MessageB.newBuilder().setData("333!").build());
//            TimeUnit.SECONDS.sleep(1);
//        } catch (Exception e) {
//            System.out.println("the exception");
//        }
//        responseObserver.onCompleted();
//    }

    @Override
    public StreamObserver<TestProto.MessageA> bidirectionStreamTest(final StreamObserver<TestProto.MessageA> responseObserver) {

        return new StreamObserver<TestProto.MessageA>() {
            @Override
            public void onNext(TestProto.MessageA note) {
                System.out.println("go input " + note);
                TestProto.MessageA reponseMessage = TestProto.MessageA.newBuilder().setData("[" + note.getData() + "]").build();
                responseObserver.onNext(reponseMessage);
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("got an error");
            }

            @Override
            public void onCompleted() {
                System.out.println("complete");
                responseObserver.onCompleted();
            }
        };
    }

}
