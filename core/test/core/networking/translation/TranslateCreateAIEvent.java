package core.networking.translation;

import core.common.CommonFactory;
import core.common.Coordinates;
import core.common.events.types.CreateAIEntityEventType;
import core.common.exceptions.SerializationDataMissing;
import core.networking.events.EventTypeFactory;
import java.util.UUID;
import org.junit.Test;

public class TranslateCreateAIEvent {

  @Test
  public void testTranslateCreateEntityEvent() throws SerializationDataMissing {
    Coordinates coordinates = CommonFactory.createCoordinates(0, 1);
    UUID uuid = UUID.randomUUID();

    CreateAIEntityEventType outgoing = EventTypeFactory.createAIEntityEventType(coordinates, uuid);
    CreateAIEntityEventType incoming =
        NetworkDataDeserializer.createCreateAIEntityEventType(
            NetworkDataSerializer.createCreateAIEntityEventType(outgoing));

    assert incoming.getTarget().equals(outgoing.getTarget());
    assert incoming.getCoordinates().equals(outgoing.getCoordinates());
  }
}
