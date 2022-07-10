package entity.attributes.inventory.item.comsumers;

import com.google.inject.Inject;
import common.Clock;
import common.GameSettings;
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
  @Inject Clock clock;

  Map<ItemActionType, ItemActionInterface> consumerMap = new HashMap<>();

  Map<UUID, Integer> gcdMap = new HashMap<>();

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

  public boolean checkTriggerGCD(UUID controleeUUID) {
    Integer currentTick = clock.getCurrentTick().time;

    Boolean trigger =
        !gcdMap.containsKey(controleeUUID)
            || gcdMap.get(controleeUUID) < currentTick - GameSettings.GCD_TIMEOUT;

    if (trigger) {
      gcdMap.put(controleeUUID, currentTick);
    }

    return trigger;
  }
}
