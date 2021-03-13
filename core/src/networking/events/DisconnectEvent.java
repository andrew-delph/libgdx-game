package networking.events;

import infra.events.Event;
import io.grpc.stub.StreamObserver;

import java.util.HashMap;

public class DisconnectEvent implements Event {

    HashMap<String, Object> data;
    public static String type = "disconnect";

    DisconnectEvent(StreamObserver requestObserver) {
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
