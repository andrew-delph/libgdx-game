package old.networking.events.outgoing;

import old.infra.entitydata.EntityData;
import old.infra.events.Event;

import java.util.HashMap;

public class OutgoingRemoveEntityEvent implements Event {
  public static String type = "outgoing_remove_entity";
  HashMap<String, Object> data;

  public OutgoingRemoveEntityEvent(EntityData removeData) {
    this.data = new HashMap<>();
    this.data.put("entityData", removeData);
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
