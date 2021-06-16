package infra.networking.consumer;

import com.google.inject.Inject;
import infra.app.GameController;
import infra.common.GameStore;
import infra.common.events.EventConsumer;
import infra.common.events.EventService;
import infra.entity.Entity;
import infra.entity.EntitySerializationConverter;
import infra.entity.block.Block;
import infra.networking.client.ClientNetworkHandle;
import infra.networking.events.*;

public class ClientEventConsumer extends EventConsumer {
  @Inject EventService eventService;
  @Inject GameController gameController;
  @Inject EntitySerializationConverter entitySerializationConverter;

  @Inject ClientNetworkHandle clientNetworkHandle;
  @Inject GameStore gameStore;
  @Inject EventFactory eventFactory;

  @Inject
  public ClientEventConsumer() {}

  public void init() {
    super.init();
    this.eventService.addListener(
        CreateEntityIncomingEvent.type,
        event -> {
          CreateEntityIncomingEvent realEvent = (CreateEntityIncomingEvent) event;
          Entity entity = entitySerializationConverter.createEntity(realEvent.getData());
          //           TODO remove or update
          if (this.gameStore.getEntity(entity.uuid) != null) {
            return;
          } else {
          }
          gameController.triggerCreateEntity(entity);
        });
    this.eventService.addListener(
        UpdateEntityIncomingEvent.type,
        event -> {
          UpdateEntityIncomingEvent realEvent = (UpdateEntityIncomingEvent) event;
          entitySerializationConverter.updateEntity(realEvent.getData());
        });
    this.eventService.addListener(
        CreateEntityOutgoingEvent.type,
        event -> {
          CreateEntityOutgoingEvent realEvent = (CreateEntityOutgoingEvent) event;
          clientNetworkHandle.send(realEvent.toNetworkEvent());
        });
    this.eventService.addListener(
        UpdateEntityOutgoingEvent.type,
        event -> {
          UpdateEntityOutgoingEvent realEvent = (UpdateEntityOutgoingEvent) event;
          clientNetworkHandle.send(realEvent.toNetworkEvent());
        });
    this.eventService.addListener(
        RemoveEntityIncomingEvent.type,
        event -> {
          RemoveEntityIncomingEvent realEvent = (RemoveEntityIncomingEvent) event;
          Entity entity = entitySerializationConverter.createEntity(realEvent.getData());
          eventService.queuePostUpdateEvent(eventFactory.createRemoveEntityEvent(entity.uuid));
        });

    this.eventService.addListener(
        ReplaceBlockOutgoingEvent.type,
        event -> {
          ReplaceBlockOutgoingEvent realEvent = (ReplaceBlockOutgoingEvent) event;
          this.eventService.queuePostUpdateEvent(
              eventFactory.createReplaceBlockEvent(
                  realEvent.getTarget(), realEvent.getReplacementBlock()));
          this.clientNetworkHandle.send(realEvent.toNetworkEvent());
        });
    this.eventService.addListener(
        ReplaceBlockIncomingEvent.type,
        event -> {
          ReplaceBlockIncomingEvent realEvent = (ReplaceBlockIncomingEvent) event;
          this.eventService.queuePostUpdateEvent(
              this.eventFactory.createReplaceBlockEvent(
                  realEvent.getTarget(),
                  (Block)
                      entitySerializationConverter.createEntity(
                          realEvent.getReplacementBlockData())));
        });
  }
}
