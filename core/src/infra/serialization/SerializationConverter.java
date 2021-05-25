package infra.serialization;

import com.google.inject.Inject;
import infra.common.networkobject.Coordinates;
import infra.entity.Entity;
import infra.entity.EntityFactory;
import infra.networking.NetworkObjects;

import java.util.UUID;

public class SerializationConverter {
    @Inject
    EntityFactory entityFactory;

    Entity createEntity(NetworkObjects.NetworkData networkData){
        Entity entity = entityFactory.createEntity();
        entity.uuid = UUID.fromString(networkData.getValue());
        for(NetworkObjects.NetworkData networkDataChild : networkData.getChildrenList()){
            if (networkDataChild.getKey() == Coordinates.class.getName()){
                entity.coordinates = this.createCoordinates(networkDataChild);
            }
        }
        return entity;
    }

    Coordinates createCoordinates(NetworkObjects.NetworkData networkData){
        float x = 0,y = 0;
        for (NetworkObjects.NetworkData value : networkData.getChildrenList()) {
            switch (value.getKey()){
                case "x":
                    x = Float.parseFloat(value.getValue());
                    break;
                case "y":
                    y = Float.parseFloat(value.getValue());
                    break;
            }
        }
        Coordinates coordinates = new Coordinates(x,y);
        return coordinates;
    }
}
