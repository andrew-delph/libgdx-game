package core.networking.events.consumer.server.incoming;

import com.google.inject.Inject;
import core.common.Clock;
import core.common.events.types.EventType;
import core.networking.events.types.incoming.PingResponseIncomingEventType;
import core.networking.ping.PingService;
import java.util.function.Consumer;

public class PingResponseIncomingConsumerServer implements Consumer<EventType> {

  @Inject PingService pingService;

  @Override
  public void accept(EventType eventType) {
    PingResponseIncomingEventType realEvent = (PingResponseIncomingEventType) eventType;

    pingService.setResponseTime(
        realEvent.getUserID(),
        realEvent.getPingID(),
        Clock.getCurrentTime(),
        realEvent.getReceivedTime());
  }
}
