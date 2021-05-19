package infra.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import infra.serialization.SerializationItem;
import networking.NetworkObject;

import java.util.UUID;

public class Entity implements SerializationItem {
    public UUID uuid;
    private int zindex;
    public Controller controller;
    public Animation animation;
    public Sprite sprite;
    public Body body;



    public Entity(){

    }

    public void setZindex(int zindex){
        this.zindex =zindex;
    }

    public int getUpdateTimeout(){
        return 1;
    }

    @Override
    public NetworkObject getNetworkObject() {
        return null;
    }
}
