package networking.events;

import com.google.inject.Inject;
import infra.entity.Entity;
import infra.entity.EntityFactory;
import infra.entity.EntityManager;
import infra.entitydata.EntityData;
import infra.events.EventService;
import networking.NetworkObjectFactory;
import networking.client.ClientNetworkHandle;
import networking.events.incoming.IncomingCreateEntityEvent;
import networking.events.incoming.IncomingRemoveEntityEvent;
import networking.events.incoming.IncomingUpdateEntityEvent;
import networking.events.outgoing.OutgoingCreateEntityEvent;
import networking.events.outgoing.OutgoingRemoveEntityEvent;
import networking.events.outgoing.OutgoingUpdateEntityEvent;

import java.util.UUID;

public class ClientEventRegister implements EventRegister {

  @Inject EventService eventService;

  @Inject EntityManager entityManager;

  @Inject EntityFactory entityFactory;

  @Inject ClientNetworkHandle clientNetworkHandle;

  @Inject NetworkObjectFactory networkObjectFactory;

  @Override
  public void register() {
    this.eventService.addListener(
        IncomingCreateEntityEvent.type,
        event -> {
          EntityData entityData = (EntityData) event.getData().get("entityData");
          Entity createEntity = entityFactory.create(entityData);
          entityManager.add(createEntity);
        });
    this.eventService.addListener(
        IncomingUpdateEntityEvent.type,
        event -> {
          EntityData entityData = (EntityData) event.getData().get("entityData");
          UUID targetUuid = UUID.fromString(entityData.getID());
          Entity target = entityManager.get(targetUuid);
          if (target == null) {
            return;
          }
          target.fromEntityData(entityData);
        });
    this.eventService.addListener(
        IncomingRemoveEntityEvent.type,
        event -> {
          EntityData entityData = (EntityData) event.getData().get("entityData");
          UUID targetUuid = UUID.fromString(entityData.getID());
          Entity target = entityManager.get(targetUuid);
          if (target == null) {
            return;
          }
          entityManager.remove(entityData.getID());
        });
    this.eventService.addListener(
        OutgoingCreateEntityEvent.type,
        event -> {
          EntityData entityData = (EntityData) event.getData().get("entityData");
          clientNetworkHandle.createRequest.onNext(
              networkObjectFactory.createNetworkObject(entityData));
        });
    this.eventService.addListener(
        OutgoingUpdateEntityEvent.type,
        event -> {
          EntityData entityData = (EntityData) event.getData().get("entityData");
          clientNetworkHandle.updateRequest.onNext(
              networkObjectFactory.updateNetworkObject(entityData));
        });
    this.eventService.addListener(
        OutgoingRemoveEntityEvent.type,
        event -> {
          EntityData entityData = (EntityData) event.getData().get("entityData");
          clientNetworkHandle.removeRequest.onNext(
              networkObjectFactory.removeNetworkObject(entityData));
        });
  }
}
