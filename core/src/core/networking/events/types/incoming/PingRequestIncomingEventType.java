package core.networking.events.types.incoming;

import core.app.user.UserID;
import core.common.events.types.EventType;
import core.networking.events.types.NetworkEventTypeEnum;
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
    return NetworkEventTypeEnum.PING_REQUEST_INCOMING;
  }
}
