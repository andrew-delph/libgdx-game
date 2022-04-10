package networking.events.types.incoming;

import static networking.events.types.NetworkEventTypeEnum.PING_RESPONSE_INCOMING;

import app.user.UserID;
import common.events.types.EventType;
import java.util.UUID;

public class PingResponseIncomingEventType extends EventType {

  private final UserID userID;
  private final UUID pingID;
  private final Long receivedTime;

  public PingResponseIncomingEventType(UserID userID, UUID pingID, Long receivedTime) {
    this.userID = userID;
    this.pingID = pingID;
    this.receivedTime = receivedTime;
  }

  public Long getReceivedTime() {
    return receivedTime;
  }

  public UUID getPingID() {
    return pingID;
  }

  public UserID getUserID() {
    return userID;
  }

  @Override
  public String getType() {
    return PING_RESPONSE_INCOMING;
  }
}
