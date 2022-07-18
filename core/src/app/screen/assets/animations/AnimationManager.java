package app.screen.assets.animations;

import com.google.inject.Inject;
import entity.Entity;
import java.util.HashMap;
import java.util.Map;

public class AnimationManager {

  @Inject GameAnimationFactory gameAnimationFactory;

  Map<Class, GameAnimation> animationCollectionMap = new HashMap<>();

  public void init() {
    animationCollectionMap.put(Entity.class, gameAnimationFactory.createEntityAnimation());
  }

  public GameAnimation getGameAnimation(Class class_type) {
    return animationCollectionMap.get(class_type);
  }
}
