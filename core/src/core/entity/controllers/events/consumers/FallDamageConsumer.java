package core.entity.controllers.events.consumers;

import com.google.inject.Inject;
import core.app.game.GameController;
import core.common.exceptions.EntityNotFound;
import core.entity.controllers.events.types.AbstractEntityEventType;
import core.entity.controllers.events.types.FallDamageEventType;

public class FallDamageConsumer implements EntityEventConsumer {

  @Inject GameController gameController;

  @Override
  public void accept(AbstractEntityEventType entityEvent) {
    FallDamageEventType event = (FallDamageEventType) entityEvent;

    int diff = event.getLast_position().getY() - event.getNew_position().getY();

    if (diff > 5) {
      try {
        gameController.updateEntityAttribute(
            event.getEntity().getUuid(), event.getEntity().health.applyDiff(-100));
      } catch (EntityNotFound e) {
        e.printStackTrace();
      }
    }
  }
}
