package entity.attributes;

import networking.events.interfaces.SerializeNetworkData;

public interface Attribute extends SerializeNetworkData {
  AttributeType getType();
}
