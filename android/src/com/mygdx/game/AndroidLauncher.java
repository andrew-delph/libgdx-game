package com.mygdx.game;

import android.os.Bundle;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.google.inject.Guice;
import com.google.inject.Injector;

import configuration.SoloConfig;
import configuration.TestConfig;
import infra.app.GameScreen;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		Injector injector = Guice.createInjector(new AbstractModule() {
//			@Override
//			protected void configure() {
//
//			}
//		});
//		initialize(new PhysicsTest(), new AndroidApplicationConfiguration());

//		Channel channel = ManagedChannelBuilder.forAddress("4.tcp.ngrok.io", 15442).usePlaintext().build();
//		NetworkObjectServiceGrpc.NetworkObjectServiceStub asyncStub = NetworkObjectServiceGrpc.newStub(channel);
//		StreamObserver<NetworkObjects.NetworkEvent> sender = asyncStub.networkObjectStream(new StreamObserver<NetworkObjects.NetworkEvent>(){
//			@Override
//			public void onNext(NetworkObjects.NetworkEvent value) {
//				System.out.println(value.getEvent());
//			}
//			@Override
//			public void onError(Throwable t) {
//				System.out.println("error");
//				t.printStackTrace();
//			}
//			@Override
//			public void onCompleted() {
//				System.out.println("complete");
//			}
//		});
//		NetworkObjects.NetworkEvent authenticationEvent =
//				NetworkObjects.NetworkEvent.newBuilder().setEvent("andoid").build();
//		sender.onNext(authenticationEvent);
//		try {
//			TimeUnit.SECONDS.sleep(3);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}

		Injector injector = Guice.createInjector(new SoloConfig());
		GameScreen gameScreen = injector.getInstance(GameScreen.class);
		initialize(gameScreen, new AndroidApplicationConfiguration());

//		Injector injector = Guice.createInjector(new SoloConfig());
//
//		GameScreen gameScreen = injector.getInstance(GameScreen.class);
//		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
//		initialize(gameScreen, config);
	}
}

class PhysicsTest extends ApplicationAdapter implements
		InputProcessor {
	final float PIXELS_TO_METERS = 100f;
	SpriteBatch batch;
	Sprite sprite;
	Texture img;
	World world;
	Body body;
	Body bodyEdgeScreen;
	Box2DDebugRenderer debugRenderer;
	Matrix4 debugMatrix;
	OrthographicCamera camera;
	BitmapFont font;
	float torque = 0.0f;
	boolean drawSprite = true;
	private float elapsed = 0;

	@Override
	public void create() {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		sprite = new Sprite(img);
		sprite.setPosition(-sprite.getWidth() / 2, -sprite.getHeight() / 2);
		world = new World(new Vector2(0, -1f), true);
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(
				(sprite.getX() + sprite.getWidth() / 2) / PIXELS_TO_METERS,
				(sprite.getY() + sprite.getHeight() / 2) / PIXELS_TO_METERS);
		body = world.createBody(bodyDef);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(
				sprite.getWidth() / 2 / PIXELS_TO_METERS, sprite.getHeight() / 2 / PIXELS_TO_METERS);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 0.1f;
		fixtureDef.restitution = 0.5f;
		body.createFixture(fixtureDef);
		body.setGravityScale(1);
		shape.dispose();
		BodyDef bodyDef2 = new BodyDef();
		bodyDef2.type = BodyDef.BodyType.StaticBody;
		float w = Gdx.graphics.getWidth() / PIXELS_TO_METERS;
		// Set the height to just 50 pixels above the bottom of the screen so we can see the edge in the
		// debug renderer
		float h = Gdx.graphics.getHeight() / PIXELS_TO_METERS - 50 / PIXELS_TO_METERS;
		// bodyDef2.position.set(0,
		//                h-10/PIXELS_TO_METERS);
		bodyDef2.position.set(0, 0);
		FixtureDef fixtureDef2 = new FixtureDef();
		EdgeShape edgeShape = new EdgeShape();
		edgeShape.set(-w / 2, -h / 2, w / 2, -h / 2);
		fixtureDef2.shape = edgeShape;
		bodyEdgeScreen = world.createBody(bodyDef2);
		bodyEdgeScreen.setActive(true);
		bodyEdgeScreen.createFixture(fixtureDef2);
		edgeShape.dispose();
		Gdx.input.setInputProcessor(this);
		debugRenderer = new Box2DDebugRenderer();
		font = new BitmapFont();
		font.setColor(Color.BLACK);
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	@Override
	public void render() {
		camera.update();
		// Step the physics simulation forward at a rate of 60hz
		world.step(1f / 60f, 6, 2);
		body.applyTorque(torque, true);
		sprite.setPosition(
				(body.getPosition().x * PIXELS_TO_METERS) - sprite.getWidth() / 2,
				(body.getPosition().y * PIXELS_TO_METERS) - sprite.getHeight() / 2);
		sprite.setRotation((float) Math.toDegrees(body.getAngle()));
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		debugMatrix = batch.getProjectionMatrix().cpy().scale(PIXELS_TO_METERS, PIXELS_TO_METERS, 0);
		batch.begin();
		if (drawSprite)
			batch.draw(
					sprite,
					sprite.getX(),
					sprite.getY(),
					sprite.getOriginX(),
					sprite.getOriginY(),
					sprite.getWidth(),
					sprite.getHeight(),
					sprite.getScaleX(),
					sprite.getScaleY(),
					sprite.getRotation());
		//    font.draw(
		//        batch,
		//        "Restitution: " + body.getFixtureList().first().getRestitution(),
		//        -Gdx.graphics.getWidth() / 2,
		//        Gdx.graphics.getHeight() / 2);
		batch.end();
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
		if (keycode == Input.Keys.RIGHT) body.setLinearVelocity(1f, 0f);
		if (keycode == Input.Keys.LEFT) body.setLinearVelocity(-1f, 0f);
		if (keycode == Input.Keys.UP) body.applyForceToCenter(0f, 10f, true);
		if (keycode == Input.Keys.DOWN) body.applyForceToCenter(0f, -10f, true);
		// On brackets ( [ ] ) apply torque, either clock or counterclockwise
		if (keycode == Input.Keys.RIGHT_BRACKET) torque += 0.1f;
		if (keycode == Input.Keys.LEFT_BRACKET) torque -= 0.1f;
		// Remove the torque using backslash /
		if (keycode == Input.Keys.BACKSLASH) torque = 0.0f;
		// If user hits spacebar, reset everything back to normal
		if (keycode == Input.Keys.SPACE || keycode == Input.Keys.NUM_2) {
			body.setLinearVelocity(0f, 0f);
			body.setAngularVelocity(0f);
			torque = 0f;
			sprite.setPosition(0f, 0f);
			body.setTransform(0f, 0f, 0f);
		}
		if (keycode == Input.Keys.COMMA) {
			body.getFixtureList()
					.first()
					.setRestitution(body.getFixtureList().first().getRestitution() - 0.1f);
		}
		if (keycode == Input.Keys.PERIOD) {
			body.getFixtureList()
					.first()
					.setRestitution(body.getFixtureList().first().getRestitution() + 0.1f);
		}
		if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.NUM_1) drawSprite = !drawSprite;
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
		body.applyForce(1f, 1f, screenX, screenY, true);
		// body.applyTorque(0.4f,true);
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
	public boolean scrolled(float amountX, float amountY) {
		return false;
	}
}