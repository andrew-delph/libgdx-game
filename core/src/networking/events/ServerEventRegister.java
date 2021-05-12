package networking.events;

import com.google.inject.Inject;
import infra.entity.Entity;
import infra.entity.EntityFactory;
import infra.entity.EntityManager;
import infra.entitydata.EntityData;
import infra.events.EventService;
import io.grpc.stub.StreamObserver;
import networking.NetworkObject;
import networking.NetworkObjectFactory;
import networking.connection.*;
import networking.events.incoming.IncomingCreateEntityEvent;
import networking.events.incoming.IncomingDisconnectEvent;
import networking.events.incoming.IncomingRemoveEntityEvent;
import networking.events.incoming.IncomingUpdateEntityEvent;
import networking.events.outgoing.OutgoingCreateEntityEvent;
import networking.events.outgoing.OutgoingRemoveEntityEvent;
import networking.events.outgoing.OutgoingUpdateEntityEvent;

import java.util.UUID;

public class ServerEventRegister implements EventRegister {

  @Inject EventService eventService;

  @Inject EntityManager entityManager;

  @Inject EntityFactory entityFactory;

  @Inject ConnectionStore connectionStore;

  @Inject NetworkObjectFactory networkObjectFactory;

  @Override
  public void register() {
    this.eventService.addListener(
        IncomingCreateEntityEvent.type,
        event -> {
          EntityData entityData = (EntityData) event.getData().get("entityData");
          StreamObserver<NetworkObject.CreateNetworkObject> requestObserver =
              (StreamObserver<NetworkObject.CreateNetworkObject>)
                  event.getData().get("requestObserver");
          Entity createEntity = entityFactory.create(entityData);
          entityManager.add(createEntity);
          eventService.fireEvent(new OutgoingCreateEntityEvent(entityData));
        });
    this.eventService.addListener(
        IncomingUpdateEntityEvent.type,
        event -> {
          EntityData entityData = (EntityData) event.getData().get("entityData");
          StreamObserver<NetworkObject.RemoveNetworkObject> requestObserver =
              (StreamObserver<NetworkObject.RemoveNetworkObject>)
                  event.getData().get("requestObserver");
          UUID targetUuid = UUID.fromString(entityData.getID());
          Entity target = entityManager.get(targetUuid);
          if (target == null) {
            return;
          }
          target.fromEntityData(entityData);
          eventService.fireEvent(new OutgoingUpdateEntityEvent(entityData));
        });
    this.eventService.addListener(
        IncomingRemoveEntityEvent.type,
        event -> {
          EntityData entityData = (EntityData) event.getData().get("entityData");
          StreamObserver<NetworkObject.RemoveNetworkObject> requestObserver =
              (StreamObserver<NetworkObject.RemoveNetworkObject>)
                  event.getData().get("requestObserver");
          entityManager.remove(entityData.getID());
          eventService.fireEvent(new OutgoingRemoveEntityEvent(entityData));
        });
    this.eventService.addListener(
        IncomingDisconnectEvent.type,
        event -> {
          StreamObserver requestObserver = (StreamObserver) event.getData().get("requestObserver");
          AbtractConnection connection = connectionStore.get(requestObserver);
          connectionStore.remove(connection.id);
        });
    this.eventService.addListener(
        OutgoingCreateEntityEvent.type,
        event -> {
          EntityData entityData = (EntityData) event.getData().get("entityData");
          connectionStore
              .getAll(CreateConnection.class)
              .forEach(
                  createConnection -> {
                    System.out.println("send");
                    createConnection.requestObserver.onNext(
                        networkObjectFactory.createNetworkObject(entityData));
                  });
        });
    this.eventService.addListener(
        OutgoingUpdateEntityEvent.type,
        event -> {
          EntityData entityData = (EntityData) event.getData().get("entityData");
          synchronized (this) {
            connectionStore
                .getAll(UpdateConnection.class)
                .forEach(
                    updateConnection -> {
                      updateConnection.requestObserver.onNext(
                          networkObjectFactory.updateNetworkObject(entityData));
                    });
          }
        });
    this.eventService.addListener(
        OutgoingRemoveEntityEvent.type,
        event -> {
          EntityData entityData = (EntityData) event.getData().get("entityData");
          connectionStore
              .getAll(RemoveConnection.class)
              .forEach(
                  removeConnection -> {
                    removeConnection.requestObserver.onNext(
                        networkObjectFactory.removeNetworkObject(entityData));
                  });
        });
  }
}
