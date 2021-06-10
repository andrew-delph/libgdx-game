package infra.networking.events;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import infra.common.events.Event;
import infra.networking.NetworkObjects;

import java.util.UUID;

public class ReplaceBlockIncomingEvent extends Event {

  public static String type = "replace_block_incoming";

  UUID target;
  String replacementBlockType;
  public NetworkObjects.NetworkEvent networkEvent;

  public UUID getTarget() {
    return target;
  }

  public String getReplacementBlockType() {
    return replacementBlockType;
  }

  @Inject
  public ReplaceBlockIncomingEvent(@Assisted NetworkObjects.NetworkEvent networkEvent) {
    this.networkEvent = networkEvent;
    for (NetworkObjects.NetworkData networkData : networkEvent.getData().getChildrenList()) {
      switch (networkData.getKey()) {
        case "target":
          this.target = UUID.fromString(networkData.getValue());
          break;
        case "replacementBlockType":
          this.replacementBlockType = networkData.getValue();
          break;
      }
    }
  }

  @Override
  public String getType() {
    return type;
  }
}
