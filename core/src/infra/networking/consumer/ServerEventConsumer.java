package infra.networking.consumer;

import com.google.inject.Inject;
import infra.app.GameController;
import infra.chunk.ChunkSubscriptionService;
import infra.common.events.EventService;
import infra.entity.EntitySerializationConverter;
import infra.networking.events.SubscriptionEvent;

public class ServerEventConsumer extends NetworkConsumer {
  @Inject EventService eventService;
  @Inject GameController gameController;
  @Inject EntitySerializationConverter entitySerializationConverter;
  @Inject ChunkSubscriptionService chunkSubscriptionService;

  public void init() {
    this.eventService.addListener(
        SubscriptionEvent.type,
        event -> {
          SubscriptionEvent realEvent = (SubscriptionEvent) event;
          chunkSubscriptionService.registerSubscription(
              realEvent.getUser(), realEvent.getChunkRangeList());
        });
  }
}
