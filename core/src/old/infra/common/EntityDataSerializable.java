package old.infra.common;

import old.infra.entitydata.EntityData;

public interface EntityDataSerializable {
  EntityData toEntityData();

  void fromEntityData(EntityData entityData);
}
