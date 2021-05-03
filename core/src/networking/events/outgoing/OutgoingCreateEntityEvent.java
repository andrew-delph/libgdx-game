package networking.events.outgoing;

import infra.entity.EntityData;
import infra.events.Event;
import io.grpc.stub.StreamObserver;
import networking.NetworkObject;

import java.util.HashMap;

public class OutgoingCreateEntityEvent implements Event {
    public static String type = "outgoing_create_entity";
    HashMap<String, Object> data;

    public OutgoingCreateEntityEvent(EntityData createData) {
        this.data = new HashMap<>();
        this.data.put("entityData", createData);
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
