package infra.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.google.inject.Inject;
import infra.common.EntityDataSerializable;
import infra.events.EventService;
import networking.events.outgoing.OutgoingUpdateEntityEvent;

import java.util.UUID;

public class Entity implements EntityDataSerializable {

    public Sprite sprite;
    public int size = 60;
    UUID id;
    UUID owner;

    @Inject
    EventService eventService;

    public Entity(UUID id, float x, float y, UUID owner) {
        this.id =id;
        this.owner = owner;
        this.sprite = new Sprite();
        this.sprite.setPosition(x, y);
        this.sprite.setSize(size, size);
    }

    public Entity(UUID id, float x, float y, UUID owner, Texture texture) {
        this(id,x,y,owner);
        this.sprite.setTexture(texture);
    }

    public Entity(EntityData data) {
        this.fromEntityData(data);
    }

    public void moveX(int move) {
        if (this.sprite != null) {
            this.sprite.setX(this.sprite.getX()+move);
        }
        this.eventService.fireEvent(new OutgoingUpdateEntityEvent(this.toEntityData()));
    }

    public void moveY(int move) {
        if (this.sprite != null) {
            this.sprite.setY(this.sprite.getX()+move);
        }
        this.eventService.fireEvent(new OutgoingUpdateEntityEvent(this.toEntityData()));
    }

    public float getX() {
        return this.sprite.getX();
    }

    public float getY() {
        return this.sprite.getY();
    }

    public UUID getID() {
        return this.id;
    }

    public UUID getOwner() {
        return this.owner;
    }

    @Override
    public EntityData toEntityData() {
        EntityData data = new EntityData();
        data.setId(this.id.toString());
        data.setX(String.valueOf(this.sprite.getX()));
        data.setY(String.valueOf(this.sprite.getY()));
        data.setOwner(this.owner.toString());
        return data;
    }

    @Override
    public void fromEntityData(EntityData entityData) {
        this.sprite.setX(Float.parseFloat(entityData.getX()));
        this.sprite.setY(Float.parseFloat(entityData.getY()));
        this.id = UUID.fromString(entityData.getID());
        this.owner = UUID.fromString(entityData.getOwner());
//        for (String key :entityData.keys()) {
//            switch (key){
////                case
//            }
//        }
    }
}
