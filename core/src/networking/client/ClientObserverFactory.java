package networking.client;

import com.google.inject.Inject;
import infra.entity.EntityManager;
import infra.entity.factories.EntityFactory;
import networking.client.observers.CreateObserver;
import networking.client.observers.RemoveObserver;
import networking.client.observers.UpdateObserver;

public class ClientObserverFactory {

    EntityManager entityManager;
    EntityFactory entityFactory;

    @Inject
    ClientObserverFactory(EntityManager entityManager, EntityFactory entityFactory){
        this.entityManager = entityManager;
        this.entityFactory = entityFactory;
    }

    public CreateObserver createCreateObserver(){
        return new CreateObserver(this.entityManager, this.entityFactory);
    }

    public UpdateObserver createUpdateObserver(){
        return new UpdateObserver(this.entityManager);
    }

    public RemoveObserver createRemoveObserver(){
        return new RemoveObserver(this.entityManager);
    }
}
