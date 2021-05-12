package game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.inject.Inject;
import infra.entity.Entity;
import infra.entity.EntityFactory;
import infra.events.Event;
import infra.events.EventService;
import networking.NetworkObjectFactory;
import networking.client.ClientNetworkHandle;
import networking.events.incoming.IncomingDisconnectEvent;
import networking.events.outgoing.OutgoingCreateEntityEvent;
import networking.events.outgoing.OutgoingRemoveEntityEvent;
import networking.events.outgoing.OutgoingUpdateEntityEvent;

import java.util.function.Consumer;

public class GameClient extends ApplicationAdapter {
  SpriteBatch batch;
  Entity player;

  ClientNetworkHandle client;

  Texture image;

  @Inject EntityFactory entityFactory;

  @Inject NetworkObjectFactory networkObjectFactory;

  @Inject EventService eventService;

  @Inject
  public GameClient(ClientNetworkHandle client) {
    this.client = client;
  }

  @Override
  public void create() {
    client.connect();
    this.image = new Texture("badlogic.jpg");
    this.player = entityFactory.createBasic();
    this.batch = new SpriteBatch();
    this.client.entityManager.add(this.player);
    this.eventService.fireEvent(new OutgoingCreateEntityEvent(this.player.toEntityData()));

    this.eventService.addListener(
        IncomingDisconnectEvent.type,
        new Consumer<Event>() {
          @Override
          public void accept(Event event) {
            System.out.println("disconnect event");
            System.exit(0);
          }
        });
  }

  @Override
  public void render() {
    this.handleInput();
    Gdx.gl.glClearColor(1, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    batch.begin();
    Consumer<Entity> renderConsumer =
        e -> {
          batch.draw(this.image, e.getX(), e.getY());
        };
    this.client.entityManager.update(renderConsumer);
    batch.end();
  }

  @Override
  public void dispose() {
    batch.dispose();
    this.eventService.fireEvent(new OutgoingRemoveEntityEvent(this.player.toEntityData()));
    this.client.disconnect();
    System.out.println("andrew dispose.");
  }

  private void handleInput() {
    if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
      this.player.moveX(-1);
    }
    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
      this.player.moveX(+1);
    }
    if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
      this.player.moveY(-1);
    }
    if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
      this.player.moveY(+1);
    }
    this.eventService.fireEvent(new OutgoingUpdateEntityEvent(this.player.toEntityData()));
  }
}
