package core.app.screen.assets.animations;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.Map;

public class GameAnimation {
  Map<AnimationState, Animation<TextureRegion>> stateMap;

  public GameAnimation(Map<AnimationState, Animation<TextureRegion>> stateMap) {
    this.stateMap = stateMap;
  }

  public Animation<TextureRegion> getAnimation(AnimationState state) {
    return stateMap.getOrDefault(state, stateMap.get(AnimationState.DEFAULT));
  }
}
