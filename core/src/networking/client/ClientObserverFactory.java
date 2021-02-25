package networking.client;

import com.google.inject.Inject;
import infra.entity.EntityManager;
import networking.client.observers.CreateObserver;
import networking.client.observers.RemoveObserver;
import networking.client.observers.UpdateObserver;

public class ClientObserverFactory {

    EntityManager entityManager;

    @Inject
    ClientObserverFactory(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public CreateObserver createCreateObserver(){
        return new CreateObserver(this.entityManager);
    }

    public UpdateObserver createUpdateObserver(){
        return new UpdateObserver(this.entityManager);
    }

    public RemoveObserver createRemoveObserver(){
        return new RemoveObserver(this.entityManager);
    }
}
