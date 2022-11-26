package core.app.screen.assets.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class GameAnimationFactory {

  TextureRegion[] getFramesFromTxt(String dir, boolean flip) {
    FileHandle assetsFile = Gdx.files.local(dir);
    BufferedReader reader = new BufferedReader(assetsFile.reader());

    List<String> lines = new LinkedList<>();
    try {
      String line = reader.readLine();
      while (line != null) {
        lines.add(line);
        line = reader.readLine();
      }
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    TextureRegion[] frames = new TextureRegion[lines.size()];

    for (int i = 0; i < frames.length; i++) {
      String fileName = lines.get(i);
      frames[i] = new TextureRegion(new Texture(Gdx.files.local(fileName)));
      if (flip) frames[i].flip(true, false);
    }

    return frames;
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

    // punch left
    TextureRegion[] punchFramesLeft = getFramesFromTxt("punch/assets.txt", false);

    // punch right
    TextureRegion[] punchFramesRight = getFramesFromTxt("punch/assets.txt", true);

    // Dig Left
    TextureRegion[] digFramesLeft = getFramesFromTxt("pickaxe/assets.txt", false);

    // Dig Right
    TextureRegion[] digFramesRight = getFramesFromTxt("pickaxe/assets.txt", true);

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
        .addAnimation(
            AnimationState.DIGGING_LEFT, new Animation<TextureRegion>(0.05f, digFramesLeft))
        .addAnimation(
            AnimationState.DIGGING_RIGHT, new Animation<TextureRegion>(0.05f, digFramesRight))
        .addAnimation(
            AnimationState.DIGGING_DOWN, new Animation<TextureRegion>(0.05f, digFramesLeft))
        .addAnimation(
            AnimationState.DIGGING_UP, new Animation<TextureRegion>(0.05f, digFramesRight))
        .build();
  }

  GameAnimation createOrbAnimation() {
    TextureRegion[] orbFrames = getFramesFromTxt("orb/Blue/assets.txt", false);

    return new GameAnimationBuilder()
        .addAnimation(AnimationState.DEFAULT, new Animation<TextureRegion>(0.2f, orbFrames))
        .build();
  }
}
