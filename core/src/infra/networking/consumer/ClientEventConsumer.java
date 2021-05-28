package infra.networking.consumer;

import com.google.inject.Inject;
import infra.app.GameController;
import infra.common.events.EventService;
import infra.entity.EntitySerializationConverter;
import infra.networking.client.ClientNetworkHandle;
import infra.networking.events.CreateEntityIncomingEvent;
import infra.networking.events.CreateEntityOutgoingEvent;
import infra.networking.events.UpdateEntityIncomingEvent;
import infra.networking.events.UpdateEntityOutgoingEvent;

public class ClientEventConsumer extends NetworkConsumer {
  @Inject EventService eventService;
  @Inject GameController gameController;
  @Inject EntitySerializationConverter entitySerializationConverter;

  @Inject ClientNetworkHandle clientNetworkHandle;

  @Inject
  public ClientEventConsumer() {}

  public void init() {
    super.init();
    this.eventService.addListener(
        CreateEntityIncomingEvent.type,
        event -> {
          System.out.println("create entity incoming");
          CreateEntityIncomingEvent realEvent = (CreateEntityIncomingEvent) event;
          gameController.triggerCreateEntity(
              entitySerializationConverter.createEntity(realEvent.getData()));
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
  }
}
