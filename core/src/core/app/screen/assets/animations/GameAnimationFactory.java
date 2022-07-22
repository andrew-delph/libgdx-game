package core.app.screen.assets.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GameAnimationFactory {

  GameAnimation createEntityAnimation() {
    final int FRAME_COLS = 6, FRAME_ROWS = 5;
    Texture walkSheet = new Texture(Gdx.files.internal("sprite-animation4.png"));
    TextureRegion[][] tmp =
        TextureRegion.split(
            walkSheet, walkSheet.getWidth() / FRAME_COLS, walkSheet.getHeight() / FRAME_ROWS);

    TextureRegion[] walkRightFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
    int index = 0;
    for (int i = 0; i < FRAME_ROWS; i++) {
      for (int j = 0; j < FRAME_COLS; j++) {
        walkRightFrames[index++] = tmp[i][j];
      }
    }

    walkSheet = new Texture(Gdx.files.internal("sprite-animation4.png"));
    tmp =
        TextureRegion.split(
            walkSheet, walkSheet.getWidth() / FRAME_COLS, walkSheet.getHeight() / FRAME_ROWS);

    TextureRegion[] walkLeftFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
    index = 0;
    for (int i = 0; i < FRAME_ROWS; i++) {
      for (int j = 0; j < FRAME_COLS; j++) {
        tmp[i][j].flip(true, false);
        walkLeftFrames[index++] = tmp[i][j];
      }
    }

    return new GameAnimationBuilder()
        .addAnimation(AnimationState.DEFAULT, new Animation<TextureRegion>(0.025f, walkRightFrames))
        .addAnimation(
            AnimationState.WALKING_RIGHT, new Animation<TextureRegion>(0.025f, walkRightFrames))
        .addAnimation(
            AnimationState.WALKING_LEFT, new Animation<TextureRegion>(0.025f, walkLeftFrames))
        .build();
  }

  GameAnimation createOrbAnimation() {
    TextureRegion[] orbFrames = new TextureRegion[6];

    orbFrames[0] = new TextureRegion(new Texture(Gdx.files.internal("orb/Blue/frame 1.png")));
    orbFrames[1] = new TextureRegion(new Texture(Gdx.files.internal("orb/Blue/frame 2.png")));
    orbFrames[2] = new TextureRegion(new Texture(Gdx.files.internal("orb/Blue/frame 3.png")));
    orbFrames[3] = new TextureRegion(new Texture(Gdx.files.internal("orb/Blue/frame 4.png")));
    orbFrames[4] = new TextureRegion(new Texture(Gdx.files.internal("orb/Blue/frame 5.png")));
    orbFrames[5] = new TextureRegion(new Texture(Gdx.files.internal("orb/Blue/frame 6.png")));

    return new GameAnimationBuilder()
        .addAnimation(AnimationState.DEFAULT, new Animation<TextureRegion>(0.2f, orbFrames))
        .build();
  }
}
