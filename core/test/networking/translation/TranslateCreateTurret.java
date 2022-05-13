package networking.translation;

import common.events.types.CreateTurretEventType;
import common.exceptions.SerializationDataMissing;
import entity.attributes.Coordinates;
import networking.events.EventTypeFactory;
import org.junit.Test;

public class TranslateCreateTurret {

  @Test
  public void testTranslateCreateEntityEvent() throws SerializationDataMissing {
    Coordinates coordinates = new Coordinates(0, 1);

    CreateTurretEventType outgoing = EventTypeFactory.createTurretEventType(coordinates);
    CreateTurretEventType incoming =
        NetworkDataDeserializer.createCreateTurretEventType(
            NetworkDataSerializer.serializeCreateTurretEventType(outgoing));

    assert incoming.getCoordinates().equals(outgoing.getCoordinates());
  }
}
