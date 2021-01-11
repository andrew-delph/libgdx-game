package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import infra.entity.Entity;
import infra.entity.factories.EntityFactory;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
//	EntityManager entityManager;
	Entity player;

	@Override
	public void create () {
		this.batch = new SpriteBatch();
//		this.entityManager = EntityManager.getInstance();
		this.player = EntityFactory.getInstance().createBasic();
//		this.entityManager.create(this.player);
	}

	@Override
	public void render () {
		this.handleInput();
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
//		entityManager.updateAll(batch);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
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
	}
}
