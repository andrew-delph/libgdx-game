package infra.networking.consumer;

import com.google.inject.Inject;
import infra.app.GameController;
import infra.common.GameStore;
import infra.common.events.EventService;
import infra.entity.Entity;
import infra.entity.EntitySerializationConverter;
import infra.networking.client.ClientNetworkHandle;
import infra.networking.events.*;

public class ClientEventConsumer extends NetworkConsumer {
  @Inject EventService eventService;
  @Inject GameController gameController;
  @Inject EntitySerializationConverter entitySerializationConverter;

  @Inject ClientNetworkHandle clientNetworkHandle;
  @Inject GameStore gameStore;

  @Inject
  public ClientEventConsumer() {}

  public void init() {
    super.init();
    this.eventService.addListener(
        CreateEntityIncomingEvent.type,
        event -> {
          System.out.println("create entity incoming");
          CreateEntityIncomingEvent realEvent = (CreateEntityIncomingEvent) event;
          Entity entity = entitySerializationConverter.createEntity(realEvent.getData());
          //           TODO remove or update
          if (this.gameStore.getEntity(entity.uuid) != null) {
            System.out.println(1);
            return;
          } else {
            System.out.println(2);
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
                  this.gameStore.removeEntity(entity.uuid);
              });
  }
}
