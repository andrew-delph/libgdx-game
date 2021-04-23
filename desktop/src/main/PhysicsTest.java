package main;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.google.inject.Guice;
import com.google.inject.Injector;
import configure.DesktopApp;
import game.GameClient;

public class PhysicsTest extends ApplicationAdapter {
    SpriteBatch batch;
    Sprite sprite;
    Texture img;
    World world;
    Body body;
    @Override
    public void create() {
        batch = new SpriteBatch();
        // We will use the default LibGdx logo for this example, but we need a
        // sprite since it's going to move
        img = new Texture("badlogic.jpg");
        sprite = new Sprite(img);
        // Center the sprite in the top/middle of the screen
        sprite.setPosition(Gdx.graphics.getWidth() / 2 - sprite.getWidth() / 2,
                Gdx.graphics.getHeight() / 2);
        // Create a physics world, the heart of the simulation.  The Vector
        //passed in is gravity
        world = new World(new Vector2(0, -98f), true);
        // Now create a BodyDefinition.  This defines the physics objects type
        //and position in the simulation
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        // We are going to use 1 to 1 dimensions.  Meaning 1 in physics engine
        //is 1 pixel
        // Set our body to the same position as our sprite
        bodyDef.position.set(sprite.getX(), sprite.getY());
        // Create a body in the world using our definition
        body = world.createBody(bodyDef);
        // Now define the dimensions of the physics shape
        PolygonShape shape = new PolygonShape();
        // We are a box, so this makes sense, no?
        // Basically set the physics polygon to a box with the same dimensions
        //as our sprite
        shape.setAsBox(sprite.getWidth()/2, sprite.getHeight()/2);
        // FixtureDef is a confusing expression for physical properties
        // Basically this is where you, in addition to defining the shape of the
        // body
        // you also define it's properties like density, restitution and others
        // we will see shortly
        // If you are wondering, density and area are used to calculate over all
        // mass
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        Fixture fixture = body.createFixture(fixtureDef);
        // Shape is the only disposable of the lot, so get rid of it
        shape.dispose();
    }
    @Override
    public void render() {
        // Advance the world, by the amount of time that has elapsed since the
        // last frame
        // Generally in a real game, dont do this in the render loop, as you are
        // tying the physics
        // update rate to the frame rate, and vice versa
        world.step(Gdx.graphics.getDeltaTime(), 1, 20);
        // Now update the spritee position accordingly to it's now updated
        // Physics body
        sprite.setPosition(body.getPosition().x, body.getPosition().y);
        // You know the rest...
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(sprite, sprite.getX(), sprite.getY());
        batch.end();
    }
    @Override
    public void dispose() {
        // Hey, I actually did some clean up in a code sample!
        img.dispose();
        world.dispose();
    }

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        new LwjglApplication(new PhysicsTest(), config);
    }
}
