package entity.controllers.events.consumers;

import app.game.GameController;
import com.google.inject.Inject;
import entity.controllers.events.types.AbstractEntityEventType;
import entity.controllers.events.types.ChangeHealthEventType;

public class ChangedHealthConsumer implements EntityEventConsumer {

  @Inject GameController gameController;

  @Override
  public void accept(AbstractEntityEventType entityEvent) {

    ChangeHealthEventType healthEvent = (ChangeHealthEventType) entityEvent;

    if (healthEvent.getEntity().getHealth().getValue() <= 0) {
      gameController.removeEntity(healthEvent.getEntity().getUuid());
    }
  }
}
