package networking.events.types.incoming;

import app.user.UserID;
import com.google.inject.Inject;
import common.events.types.EventType;

import java.util.UUID;

public class DisconnectionIncomingEventType extends EventType {

    public final static String type = "disconnection_entity_incoming";
    private final UserID userID;

    @Inject
    public DisconnectionIncomingEventType(UserID userID) {
        this.userID = userID;
    }

    public UserID getUserID() {
        return userID;
    }

    @Override
    public String getType() {
        return type;
    }
}
