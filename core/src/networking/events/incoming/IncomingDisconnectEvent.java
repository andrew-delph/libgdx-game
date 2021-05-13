package networking.events.incoming;

import infra.events.Event;
import io.grpc.stub.StreamObserver;

import java.util.HashMap;

public class IncomingDisconnectEvent implements Event {

  public static String type = "disconnect";
  HashMap<String, Object> data;

  public IncomingDisconnectEvent(StreamObserver requestObserver) {
    this.data = new HashMap<>();
    this.data.put("requestObserver", requestObserver);
  }

  @Override
  public String getType() {
    return type;
  }

  @Override
  public HashMap<String, Object> getData() {
    return this.data;
  }
}
