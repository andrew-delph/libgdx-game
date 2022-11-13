package core.app.screen.assets.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GameAnimationFactory {

  GameAnimation createEntityAnimation2() {
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

  GameAnimation createEntityAnimation() {
    final int DEFAULT_FRAMES_NUM = 23;
    final int DEFAULT_FRAME_COLS = 6, DEFAULT_FRAME_ROWS = 4;
    final String DEFAULT_FILE_NAME = "default_spritesheet2.png";
    Texture walkSheet = new Texture(Gdx.files.internal(DEFAULT_FILE_NAME));
    TextureRegion[][] tmp =
        TextureRegion.split(
            walkSheet,
            walkSheet.getWidth() / DEFAULT_FRAME_COLS,
            walkSheet.getHeight() / DEFAULT_FRAME_ROWS);

    TextureRegion[] defaultFrames = new TextureRegion[DEFAULT_FRAMES_NUM];
    int index = 0;
    for (int i = 0; i < DEFAULT_FRAME_ROWS; i++) {
      for (int j = 0; j < DEFAULT_FRAME_COLS; j++) {
        if (index >= DEFAULT_FRAMES_NUM) break;
        defaultFrames[index++] = tmp[i][j];
      }
    }

    final int WALKING_FRAMES_NUM = 26;
    final int WALKING_FRAME_COLS = 6, WALKING_FRAME_ROWS = 5;
    final String WALKING_FILE_NAME = "walking_spritesheet.png";

    walkSheet = new Texture(Gdx.files.internal(WALKING_FILE_NAME));
    tmp =
        TextureRegion.split(
            walkSheet,
            walkSheet.getWidth() / WALKING_FRAME_COLS,
            walkSheet.getHeight() / WALKING_FRAME_ROWS);

    TextureRegion[] walkLeftFrames = new TextureRegion[WALKING_FRAMES_NUM];
    index = 0;
    for (int i = 0; i < WALKING_FRAME_ROWS; i++) {
      for (int j = 0; j < WALKING_FRAME_COLS; j++) {
        //        tmp[i][j].flip(true, false);
        if (index >= WALKING_FRAMES_NUM) break;

        walkLeftFrames[index++] = tmp[i][j];
      }
    }

    TextureRegion[] walkRightFrames = new TextureRegion[WALKING_FRAMES_NUM];
    tmp =
        TextureRegion.split(
            walkSheet,
            walkSheet.getWidth() / WALKING_FRAME_COLS,
            walkSheet.getHeight() / WALKING_FRAME_ROWS);
    index = 0;
    for (int i = 0; i < WALKING_FRAME_ROWS; i++) {
      for (int j = 0; j < WALKING_FRAME_COLS; j++) {
        if (index >= WALKING_FRAMES_NUM) break;
        tmp[i][j].flip(true, false);
        walkRightFrames[index++] = tmp[i][j];
      }
    }

    TextureRegion[] punchFramesLeft = new TextureRegion[27];
    for (int i = 0; i < 27; i++) {
      if (i < 10)
        punchFramesLeft[i] =
            new TextureRegion(new Texture(Gdx.files.internal("punch/punch_000" + i + ".png")));
      else
        punchFramesLeft[i] =
            new TextureRegion(new Texture(Gdx.files.internal("punch/punch_00" + i + ".png")));
    }

    TextureRegion[] punchFramesRight = new TextureRegion[27];
    for (int i = 0; i < 27; i++) {
      if (i < 10)
        punchFramesRight[i] =
            new TextureRegion(new Texture(Gdx.files.internal("punch/punch_000" + i + ".png")));
      else
        punchFramesRight[i] =
            new TextureRegion(new Texture(Gdx.files.internal("punch/punch_00" + i + ".png")));

      punchFramesRight[i].flip(true, false);
    }

    return new GameAnimationBuilder()
        .addAnimation(AnimationState.DEFAULT, new Animation<TextureRegion>(0.25f, defaultFrames))
        .addAnimation(
            AnimationState.WALKING_LEFT, new Animation<TextureRegion>(0.05f, walkLeftFrames))
        .addAnimation(
            AnimationState.WALKING_RIGHT, new Animation<TextureRegion>(0.05f, walkRightFrames))
        .addAnimation(
            AnimationState.PUNCH_LEFT, new Animation<TextureRegion>(0.05f, punchFramesLeft))
        .addAnimation(
            AnimationState.PUNCH_RIGHT, new Animation<TextureRegion>(0.05f, punchFramesRight))
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
