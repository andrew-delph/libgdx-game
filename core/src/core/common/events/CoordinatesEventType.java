package core.common.events;

import com.google.inject.Inject;
import core.common.Coordinates;
import core.common.events.types.EventType;

public class CoordinatesEventType extends EventType {

  public static String type = "coordinates_event";

  Coordinates coordinates;

  @Inject
  public CoordinatesEventType(Coordinates coordinates) {
    this.coordinates = coordinates;
  }

  @Override
  public String getEventType() {
    return type;
  }
}
