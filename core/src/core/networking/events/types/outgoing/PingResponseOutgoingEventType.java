package core.networking.events.types.outgoing;

import core.common.events.types.EventType;
import core.networking.events.types.NetworkEventTypeEnum;
import java.util.UUID;

public class PingResponseOutgoingEventType extends EventType {

  private final UUID pingID;

  public PingResponseOutgoingEventType(UUID pingID) {
    this.pingID = pingID;
  }

  public UUID getPingID() {
    return pingID;
  }

  @Override
  public String getEventType() {
    return NetworkEventTypeEnum.PING_RESPONSE_OUTGOING;
  }
}
