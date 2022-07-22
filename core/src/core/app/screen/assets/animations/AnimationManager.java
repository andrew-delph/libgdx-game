package core.app.screen.assets.animations;

import com.google.inject.Inject;
import core.entity.Entity;
import core.entity.misc.Orb;
import java.util.HashMap;
import java.util.Map;

public class AnimationManager {

  @Inject GameAnimationFactory gameAnimationFactory;

  Map<Class, GameAnimation> animationCollectionMap = new HashMap<>();

  public void init() {
    animationCollectionMap.put(Entity.class, gameAnimationFactory.createEntityAnimation());
    animationCollectionMap.put(Orb.class, gameAnimationFactory.createOrbAnimation());
  }

  public GameAnimation getGameAnimation(Class class_type) {
    return animationCollectionMap.get(class_type);
  }
}
