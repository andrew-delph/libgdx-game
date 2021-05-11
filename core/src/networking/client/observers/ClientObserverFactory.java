package networking.client.observers;

import com.google.inject.Inject;
import infra.entity.EntityManager;
import infra.entity.EntityFactory;
import infra.events.EventService;
import networking.connection.ConnectionStore;

public class ClientObserverFactory {

  EntityManager entityManager;
  ConnectionStore connectionStore;
  EntityFactory entityFactory;
  EventService eventService;

  @Inject
  ClientObserverFactory(
      EntityManager entityManager,
      ConnectionStore connectionStore,
      EntityFactory entityFactory,
      EventService eventService) {
    this.entityManager = entityManager;
    this.connectionStore = connectionStore;
    this.entityFactory = entityFactory;
    this.eventService = eventService;
  }

  public networking.client.observers.CreateObserver createCreateObserver() {
    return new CreateObserver(
        this.entityManager, this.connectionStore, this.entityFactory, this.eventService);
  }

  public networking.client.observers.UpdateObserver createUpdateObserver() {
    return new UpdateObserver(this.entityManager, this.connectionStore, this.eventService);
  }

  public networking.client.observers.RemoveObserver createRemoveObserver() {
    return new RemoveObserver(this.entityManager, this.connectionStore, this.eventService);
  }
}
