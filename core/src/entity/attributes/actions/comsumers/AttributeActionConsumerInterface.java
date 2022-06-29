package entity.attributes.actions.comsumers;

import entity.Entity;
import entity.attributes.actions.AttributeActionType;

public interface AttributeActionConsumerInterface {
  AttributeActionType getAttributeActionType();

  void use(Entity controlee);
}
