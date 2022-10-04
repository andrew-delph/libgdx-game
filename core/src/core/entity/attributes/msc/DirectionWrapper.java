package core.entity.attributes.msc;

import core.common.Direction;
import core.entity.attributes.Attribute;
import core.entity.attributes.AttributeType;
import core.networking.translation.NetworkDataSerializer;
import networking.NetworkObjects.NetworkData;

public class DirectionWrapper implements Attribute {

  private Direction direction;

  public DirectionWrapper(Direction direction) {
    this.direction = direction;
  }

  public Direction getDirection() {
    return direction;
  }

  public void setDirection(Direction direction) {
    this.direction = direction;
  }

  @Override
  public AttributeType getType() {
    return AttributeType.DIRECTION;
  }

  @Override
  public NetworkData toNetworkData() {
    return NetworkDataSerializer.createDirectionWrapper(this);
  }
}
