package infra.common.events;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import infra.common.Coordinates;

public class CoordinatesEvent extends Event {

  public static String type = "coordinates_event";

  Coordinates coordinates;

  @Inject
  public CoordinatesEvent(@Assisted Coordinates coordinates) {
    this.coordinates = coordinates;
  }

  @Override
  public String getType() {
    return type;
  }
}
