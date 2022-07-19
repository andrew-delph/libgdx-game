package core.entity.attributes.msc;

import core.app.screen.assets.animations.AnimationState;
import com.google.common.base.Objects;
import core.entity.attributes.Attribute;
import core.entity.attributes.AttributeType;
import core.networking.translation.NetworkDataSerializer;
import networking.NetworkObjects.NetworkData;

public class AnimationStateWrapper implements Attribute {

  private final AnimationState animationState;

  public AnimationStateWrapper(AnimationState animationState) {
    this.animationState = animationState;
  }

  public AnimationState getAnimationState() {
    return animationState;
  }

  @Override
  public AttributeType getType() {
    return AttributeType.ANIMATION_STATE;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AnimationStateWrapper that = (AnimationStateWrapper) o;
    return animationState == that.animationState;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(animationState);
  }

  @Override
  public NetworkData toNetworkData() {
    return NetworkDataSerializer.createAnimationStateWrapper(this);
  }
}
