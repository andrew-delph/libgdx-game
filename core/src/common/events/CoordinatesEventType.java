package common.events;

import com.google.inject.Inject;
import common.events.types.EventType;
import entity.attributes.Coordinates;

public class CoordinatesEventType extends EventType {

  public static String type = "coordinates_event";

  Coordinates coordinates;

  @Inject
  public CoordinatesEventType(Coordinates coordinates) {
    this.coordinates = coordinates;
  }

  @Override
  public String getType() {
    return type;
  }
}
