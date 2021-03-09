package networking.events;

import infra.entity.EntityData;
import infra.events.Event;
import io.grpc.stub.StreamObserver;
import networking.NetworkObject;
import networking.connetion.RemoveConnection;

import java.util.HashMap;

public class RemoveEntityEvent implements Event {
    HashMap<String, Object> data;
    public static String type = "remove_entity";

    public RemoveEntityEvent(EntityData removeData, StreamObserver<NetworkObject.RemoveNetworkObject> requestObserver) {
        this.data = new HashMap<>();
        this.data.put("entityData", removeData);
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

