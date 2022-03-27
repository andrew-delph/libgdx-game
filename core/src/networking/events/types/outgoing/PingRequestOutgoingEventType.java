package networking.events.types.outgoing;

import static networking.events.types.NetworkEventTypeEnum.PING_REQUEST_OUTGOING;

import app.user.UserID;
import common.events.types.EventType;
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
  public String getType() {
    return PING_REQUEST_OUTGOING;
  }
}
