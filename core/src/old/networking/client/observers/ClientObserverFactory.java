package old.networking.client.observers;

import com.google.inject.Inject;
import old.infra.entity.EntityFactory;
import old.infra.entity.EntityManager;
import old.infra.events.EventService;
import old.networking.connection.ConnectionStore;

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

  public ClientCreateObserver createCreateObserver() {
    return new ClientCreateObserver(
        this.entityManager, this.connectionStore, this.entityFactory, this.eventService);
  }

  public ClientUpdateObserver createUpdateObserver() {
    return new ClientUpdateObserver(this.entityManager, this.connectionStore, this.eventService);
  }

  public ClientRemoveObserver createRemoveObserver() {
    return new ClientRemoveObserver(this.entityManager, this.connectionStore, this.eventService);
  }
}
