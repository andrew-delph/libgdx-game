package networking.events.incoming;

import infra.entitydata.EntityData;
import infra.events.Event;
import io.grpc.stub.StreamObserver;
import networking.NetworkObject;

import java.util.HashMap;

public class IncomingUpdateEntityEvent implements Event {

    public static String type = "update_entity";
    HashMap<String, Object> data;

    public IncomingUpdateEntityEvent(
            EntityData updateData, StreamObserver<NetworkObject.UpdateNetworkObject> requestObserver) {
        this.data = new HashMap<>();
        this.data.put("entityData", updateData);
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
