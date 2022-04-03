package networking.events.types.outgoing;

import static networking.events.types.NetworkEventTypeEnum.PING_RESPONSE_OUTGOING;

import common.events.types.EventType;
import java.util.UUID;

public class PingResponseOutgoingEventType extends EventType {

  private UUID pingID;

  public PingResponseOutgoingEventType(UUID pingID) {
    this.pingID = pingID;
  }

  public UUID getPingID() {
    return pingID;
  }

  @Override
  public String getType() {
    return PING_RESPONSE_OUTGOING;
  }
}