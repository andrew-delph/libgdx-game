package networking.events.consumer;

import app.GameController;
import com.google.inject.Inject;
import common.GameStore;
import common.events.EventType;
import common.events.EventConsumer;
import common.events.EventService;
import entity.Entity;
import entity.EntitySerializationConverter;
import entity.block.Block;
import networking.client.ClientNetworkHandle;
import networking.events.*;
import networking.events.types.incoming.CreateEntityIncomingEventType;
import networking.events.types.incoming.RemoveEntityIncomingEventType;
import networking.events.types.incoming.ReplaceBlockIncomingEventType;
import networking.events.types.incoming.UpdateEntityIncomingEventType;
import networking.events.types.outgoing.CreateEntityOutgoingEventType;
import networking.events.types.outgoing.ReplaceBlockOutgoingEventType;
import networking.events.types.outgoing.UpdateEntityOutgoingEventType;

import java.util.function.Consumer;

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
    Consumer<EventType> createEntityIncoming =
        event -> {
          CreateEntityIncomingEventType realEvent = (CreateEntityIncomingEventType) event;
          Entity entity = entitySerializationConverter.createEntity(realEvent.getData());
          //           TODO remove or update
          if (this.gameStore.getEntity(entity.uuid) != null) {
            return;
          }
          gameController.triggerCreateEntity(entity);
        };

    Consumer<EventType> updateEntityIncoming =
        event -> {
          UpdateEntityIncomingEventType realEvent = (UpdateEntityIncomingEventType) event;
          entitySerializationConverter.updateEntity(realEvent.getData());
        };

    Consumer<EventType> removeEntityIncoming =
        event -> {
          RemoveEntityIncomingEventType realEvent = (RemoveEntityIncomingEventType) event;
          Entity entity = entitySerializationConverter.createEntity(realEvent.getData());
          eventService.queuePostUpdateEvent(eventFactory.createRemoveEntityEvent(entity.uuid));
        };

    Consumer<EventType> replaceBlockIncoming =
        event -> {
          ReplaceBlockIncomingEventType realEvent = (ReplaceBlockIncomingEventType) event;
          this.eventService.queuePostUpdateEvent(
              this.eventFactory.createReplaceBlockEvent(
                  realEvent.getTarget(),
                  (Block)
                      entitySerializationConverter.createEntity(
                          realEvent.getReplacementBlockData())));
        };

    this.eventService.addListener(CreateEntityIncomingEventType.type, createEntityIncoming);
    this.eventService.addListener(UpdateEntityIncomingEventType.type, updateEntityIncoming);
    this.eventService.addListener(RemoveEntityIncomingEventType.type, removeEntityIncoming);
    this.eventService.addListener(ReplaceBlockIncomingEventType.type, replaceBlockIncoming);

    Consumer<EventType> createEntityOutgoing =
        event -> {
          CreateEntityOutgoingEventType realEvent = (CreateEntityOutgoingEventType) event;
          clientNetworkHandle.send(realEvent.toNetworkEvent());
        };

    Consumer<EventType> updateEntityOutgoing =
        event -> {
          UpdateEntityOutgoingEventType realEvent = (UpdateEntityOutgoingEventType) event;
          clientNetworkHandle.send(realEvent.toNetworkEvent());
        };

    Consumer<EventType> replaceBlockOutgoing =
        event -> {
          ReplaceBlockOutgoingEventType realEvent = (ReplaceBlockOutgoingEventType) event;
          this.eventService.queuePostUpdateEvent(
              eventFactory.createReplaceBlockEvent(
                  realEvent.getTarget(), realEvent.getReplacementBlock()));
          this.clientNetworkHandle.send(realEvent.toNetworkEvent());
        };

    this.eventService.addListener(CreateEntityOutgoingEventType.type, createEntityOutgoing);
    this.eventService.addListener(UpdateEntityOutgoingEventType.type, updateEntityOutgoing);
    this.eventService.addListener(ReplaceBlockOutgoingEventType.type, replaceBlockOutgoing);
  }
}
