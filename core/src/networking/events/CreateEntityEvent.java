package networking.events;

import infra.entity.EntityData;
import infra.events.Event;
import io.grpc.stub.StreamObserver;
import networking.NetworkObject;

import java.util.HashMap;

public class CreateEntityEvent implements Event {
    public static String type = "create_entity";
    HashMap<String, Object> data;

    public CreateEntityEvent(EntityData createData, StreamObserver<NetworkObject.CreateNetworkObject> requestObserver) {
        this.data = new HashMap<>();
        this.data.put("entityData", createData);
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
