package infra.entity;

import java.util.UUID;

public class Entity {

    EntityData data;

    public Entity(UUID id, int x, int y, UUID owner) {
        this.data = new EntityData();
        this.data.setId(id.toString());
        this.data.setX(String.valueOf(x));
        this.data.setY(String.valueOf(y));
        this.data.setOwner(owner.toString());
    }

    public Entity(EntityData data) {
        this.data = data;
    }

    public EntityData getEntityData() {
        return this.data;
    }

    public void updateEntityData(EntityData data) {
        this.data.merge(data);
    }

    public void moveX(int move) {
        this.data.setX(String.valueOf((Integer.parseInt(this.data.getX()) + move)));
    }

    public void moveY(int move) {
        this.data.setY(String.valueOf(Integer.parseInt(this.data.getY()) + move));

    }

    public int getX() {
        return Integer.parseInt(this.data.getX());
    }

    public int getY() {
        return Integer.parseInt(this.data.getY());
    }

    public UUID getID() {
        return UUID.fromString(this.data.getID());
    }

    public UUID getOwner() {
        return UUID.fromString(this.data.getOwner());
    }
}
