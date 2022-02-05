package entity.collision.right;

import com.badlogic.gdx.physics.box2d.Body;

public class RightSensorPoint {

    private final Body body;

    public RightSensorPoint(Body body) {
        this.body = body;
    }

    public Body getBody() {
        return body;
    }

}
