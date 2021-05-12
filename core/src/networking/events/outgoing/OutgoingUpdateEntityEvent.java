package networking.events.outgoing;

import infra.entitydata.EntityData;
import infra.events.Event;

import java.util.HashMap;

public class OutgoingUpdateEntityEvent implements Event {
    public static String type = "outgoing_update_entity";
    HashMap<String, Object> data;

    public OutgoingUpdateEntityEvent(EntityData updateData) {
        this.data = new HashMap<>();
        this.data.put("entityData", updateData);
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
