package entity.collision;

import com.google.inject.Inject;

public class EntityContactListenerFactory {
    @Inject
    CollisionService collisionService;

    @Inject
    EntityContactListenerFactory() {
    }

    public EntityContactListener createEntityContactListener() {
        return new EntityContactListener(collisionService);
    }
}
