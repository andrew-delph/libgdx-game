package core.entity.attributes;

import core.networking.events.interfaces.SerializeNetworkData;

public interface Attribute extends SerializeNetworkData {
  AttributeType getType();
}
