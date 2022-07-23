package core.entity.attributes.msc;

import com.google.common.base.Objects;
import core.entity.attributes.Attribute;
import core.entity.attributes.AttributeType;
import networking.NetworkObjects.NetworkData;

public class CoordinatesWrapper implements Attribute {

  final Coordinates coordinates;

  public CoordinatesWrapper(Coordinates coordinates) {
    this.coordinates = coordinates;
  }

  public Coordinates getCoordinates() {
    return coordinates;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CoordinatesWrapper that = (CoordinatesWrapper) o;
    return Objects.equal(coordinates, that.coordinates);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(coordinates);
  }

  @Override
  public AttributeType getType() {
    return AttributeType.COORDINATES;
  }

  @Override
  public NetworkData toNetworkData() {
    return this.getCoordinates().toNetworkData();
  }
}
