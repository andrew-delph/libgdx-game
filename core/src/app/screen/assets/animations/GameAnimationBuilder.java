package app.screen.assets.animations;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.HashMap;
import java.util.Map;

public class GameAnimationBuilder {

  Map<AnimationState, Animation<TextureRegion>> stateMap = new HashMap<>();

  public GameAnimationBuilder addAnimation(
      AnimationState state, Animation<TextureRegion> animation) {
    stateMap.put(state, animation);
    return this;
  }

  public GameAnimation build() {
    return new GameAnimation(this.stateMap);
  }
}
