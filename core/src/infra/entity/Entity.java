package infra.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.google.inject.Inject;
import infra.common.Clock;
import infra.common.networkobject.Coordinates;
import infra.entity.controllers.Controller;
import infra.serialization.SerializationItem;
import networking.NetworkObject;

import java.util.UUID;

public  class Entity implements SerializationItem {
  public UUID uuid;
  public Controller controller;
  public Animation animation;
  public Sprite sprite;
  public Body body;
  public Coordinates coordinates;
  @Inject Clock clock;
  private int zindex;

  @Inject
  public Entity() {
    this.sprite = new Sprite(new Texture("frog.png"));
//    this.sprite = new Sprite();
    this.sprite.setPosition(0, 0);
    this.sprite.setSize(50, 50);
    this.coordinates = new Coordinates(0,0);
    this.uuid = UUID.randomUUID();
    this.controller = new Controller(this);
  }

  public synchronized void setController(Controller controller){
    this.controller = controller;
  }

  public synchronized void sync(){
    this.sprite.setPosition(this.coordinates.getX(), this.coordinates.getY());
  }

  public synchronized void setZindex(int zindex) {
    this.zindex = zindex;
  }

  public synchronized int getUpdateTimeout() {
    return this.clock.currentTick.time + 1;
  }

  @Override
  public NetworkObject getNetworkObject() {
    return null;
  }
}
