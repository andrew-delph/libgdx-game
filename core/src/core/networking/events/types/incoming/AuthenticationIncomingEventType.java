package core.networking.events.types.incoming;

import static core.networking.events.types.NetworkEventTypeEnum.AUTH_INCOMING;

import core.app.user.UserID;
import core.common.events.types.EventType;
import core.networking.RequestNetworkEventObserver;

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
  public String getEventType() {
    return AUTH_INCOMING;
  }
}
