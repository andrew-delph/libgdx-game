package core.entity.statemachine.states;

import com.google.inject.Inject;
import core.app.screen.assets.animations.AnimationState;
import core.common.Coordinates;
import core.common.exceptions.EntityNotFound;
import core.entity.Entity;
import core.entity.attributes.msc.AnimationStateWrapper;
import core.entity.attributes.msc.Health;
import core.entity.block.Block;
import core.entity.block.SolidBlock;
import core.entity.collision.RayCastService;
import core.entity.statemachine.EntityStateMachineNodeInterface;
import java.util.Set;

public class DiggingUpState extends EntityStateMachineNodeInterface {

  @Inject RayCastService rayCastService;

  @Override
  public void callAnimation(Entity entity) {
    if (!entity.getAnimationStateWrapper().getAnimationState().equals(AnimationState.DIGGING_UP)) {
      try {
        gameController.updateEntityAttribute(
            entity.getUuid(), new AnimationStateWrapper(AnimationState.DIGGING_UP));
      } catch (EntityNotFound e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void callAction(Entity entity, long timeInState) {

    if (timeInState < 350) return;
    else entity.getEntityStateMachine().setState(AnimationState.DEFAULT);

    // after time

    Coordinates entityCoordinates = entity.getCoordinatesWrapper().getCoordinates();
    Coordinates targetCoordinates = entityCoordinates.add(0, 1);

    Set<Entity> rayCastSet = rayCastService.rayCast(entityCoordinates, targetCoordinates);

    Block targetBlock =
        (Block) (rayCastSet.stream().filter(e -> e instanceof SolidBlock).findAny()).orElse(null);

    if (targetBlock == null) return;

    Health newHealth = targetBlock.getHealth().applyDiff(-50);

    try {
      gameController.updateEntityAttribute(targetBlock.getUuid(), newHealth);
    } catch (EntityNotFound e) {
      e.printStackTrace();
    }
  }
}
