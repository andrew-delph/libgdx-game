package core.common.events;

import com.badlogic.gdx.Gdx;
import com.google.inject.Inject;
import core.app.user.User;
import core.common.GameSettings;
import core.common.events.types.CreateAIEntityEventType;
import core.common.events.types.CreateTurretEventType;
import core.common.events.types.ItemActionEventType;
import core.common.exceptions.ChunkNotFound;
import core.common.exceptions.EntityNotFound;
import core.entity.AIManager;
import core.entity.Entity;
import core.entity.attributes.inventory.item.comsumers.ItemActionService;
import core.entity.misc.Turret;



public class SoloEventConsumer extends EventConsumer {


  @Inject ItemActionService itemActionService;
  @Inject AIManager aiManager;
  @Inject User user;

  @Override
  public void init() {
    super.init();

    this.eventService.addPostUpdateListener(
        CreateAIEntityEventType.type,
        eventType -> {
          try {
            CreateAIEntityEventType realEvent = (CreateAIEntityEventType) eventType;
            Gdx.app.log(GameSettings.LOG_TAG,"CREATE AI " + realEvent.getCoordinates());

            aiManager.requestCreateAI(
                user.getUserID(), realEvent.getCoordinates(), realEvent.getTarget());

          } catch (EntityNotFound e) {
            e.printStackTrace();
          } catch (ChunkNotFound e) {
            e.printStackTrace();
          }
        });
    this.eventService.addPostUpdateListener(
        CreateTurretEventType.type,
        event -> {
          CreateTurretEventType realEvent = (CreateTurretEventType) event;
          try {
            Entity entity = gameStore.getEntity(realEvent.getEntityUUID());
            Turret turret = gameController.createTurret(entity, realEvent.getCoordinates());
            if (turret != null)
              activeEntityManager.registerActiveEntity(user.getUserID(), turret.getUuid());
          } catch (ChunkNotFound | EntityNotFound e) {
              Gdx.app.error(GameSettings.LOG_TAG,e.getMessage(), e);
          }
        });

    this.eventService.addPostUpdateListener(
        ItemActionEventType.type,
        event -> {
          ItemActionEventType realEvent = (ItemActionEventType) event;
          try {
            Boolean gcd = itemActionService.checkTriggerGCD(realEvent.getControleeUUID());
            if (!gcd) return;
            itemActionService.use(realEvent.getItemActionType(), realEvent.getControleeUUID());
          } catch (EntityNotFound e) {
              Gdx.app.error(GameSettings.LOG_TAG,e.getMessage(), e);
          }
        });
  }
}
