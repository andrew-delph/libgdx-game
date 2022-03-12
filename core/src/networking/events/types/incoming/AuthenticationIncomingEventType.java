package networking.events.types.incoming;

import static networking.events.types.NetworkEventTypeEnum.AUTH_INCOMING;

import app.user.UserID;
import common.events.types.EventType;
import networking.RequestNetworkEventObserver;

public class AuthenticationIncomingEventType extends EventType {

  UserID user;
  RequestNetworkEventObserver requestNetworkEventObserver;

  public AuthenticationIncomingEventType(
      UserID user, RequestNetworkEventObserver requestNetworkEventObserver) {
    this.user = user;
    this.requestNetworkEventObserver = requestNetworkEventObserver;
  }

  public UserID getUser() {
    return user;
  }

  public RequestNetworkEventObserver getRequestNetworkEventObserver() {
    return requestNetworkEventObserver;
  }

  @Override
  public String getType() {
    return AUTH_INCOMING;
  }
}
