package entity;

import configuration.GameSettings;
import app.render.BaseAssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import common.Clock;
import common.Coordinates;
import entity.controllers.EntityController;
import networking.NetworkObjects;
import networking.events.interfaces.SerializeNetworkData;

import java.util.UUID;

public class Entity implements SerializeNetworkData {
    public static int coordinatesScale = GameSettings.COORDINATES_SCALE;
    public static int staticHeight = (int) (Entity.coordinatesScale * 0.8);
    public static int staticWidth = (int) (Entity.coordinatesScale * 0.8);
    public UUID uuid;
    public EntityController entityController;
    public Animation animation;
    public Sprite sprite;
    public Body body;
    public Coordinates coordinates;
    public int zindex = 1;
    public String textureName = "frog.png";
    public EntityBodyBuilder entityBodyBuilder;
    Clock clock;
    BaseAssetManager baseAssetManager;
    private int width;
    private int height;

    public Entity(
            Clock clock, BaseAssetManager baseAssetManager, EntityBodyBuilder entityBodyBuilder) {
        this.setHeight(Entity.staticHeight);
        this.setWidth(Entity.staticWidth);
        this.clock = clock;
        this.baseAssetManager = baseAssetManager;
        this.entityBodyBuilder = entityBodyBuilder;
        this.sprite = new Sprite();
        this.sprite.setPosition(0, 0);
        this.sprite.setSize(width, height);
        this.coordinates = new Coordinates(0, 0);
        this.uuid = UUID.randomUUID();
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public synchronized Body addWorld(World world) {
        return EntityBodyBuilder.createEntityBody(world, this.coordinates);
    }

    public synchronized void setController(EntityController entityController) {
        this.entityController = entityController;
    }

    public synchronized void renderSync() {
        this.sprite = new Sprite((Texture) baseAssetManager.get(this.textureName));
        this.sprite.setSize(this.getWidth(), this.getHeight());
        this.sprite.setPosition(
                this.coordinates.getXReal() * coordinatesScale,
                this.coordinates.getYReal() * coordinatesScale);
    }

    public void syncPosition() {
    }

    public synchronized void setZindex(int zindex) {
        this.zindex = zindex;
    }

    public synchronized int getUpdateTimeout() {
        return this.clock.currentTick.time + 1;
    }

    public NetworkObjects.NetworkData toNetworkData() {
        NetworkObjects.NetworkData uuid =
                NetworkObjects.NetworkData.newBuilder()
                        .setKey(UUID.class.getName())
                        .setValue(this.uuid.toString())
                        .build();
        return NetworkObjects.NetworkData.newBuilder()
                .setKey("class")
                .setValue(this.getClass().getName())
                .addChildren(this.coordinates.toNetworkData())
                .addChildren(uuid)
                .build();
    }

    @Override
    public int hashCode() {
        return (this.uuid).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Entity other = (Entity) obj;
        return this.uuid.equals(other.uuid) && this.coordinates.equals(other.coordinates);
    }

    public Coordinates getCenter() {
        return new Coordinates(this.coordinates.getXReal() + 0.5f, this.coordinates.getYReal() + 0.5f);
    }
}
