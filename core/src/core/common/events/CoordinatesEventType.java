package core.common.events;

import com.google.inject.Inject;
import core.common.events.types.EventType;
import core.entity.attributes.msc.Coordinates;

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
