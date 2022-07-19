package core.networking.translation;

import core.common.events.types.ItemActionEventType;
import core.common.exceptions.SerializationDataMissing;
import core.entity.attributes.inventory.item.ItemActionType;
import core.networking.translation.NetworkDataDeserializer;
import java.util.UUID;
import org.junit.Test;

public class TranslateItemActionType {

  @Test
  public void testTranslateUpdateEntityEvent() throws SerializationDataMissing {
    UUID uuid = UUID.randomUUID();
    ItemActionEventType outgoing = new ItemActionEventType(ItemActionType.DEFAULT, uuid);

    ItemActionEventType incoming =
        NetworkDataDeserializer.createItemActionEventType(outgoing.toNetworkEvent());

    assert incoming.getItemActionType().equals(outgoing.getItemActionType());
    assert incoming.getControleeUUID().equals(outgoing.getControleeUUID());
  }
}
