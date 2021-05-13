package networking.client.observers;

import com.google.inject.Inject;
import infra.entity.EntityFactory;
import infra.entity.EntityManager;
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
