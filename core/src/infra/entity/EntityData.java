package infra.entity;

import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;
import java.util.UUID;

public class EntityData{
     private HashMap<String, Object> data;

    public EntityData() {
        this.data = new HashMap<>();
    }
    void setImg(Texture texture){
        this.data.put("img", texture);
    }
    Texture getImg(){
        return (Texture)this.data.get("img");
    }

    public void setId(UUID id){
        this.data.put("id", id);
    }
    public UUID getID(){
        return (UUID) this.data.get("id");
    }

    void setX(int x){
        this.data.put("x", x);
    }
    int getX(){
        return (int) this.data.get("x");
    }

    void setY(int y){
        this.data.put("y", y);
    }
    int getY(){
        return (int) this.data.get("y");
    }

    HashMap<String, Object> getData(){
        return this.data;
    }

    void merge(EntityData data){
        this.data.putAll(data.getData());
    }
}
