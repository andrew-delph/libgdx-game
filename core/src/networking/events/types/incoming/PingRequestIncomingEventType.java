package networking.events.types.incoming;

import static networking.events.types.NetworkEventTypeEnum.PING_REQUEST_INCOMING;

import app.user.UserID;
import common.events.types.EventType;
import java.util.UUID;

public class PingRequestIncomingEventType extends EventType {
  private final UserID userID;
  private final UUID pingID;

  public PingRequestIncomingEventType(UserID userID, UUID pingID) {
    this.userID = userID;
    this.pingID = pingID;
  }

  public UserID getUserID() {
    return userID;
  }

  public UUID getPingID() {
    return pingID;
  }

  @Override
  public String getEventType() {
    return PING_REQUEST_INCOMING;
  }
}
