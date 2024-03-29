package core.networking.translation;

import com.google.inject.Guice;
import com.google.inject.Injector;
import core.app.user.UserID;
import core.common.ChunkRange;
import core.common.CommonFactory;
import core.configuration.ClientConfig;
import core.networking.events.types.incoming.SubscriptionIncomingEventType;
import core.networking.events.types.outgoing.SubscriptionOutgoingEventType;
import java.util.LinkedList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class testSubscriptionSerialization {

  Injector injector;

  @Before
  public void setup() {
    injector = Guice.createInjector(new ClientConfig());
  }

  @Test
  public void testHandleSubscriptionEvent() {
    List<ChunkRange> chunkRangeList = new LinkedList<>();
    chunkRangeList.add(CommonFactory.createChunkRange(CommonFactory.createCoordinates(0, 1)));
    chunkRangeList.add(CommonFactory.createChunkRange(CommonFactory.createCoordinates(-2, 1)));
    SubscriptionOutgoingEventType subscriptionOutgoingEvent =
        new SubscriptionOutgoingEventType(chunkRangeList);
    UserID userID = UserID.createUserID();

    SubscriptionIncomingEventType subscriptionIncomingEvent =
        new SubscriptionIncomingEventType(
            subscriptionOutgoingEvent.toNetworkEvent().toBuilder()
                .setUser(userID.toString())
                .build());

    Assert.assertEquals(
        subscriptionOutgoingEvent.getChunkRangeList(),
        subscriptionIncomingEvent.getChunkRangeList());

    Assert.assertEquals(subscriptionIncomingEvent.getUserID(), userID);
  }
}
