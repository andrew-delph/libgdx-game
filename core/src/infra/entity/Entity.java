package infra.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import infra.common.EntityDataSerializable;

import java.util.UUID;

public class Entity implements EntityDataSerializable {

    public Sprite sprite;
    public int size = 60;
    UUID id;
    int x;
    int y;
    UUID owner;

    public Entity(UUID id, int x, int y, UUID owner) {
        this.x = x;
        this.y  =y;
        this.id =id;
        this.owner = owner;
    }

    public Entity(UUID id, int x, int y, UUID owner, Texture texture) {
        this(id,x,y,owner);
        this.sprite = new Sprite(texture);
        this.sprite.setPosition(x, y);
        this.sprite.setSize(size, size);
    }

    public Entity(EntityData data) {
        this.fromEntityData(data);
    }



    public void moveX(int move) {
        this.x = this.x + move;
        if (this.sprite != null) {
            this.sprite.setX(this.x);
        }
    }

    public void moveY(int move) {
        this.y = this.y + move;
        if (this.sprite != null) {
            this.sprite.setY(this.y);
        }
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
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
        data.setX(String.valueOf(this.x));
        data.setY(String.valueOf(this.y));
        data.setOwner(this.owner.toString());
        return data;
    }

    @Override
    public void fromEntityData(EntityData entityData) {
        this.x = Integer.parseInt(entityData.getX());
        this.y =  Integer.parseInt(entityData.getY());
        this.id = UUID.fromString(entityData.getID());
        this.owner = UUID.fromString(entityData.getOwner());
        for (String key :entityData.keys()) {
            switch (key){
//                case
            }
        }
    }
}
