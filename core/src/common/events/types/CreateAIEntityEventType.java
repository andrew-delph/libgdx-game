package common.events.types;

import common.Coordinates;

public class CreateAIEntityEventType extends EventType {

  public static String type = "create_ai";

  Coordinates coordinates;

  public CreateAIEntityEventType(Coordinates coordinates) {
    this.coordinates = coordinates;
  }

  @Override
  public String getType() {
    return type;
  }
}
