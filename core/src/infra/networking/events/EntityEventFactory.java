package infra.networking.events;

import infra.networking.NetworkObjects;

public interface EntityEventFactory {
  CreateEntityOutgoingEvent createCreateEntityOutgoingEvent(NetworkObjects.NetworkData entityData);

  CreateEntityIncomingEvent createCreateEntityIncomingEvent(NetworkObjects.NetworkData entityData);

  UpdateEntityOutgoingEvent createUpdateEntityOutgoingEvent(NetworkObjects.NetworkData entityData);

  UpdateEntityIncomingEvent createUpdateEntityIncomingEvent(NetworkObjects.NetworkData entityData);
}
