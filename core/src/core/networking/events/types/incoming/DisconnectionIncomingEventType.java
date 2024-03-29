package core.networking.events.types.incoming;

import com.google.inject.Inject;
import core.app.user.UserID;
import core.common.events.types.EventType;

public class DisconnectionIncomingEventType extends EventType {

  public static final String type = "disconnection_entity_incoming";
  private final UserID userID;

  @Inject
  public DisconnectionIncomingEventType(UserID userID) {
    this.userID = userID;
  }

  public UserID getUserID() {
    return userID;
  }

  @Override
  public String getEventType() {
    return type;
  }
}
