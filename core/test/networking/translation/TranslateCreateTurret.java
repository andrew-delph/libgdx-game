package networking.translation;

import common.events.types.CreateTurretEventType;
import common.exceptions.SerializationDataMissing;
import entity.attributes.msc.Coordinates;
import java.util.UUID;
import networking.events.EventTypeFactory;
import org.junit.Test;

public class TranslateCreateTurret {

  @Test
  public void testTranslateCreateEntityEvent() throws SerializationDataMissing {
    Coordinates coordinates = new Coordinates(0, 1);
    UUID uuid = UUID.randomUUID();

    CreateTurretEventType outgoing = EventTypeFactory.createTurretEventType(uuid, coordinates);
    CreateTurretEventType incoming =
        NetworkDataDeserializer.createCreateTurretEventType(
            NetworkDataSerializer.serializeCreateTurretEventType(outgoing));

    assert incoming.getCoordinates().equals(outgoing.getCoordinates());
    assert incoming.getEntityUUID().equals(outgoing.getEntityUUID());
  }
}
