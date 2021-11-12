package networking.consumer;

import app.GameController;
import com.google.inject.Inject;
import common.GameStore;
import common.events.Event;
import common.events.EventConsumer;
import common.events.EventService;
import entity.Entity;
import entity.EntitySerializationConverter;
import entity.block.Block;
import networking.client.ClientNetworkHandle;
import networking.events.*;

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
    Consumer<Event> createEntityIncoming =
        event -> {
          CreateEntityIncomingEvent realEvent = (CreateEntityIncomingEvent) event;
          Entity entity = entitySerializationConverter.createEntity(realEvent.getData());
          //           TODO remove or update
          if (this.gameStore.getEntity(entity.uuid) != null) {
            return;
          }
          gameController.triggerCreateEntity(entity);
        };

    Consumer<Event> updateEntityIncoming =
        event -> {
          UpdateEntityIncomingEvent realEvent = (UpdateEntityIncomingEvent) event;
          entitySerializationConverter.updateEntity(realEvent.getData());
        };

    Consumer<Event> removeEntityIncoming =
        event -> {
          RemoveEntityIncomingEvent realEvent = (RemoveEntityIncomingEvent) event;
          Entity entity = entitySerializationConverter.createEntity(realEvent.getData());
          eventService.queuePostUpdateEvent(eventFactory.createRemoveEntityEvent(entity.uuid));
        };

    Consumer<Event> replaceBlockIncoming =
        event -> {
          ReplaceBlockIncomingEvent realEvent = (ReplaceBlockIncomingEvent) event;
          this.eventService.queuePostUpdateEvent(
              this.eventFactory.createReplaceBlockEvent(
                  realEvent.getTarget(),
                  (Block)
                      entitySerializationConverter.createEntity(
                          realEvent.getReplacementBlockData())));
        };

    this.eventService.addListener(CreateEntityIncomingEvent.type, createEntityIncoming);
    this.eventService.addListener(UpdateEntityIncomingEvent.type, updateEntityIncoming);
    this.eventService.addListener(RemoveEntityIncomingEvent.type, removeEntityIncoming);
    this.eventService.addListener(ReplaceBlockIncomingEvent.type, replaceBlockIncoming);

    Consumer<Event> createEntityOutgoing =
        event -> {
          CreateEntityOutgoingEvent realEvent = (CreateEntityOutgoingEvent) event;
          clientNetworkHandle.send(realEvent.toNetworkEvent());
        };

    Consumer<Event> updateEntityOutgoing =
        event -> {
          UpdateEntityOutgoingEvent realEvent = (UpdateEntityOutgoingEvent) event;
          clientNetworkHandle.send(realEvent.toNetworkEvent());
        };

    Consumer<Event> replaceBlockOutgoing =
        event -> {
          ReplaceBlockOutgoingEvent realEvent = (ReplaceBlockOutgoingEvent) event;
          this.eventService.queuePostUpdateEvent(
              eventFactory.createReplaceBlockEvent(
                  realEvent.getTarget(), realEvent.getReplacementBlock()));
          this.clientNetworkHandle.send(realEvent.toNetworkEvent());
        };

    this.eventService.addListener(CreateEntityOutgoingEvent.type, createEntityOutgoing);
    this.eventService.addListener(UpdateEntityOutgoingEvent.type, updateEntityOutgoing);
    this.eventService.addListener(ReplaceBlockOutgoingEvent.type, replaceBlockOutgoing);
  }
}
