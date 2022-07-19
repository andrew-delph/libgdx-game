package core.common.events;

import com.google.inject.Inject;
import core.common.events.types.CreateAIEntityEventType;
import core.common.events.types.CreateTurretEventType;
import core.common.events.types.ItemActionEventType;
import core.common.exceptions.ChunkNotFound;
import core.common.exceptions.EntityNotFound;
import core.entity.Entity;
import core.entity.attributes.inventory.item.comsumers.ItemActionService;
import core.entity.controllers.factories.EntityControllerFactory;
import core.entity.groups.Group;
import core.entity.groups.GroupService;
import core.entity.misc.Turret;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SoloEventConsumer extends EventConsumer {

  final Logger LOGGER = LogManager.getLogger();
  @Inject EntityControllerFactory entityControllerFactory;
  @Inject GroupService groupService;
  @Inject ItemActionService itemActionService;

  @Override
  public void init() {
    super.init();

    this.eventService.addPostUpdateListener(
        CreateAIEntityEventType.type,
        eventType -> {
          try {
            CreateAIEntityEventType realEvent = (CreateAIEntityEventType) eventType;
            LOGGER.info("CREATE AI " + realEvent.getCoordinates());

            Entity aiEntity = gameController.createEntity(realEvent.getCoordinates());

            Entity aiTarget = gameStore.getEntity(realEvent.getTarget());

            aiEntity.setEntityController(
                entityControllerFactory.createEntityPathController(aiEntity, aiTarget));

            groupService.registerEntityGroup(aiEntity.getUuid(), Group.AI_GROUP);

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
            LOGGER.error(e, e);
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
            LOGGER.error(e, e);
          }
        });
  }
}