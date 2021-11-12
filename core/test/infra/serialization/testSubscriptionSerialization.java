package infra.serialization;

import chunk.ChunkRange;
import com.google.inject.Guice;
import com.google.inject.Injector;
import common.Coordinates;
import configuration.ClientConfig;
import networking.events.EventFactory;
import networking.events.types.incoming.SubscriptionIncomingEventType;
import networking.events.types.outgoing.SubscriptionOutgoingEventType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class testSubscriptionSerialization {

  Injector injector;

  EventFactory eventFactory;

  @Before
  public void setup() {
    injector = Guice.createInjector(new ClientConfig());
    eventFactory = injector.getInstance(EventFactory.class);
  }

  @Test
  public void testHandleSubscriptionEvent() {
    List<ChunkRange> chunkRangeList = new LinkedList<>();
    chunkRangeList.add(new ChunkRange(new Coordinates(0, 1)));
    chunkRangeList.add(new ChunkRange(new Coordinates(-2, 1)));
    SubscriptionOutgoingEventType subscriptionOutgoingEvent =
        new SubscriptionOutgoingEventType(chunkRangeList);
    UUID uuid = UUID.randomUUID();

    SubscriptionIncomingEventType subscriptionIncomingEvent =
        new SubscriptionIncomingEventType(
            subscriptionOutgoingEvent.toNetworkEvent().toBuilder()
                .setUser(uuid.toString())
                .build());

    Assert.assertEquals(
        subscriptionOutgoingEvent.getChunkRangeList(),
        subscriptionIncomingEvent.getChunkRangeList());

    assert subscriptionIncomingEvent.getUser().equals(uuid);
  }
}
