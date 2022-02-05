package entity.collision.left;

import com.badlogic.gdx.physics.box2d.Body;

public class LeftSensorPoint {

    private final Body body;

    public LeftSensorPoint(Body body) {
        this.body = body;
    }

    public Body getBody() {
        return body;
    }

}
