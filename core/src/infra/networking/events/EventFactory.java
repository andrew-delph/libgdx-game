package infra.networking.events;

import infra.networking.NetworkObjects;

public interface EventFactory {
  CreateEntityOutgoingEvent createCreateEntityOutgoingEvent(NetworkObjects.NetworkData entityData);

  CreateEntityIncomingEvent createCreateEntityIncomingEvent(NetworkObjects.NetworkData entityData);

  UpdateEntityOutgoingEvent createUpdateEntityOutgoingEvent(NetworkObjects.NetworkData entityData);

  UpdateEntityIncomingEvent createUpdateEntityIncomingEvent(NetworkObjects.NetworkData entityData);

  SubscriptionEvent createSubscriptionEvent(NetworkObjects.NetworkEvent entityData);
}
