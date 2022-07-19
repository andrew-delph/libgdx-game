package core.networking.events.types.outgoing;

import core.app.user.UserID;
import core.common.events.types.EventType;
import core.networking.events.types.NetworkEventTypeEnum;
import java.util.UUID;

public class PingRequestOutgoingEventType extends EventType {
  UserID userID;
  UUID pingID;

  public PingRequestOutgoingEventType(UserID userID, UUID pingID) {
    this.userID = userID;
    this.pingID = pingID;
  }

  public UUID getPingID() {
    return pingID;
  }

  public UserID getUserID() {
    return userID;
  }

  @Override
  public String getEventType() {
    return NetworkEventTypeEnum.PING_REQUEST_OUTGOING;
  }
}
