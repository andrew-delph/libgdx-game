package infra.serialization;

import com.google.inject.Guice;
import com.google.inject.Injector;
import configuration.ClientConfig;
import chunk.ChunkRange;
import common.Coordinates;
import networking.events.EventFactory;
import networking.events.SubscriptionIncomingEvent;
import networking.events.SubscriptionOutgoingEvent;
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
    SubscriptionOutgoingEvent subscriptionOutgoingEvent =
        new SubscriptionOutgoingEvent(chunkRangeList);
    UUID uuid = UUID.randomUUID();

    SubscriptionIncomingEvent subscriptionIncomingEvent =
        new SubscriptionIncomingEvent(
            subscriptionOutgoingEvent.toNetworkEvent().toBuilder()
                .setUser(uuid.toString())
                .build());

    Assert.assertEquals(
        subscriptionOutgoingEvent.getChunkRangeList(),
        subscriptionIncomingEvent.getChunkRangeList());

    assert subscriptionIncomingEvent.getUser().equals(uuid);
  }
}
