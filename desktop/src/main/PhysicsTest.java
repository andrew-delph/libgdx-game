package main;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

//public class PhysicsTest extends ApplicationAdapter {
//    SpriteBatch batch;
//    Sprite sprite;
//    Texture img;
//    World world;
//    Body body;
//    @Override
//    public void create() {
//        batch = new SpriteBatch();
//        // We will use the default LibGdx logo for this example, but we need a
//        // sprite since it's going to move
//        img = new Texture("badlogic.jpg");
//        sprite = new Sprite(img);
//        // Center the sprite in the top/middle of the screen
//        sprite.setPosition(Gdx.graphics.getWidth() / 2 - sprite.getWidth() / 2,
//                Gdx.graphics.getHeight() / 2);
//        // Create a physics world, the heart of the simulation.  The Vector
//        //passed in is gravity
//        world = new World(new Vector2(0, -98f), true);
//        // Now create a BodyDefinition.  This defines the physics objects type
//        //and position in the simulation
//        BodyDef bodyDef = new BodyDef();
//        bodyDef.type = BodyDef.BodyType.DynamicBody;
//        // We are going to use 1 to 1 dimensions.  Meaning 1 in physics engine
//        //is 1 pixel
//        // Set our body to the same position as our sprite
//        bodyDef.position.set(sprite.getX(), sprite.getY());
//        // Create a body in the world using our definition
//        body = world.createBody(bodyDef);
//        // Now define the dimensions of the physics shape
//        PolygonShape shape = new PolygonShape();
//        // We are a box, so this makes sense, no?
//        // Basically set the physics polygon to a box with the same dimensions
//        //as our sprite
//        shape.setAsBox(sprite.getWidth()/2, sprite.getHeight()/2);
//        // FixtureDef is a confusing expression for physical properties
//        // Basically this is where you, in addition to defining the shape of the
//        // body
//        // you also define it's properties like density, restitution and others
//        // we will see shortly
//        // If you are wondering, density and area are used to calculate over all
//        // mass
//        FixtureDef fixtureDef = new FixtureDef();
//        fixtureDef.shape = shape;
//        fixtureDef.density = 3f;
//        Fixture fixture = body.createFixture(fixtureDef);
//        // Shape is the only disposable of the lot, so get rid of it
//        shape.dispose();
//    }
//    @Override
//    public void render() {
//        // Advance the world, by the amount of time that has elapsed since the
//        // last frame
//        // Generally in a real game, dont do this in the render loop, as you are
//        // tying the physics
//        // update rate to the frame rate, and vice versa
//        world.step(Gdx.graphics.getDeltaTime(), 1, 20);
//        // Now update the spritee position accordingly to it's now updated
//        // Physics body
//        sprite.setPosition(body.getPosition().x, body.getPosition().y);
//        // You know the rest...
//        Gdx.gl.glClearColor(1, 1, 1, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        batch.begin();
//        batch.draw(sprite, sprite.getX(), sprite.getY());
//        batch.end();
//    }
//    @Override
//    public void dispose() {
//        // Hey, I actually did some clean up in a code sample!
//        img.dispose();
//        world.dispose();
//    }
//
//    public static void main(String[] arg) {
//        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
//
//        new LwjglApplication(new PhysicsTest(), config);
//    }
//}


public class PhysicsTest extends ApplicationAdapter implements InputProcessor {
    SpriteBatch batch;
    Sprite sprite;
    Texture img;
    World world;
    Body body;
    Box2DDebugRenderer debugRenderer;
    Matrix4 debugMatrix;
    OrthographicCamera camera;


    float torque = 0.0f;
    boolean drawSprite = true;

    final float PIXELS_TO_METERS = 100f;

        public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        new LwjglApplication(new PhysicsTest(), config);
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        sprite = new Sprite(img);
        sprite.setPosition(-sprite.getWidth()/2,-sprite.getHeight()/2);
        world = new World(new Vector2(0, 0f),true);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((sprite.getX() + sprite.getWidth()/2) /
                        PIXELS_TO_METERS,
                (sprite.getY() + sprite.getHeight()/2) / PIXELS_TO_METERS);
        body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth()/2 / PIXELS_TO_METERS, sprite.getHeight()
                /2 / PIXELS_TO_METERS);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.1f;
        body.createFixture(fixtureDef);
        shape.dispose();

        Gdx.input.setInputProcessor(this);

        // Create a Box2DDebugRenderer, this allows us to see the physics
        //simulation controlling the scene
        debugRenderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.
                getHeight());
    }
    private float elapsed = 0;
    @Override
    public void render() {
        camera.update();
        // Step the physics simulation forward at a rate of 60hz
        world.step(1f/60f, 6, 2);

        // Apply torque to the physics body.  At start this is 0 and will do
        //nothing.  Controlled with [] keys
        // Torque is applied per frame instead of just once
        body.applyTorque(torque,true);

        // Set the sprite's position from the updated physics body location
        sprite.setPosition((body.getPosition().x * PIXELS_TO_METERS) - sprite.
                        getWidth()/2 ,
                (body.getPosition().y * PIXELS_TO_METERS) -sprite.getHeight()/2 )
        ;
        // Ditto for rotation
        sprite.setRotation((float)Math.toDegrees(body.getAngle()));
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        // Scale down the sprite batches projection matrix to box2D size
        debugMatrix = batch.getProjectionMatrix().cpy().scale(PIXELS_TO_METERS,
                PIXELS_TO_METERS, 0);

        batch.begin();

        if(drawSprite)
            batch.draw(sprite, sprite.getX(), sprite.getY(),sprite.getOriginX(),
                    sprite.getOriginY(),
                    sprite.getWidth(),sprite.getHeight(),sprite.getScaleX(),sprite.
                            getScaleY(),sprite.getRotation());

        batch.end();

        // Now render the physics world using our scaled down matrix
        // Note, this is strictly optional and is, as the name suggests, just
        //for debugging purposes
        debugRenderer.render(world, debugMatrix);
    }
    @Override
    public void dispose() {
        img.dispose();
        world.dispose();
    }
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }
    @Override
    public boolean keyUp(int keycode) {

        // On right or left arrow set the velocity at a fixed rate in that
        //direction
        if(keycode == Input.Keys.RIGHT)
            body.setLinearVelocity(1f, 0f);
        if(keycode == Input.Keys.LEFT)
            body.setLinearVelocity(-1f,0f);
        if(keycode == Input.Keys.UP)
            body.applyForceToCenter(0f,10f,true);
        if(keycode == Input.Keys.DOWN)
            body.applyForceToCenter(0f, -10f, true);

        // On brackets ( [ ] ) apply torque, either clock or counterclockwise
        if(keycode == Input.Keys.RIGHT_BRACKET)
            torque += 0.1f;
        if(keycode == Input.Keys.LEFT_BRACKET)
            torque -= 0.1f;

        // Remove the torque using backslash /
        if(keycode == Input.Keys.BACKSLASH)
            torque = 0.0f;

        // If user hits spacebar, reset everything back to normal
        if(keycode == Input.Keys.SPACE) {
            body.setLinearVelocity(0f, 0f);
            body.setAngularVelocity(0f);
            torque = 0f;
            sprite.setPosition(0f,0f);
            body.setTransform(0f,0f,0f);
        }

        // The ESC key toggles the visibility of the sprite allow user to see
        //physics debug info
        if(keycode == Input.Keys.ESCAPE)
            drawSprite = !drawSprite;
        return true;
    }
    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    // On touch we apply force from the direction of the users touch.
    // This could result in the object "spinning"
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        body.applyForce(1f,1f,screenX,screenY,true);
        //body.applyTorque(0.4f,true);
        return true;
    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
    }

}
