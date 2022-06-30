package entity.attributes.inventory.item.comsumers;

import com.google.inject.Inject;
import common.GameStore;
import common.exceptions.EntityNotFound;
import entity.Entity;
import entity.attributes.inventory.item.ItemActionType;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ItemActionService {

  @Inject GameStore gameStore;
  @Inject DefaultItemAction defaultItemAction;

  Map<ItemActionType, ItemActionInterface> consumerMap = new HashMap<>();

  public void registerAttributeAction(
      ItemActionType actionType, ItemActionInterface actionConsumer) {
    consumerMap.put(actionType, actionConsumer);
  }

  public void use(ItemActionType actionType, UUID controleeUUID) throws EntityNotFound {
    Entity controlee = gameStore.getEntity(controleeUUID);
    if (consumerMap.containsKey(actionType)) {
      consumerMap.get(actionType).use(controlee);
    } else {
      defaultItemAction.use(controlee);
    }
  }
}
