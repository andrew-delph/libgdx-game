package networking.events.consumer.client.incoming;

import com.google.inject.Inject;
import common.Clock;
import common.events.types.EventType;
import java.util.function.Consumer;
import networking.events.types.incoming.PingResponseIncomingEventType;
import networking.ping.PingService;

public class PingResponseIncomingConsumerClient implements Consumer<EventType> {

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
