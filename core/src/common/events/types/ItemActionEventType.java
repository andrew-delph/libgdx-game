package common.events.types;

import static networking.translation.NetworkDataSerializer.serializeItemActionEventType;

import entity.attributes.inventory.item.ItemActionType;
import java.util.UUID;
import networking.NetworkObjects.NetworkEvent;
import networking.events.interfaces.SerializeNetworkEvent;

public class ItemActionEventType extends EventType implements SerializeNetworkEvent {

  public static String type = "item_action";

  ItemActionType itemActionType;
  UUID controleeUUID;

  public ItemActionEventType(ItemActionType itemActionType, UUID controleeUUID) {
    this.itemActionType = itemActionType;
    this.controleeUUID = controleeUUID;
  }

  public ItemActionType getItemActionType() {
    return itemActionType;
  }

  public UUID getControleeUUID() {
    return controleeUUID;
  }

  @Override
  public String getEventType() {
    return type;
  }

  @Override
  public NetworkEvent toNetworkEvent() {
    return serializeItemActionEventType(this);
  }
}
