package infra.common;

import infra.entitydata.EntityData;

public interface EntityDataSerializable {
    EntityData toEntityData();

    void fromEntityData(EntityData entityData);
}
