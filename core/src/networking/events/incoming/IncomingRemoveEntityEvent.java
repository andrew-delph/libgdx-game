package networking.events.incoming;

import infra.entitydata.EntityData;
import infra.events.Event;
import io.grpc.stub.StreamObserver;
import networking.NetworkObject;

import java.util.HashMap;

public class IncomingRemoveEntityEvent implements Event {
    public static String type = "remove_entity";
    HashMap<String, Object> data;

    public IncomingRemoveEntityEvent(
            EntityData removeData, StreamObserver<NetworkObject.RemoveNetworkObject> requestObserver) {
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
