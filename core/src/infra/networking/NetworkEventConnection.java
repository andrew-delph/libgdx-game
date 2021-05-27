package infra.networking;

import io.grpc.stub.StreamObserver;

public class NetworkEventConnection {
  // used to send events to the server
  private StreamObserver<NetworkObjects.NetworkEvent> responseNetworkEvent;

  // used to receive events from the server
  private StreamObserver<NetworkObjects.NetworkEvent> requestNetworkEvent;

  public NetworkEventConnection() {}

  public StreamObserver<NetworkObjects.NetworkEvent> getResponseNetworkEvent() {
    return responseNetworkEvent;
  }

  public void setResponseNetworkEvent(
      StreamObserver<NetworkObjects.NetworkEvent> responseNetworkEvent) {
    this.responseNetworkEvent = responseNetworkEvent;
  }

  public StreamObserver<NetworkObjects.NetworkEvent> getRequestNetworkEvent() {
    return requestNetworkEvent;
  }

  public void setRequestNetworkEvent(
      StreamObserver<NetworkObjects.NetworkEvent> requestNetworkEvent) {
    this.requestNetworkEvent = requestNetworkEvent;
  }
}
