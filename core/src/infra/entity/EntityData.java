package infra.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.HashMap;
import java.util.UUID;

public class EntityData {
    public HashMap<String, String> data;

    public EntityData() {
        this.data = new HashMap<>();
    }

    public EntityData(HashMap<String, String> data) {
        this.data = data;
    }

    void setImgPath(String texture) {
        this.data.put("img", texture);
    }

    String getImgPath() {
        return this.data.get("img");
    }

    public void setId(String id) {
        this.data.put("id", id);
    }

    public String getID() {
        return this.data.get("id");
    }

    public void setX(String x) {
        this.data.put("x", x);
    }

    public String getX() {
        return this.data.get("x");
    }

    public void setY(String y) {
        this.data.put("y", y);
    }

    public String getY() {
        return this.data.get("y");
    }

    public void setOwner(String y) {
        this.data.put("owner", y);
    }

    public String getOwner() {
        return this.data.get("owner");
    }

    HashMap<String, String> getData() {
        return this.data;
    }

    void merge(EntityData data) {
        this.data.putAll(data.getData());
    }

    void merge(HashMap<String, String> merge) {
        this.data.putAll(merge);
    }

    public void setItem(String key, String value) {
        this.data.put(key, value);
    }

    public static void main(String[] args) {
        EntityData data = new EntityData();
        data.data.put("id", UUID.randomUUID().toString());
        System.out.println(data.getID());
    }
}
