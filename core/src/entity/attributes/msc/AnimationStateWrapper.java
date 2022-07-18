package entity.attributes.msc;

import app.screen.assets.animations.AnimationState;
import com.google.common.base.Objects;
import entity.attributes.Attribute;
import entity.attributes.AttributeType;
import networking.NetworkObjects.NetworkData;
import networking.translation.NetworkDataSerializer;

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
