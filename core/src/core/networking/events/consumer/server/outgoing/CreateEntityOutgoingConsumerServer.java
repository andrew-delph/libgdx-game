package core.networking.events.consumer.server.outgoing;

import core.app.user.UserID;
import core.chunk.ActiveChunkManager;
import com.google.inject.Inject;
import core.common.events.types.EventType;
import java.util.function.Consumer;
import networking.NetworkObjects;
import core.networking.events.types.outgoing.CreateEntityOutgoingEventType;
import core.networking.server.ServerNetworkHandle;

public class CreateEntityOutgoingConsumerServer implements Consumer<EventType> {

  @Inject ActiveChunkManager activeChunkManager;
  @Inject ServerNetworkHandle serverNetworkHandle;

  @Override
  public void accept(EventType eventType) {
    CreateEntityOutgoingEventType realEvent = (CreateEntityOutgoingEventType) eventType;
    NetworkObjects.NetworkEvent networkEvent = realEvent.toNetworkEvent();
    for (UserID uuid : activeChunkManager.getChunkRangeUsers(realEvent.getChunkRange())) {
      serverNetworkHandle.send(uuid, networkEvent);
    }
  }
}
