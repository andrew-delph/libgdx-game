package old.networking.events;

import com.google.inject.Inject;
import old.infra.entity.Entity;
import old.infra.entity.EntityFactory;
import old.infra.entity.EntityManager;
import old.infra.entitydata.EntityData;
import old.infra.events.EventService;
import old.networking.NetworkObjectFactory;
import old.networking.client.ClientNetworkHandle;
import old.networking.events.incoming.IncomingCreateEntityEvent;
import old.networking.events.incoming.IncomingRemoveEntityEvent;
import old.networking.events.incoming.IncomingUpdateEntityEvent;
import old.networking.events.outgoing.OutgoingCreateEntityEvent;
import old.networking.events.outgoing.OutgoingRemoveEntityEvent;
import old.networking.events.outgoing.OutgoingUpdateEntityEvent;

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
