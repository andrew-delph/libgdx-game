package networking.translation;

import common.events.types.ItemActionEventType;
import common.exceptions.SerializationDataMissing;
import entity.attributes.inventory.item.ItemActionType;
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
