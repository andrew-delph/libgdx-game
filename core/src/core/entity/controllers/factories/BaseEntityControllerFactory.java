package core.entity.controllers.factories;

import com.google.inject.Inject;
import core.entity.Entity;
import core.entity.controllers.EntityController;
import core.entity.controllers.events.consumers.ChangedHealthConsumer;
import core.entity.controllers.events.consumers.FallDamageConsumer;
import core.entity.controllers.events.types.ChangeHealthEventType;
import core.entity.controllers.events.types.FallDamageEventType;

public class BaseEntityControllerFactory extends EntityControllerFactory {
  @Inject FallDamageConsumer fallDamageConsumer;
  @Inject ChangedHealthConsumer changeHealthConsumer;

  @Override
  public EntityController createEntityUserController(Entity entity) {
    return super.createEntityUserController(entity)
        .registerEntityEventConsumer(FallDamageEventType.type, fallDamageConsumer)
        .registerEntityEventConsumer(ChangeHealthEventType.type, changeHealthConsumer);
  }

  @Override
  public EntityController createEntityPathController(Entity source, Entity target) {
    return super.createEntityPathController(source, target)
        .registerEntityEventConsumer(FallDamageEventType.type, fallDamageConsumer)
        .registerEntityEventConsumer(ChangeHealthEventType.type, changeHealthConsumer);
  }

  @Override
  public EntityController createSolidBlockController(Entity entity) {
    return super.createSolidBlockController(entity)
        .registerEntityEventConsumer(ChangeHealthEventType.type, changeHealthConsumer);
  }

  @Override
  public EntityController createRemoteBodyController(Entity entity) {
    return super.createRemoteBodyController(entity)
        .registerEntityEventConsumer(FallDamageEventType.type, fallDamageConsumer)
        .registerEntityEventConsumer(ChangeHealthEventType.type, changeHealthConsumer);
  }
}
