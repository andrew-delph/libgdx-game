package infra.common;

import infra.entity.EntityData;

public interface EntityDataSerializable {
    EntityData toEntityData();
    void fromEntityData(EntityData entityData);
}
