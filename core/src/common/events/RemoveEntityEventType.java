package common.events;

import com.google.inject.Inject;

import java.util.UUID;

public class RemoveEntityEventType extends EventType {

  public static String type = "remove_entity";

  UUID entityUUID;

  @Inject
  public RemoveEntityEventType(UUID entityUUID) {
    this.entityUUID = entityUUID;
  }

  public UUID getEntityUUID() {
    return entityUUID;
  }

  @Override
  public String getType() {
    return type;
  }
}
