package game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.inject.Inject;
import infra.entity.Entity;
import infra.entity.EntityData;
import infra.entity.EntityManager;
import infra.entity.factories.EntityFactory;
import networking.NetworkObject;
import networking.client.ClientNetworkHandle;

import java.util.function.Consumer;

public class GameClient extends ApplicationAdapter {
    SpriteBatch batch;
    Entity player;

    ClientNetworkHandle client;

    Texture image;

    @Inject
    public GameClient(ClientNetworkHandle client){
        this.client = client;
    }

    @Override
    public void create() {
        this.image = new Texture("badlogic.jpg");
        this.player = EntityFactory.getInstance().createBasic();
        this.batch = new SpriteBatch();
        this.client.entityManager.add(this.player);
        this.client.connect();
        NetworkObject.NetworkObjectItem networkObjectItem_x = NetworkObject.NetworkObjectItem.newBuilder().setKey("x").setValue((this.player.getEntityData().getX() + "")).build();
        NetworkObject.NetworkObjectItem networkObjectItem_y = NetworkObject.NetworkObjectItem.newBuilder().setKey("y").setValue((this.player.getEntityData().getY() + "")).build();
        NetworkObject.CreateNetworkObject createRequestObject = NetworkObject.CreateNetworkObject.newBuilder().setId(this.player.getEntityData().getID()).addItem(networkObjectItem_x).addItem(networkObjectItem_y).build();
        this.client.createRequest.onNext(createRequestObject);
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render() {
        this.handleInput();
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        Consumer<Entity> renderConsumer = e -> batch.draw(this.image, e.getX(), e.getY());
        this.client.entityManager.update(renderConsumer);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
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
        NetworkObject.NetworkObjectItem networkObjectItem_x = NetworkObject.NetworkObjectItem.newBuilder().setKey("x").setValue((this.player.getEntityData().getX() + "")).build();
        NetworkObject.NetworkObjectItem networkObjectItem_y = NetworkObject.NetworkObjectItem.newBuilder().setKey("y").setValue((this.player.getEntityData().getY() + "")).build();
        NetworkObject.UpdateNetworkObject updateRequestObject = NetworkObject.UpdateNetworkObject.newBuilder().setId(this.player.getEntityData().getID().toString()).addItem(networkObjectItem_x).addItem(networkObjectItem_y).build();
        this.client.updateRequest.onNext(updateRequestObject);
    }
}
