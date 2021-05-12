package infra.map.block;

import base.BaseAssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import game.User;
import infra.common.Coordinate;

import java.util.UUID;

public class BlockFactory {

  @Inject BaseAssetManager assetManager;

  @Inject User user;

  @Inject
  @Named("provideTexture")
  Boolean provideTexture;

  int size = 10;

  Texture dirtTexture;

  @Inject
  BlockFactory(@Named("CoordinateScale") int size) {
    this.size = size;
  }

  public Block createBlock(int x, int y) {
    return this.createBlock(new Coordinate(x, y));
  }

  public Block createBlock(Coordinate coordinate) {
    if (provideTexture) {
      if (dirtTexture == null) {
        Pixmap pixmap = this.createPixmapDefaulltSize();
        pixmap.setColor(0, 1, 0, 0.75f);
        pixmap.fillCircle(this.size / 2, this.size / 2, this.size / 2);
        dirtTexture = new Texture(pixmap);
        pixmap.dispose();
      }
      return new Block(UUID.randomUUID(), coordinate, user.getId(), this.size, dirtTexture);
    } else {
      return new Block(UUID.randomUUID(), coordinate, user.getId(), this.size);
    }
  }

  public DirtBlock createDirtBlock(Coordinate coordinate) {
    if (provideTexture) {
      Pixmap pixmap = this.createPixmapDefaulltSize();
      pixmap.setColor(Color.BROWN);
      pixmap.fill();
      Texture pixmaptex = new Texture(pixmap);
      pixmap.dispose();
      return new DirtBlock(UUID.randomUUID(), coordinate, user.getId(), this.size, pixmaptex);
      //            return new DirtBlock(coordinate, this.size, assetManager.get("dirtblock.jpg",
      // Texture.class));
    } else {

      return new DirtBlock(UUID.randomUUID(), coordinate, user.getId(), this.size);
    }
  }

  public AirBlock createAirBlock(Coordinate coordinate) {
    if (provideTexture) {
      Pixmap pixmap = this.createPixmapDefaulltSize();
      pixmap.setColor(Color.BLUE);
      pixmap.fill();
      Texture pixmaptex = new Texture(pixmap);
      pixmap.dispose();
      return new AirBlock(UUID.randomUUID(), coordinate, user.getId(), this.size, pixmaptex);
      //            return new DirtBlock(coordinate, this.size, assetManager.get("dirtblock.jpg",
      // Texture.class));
    } else {
      return new AirBlock(UUID.randomUUID(), coordinate, user.getId(), this.size);
    }
  }

  public StoneBlock createStoneBlock(Coordinate coordinate) {
    if (provideTexture) {
      Pixmap pixmap = this.createPixmapDefaulltSize();
      pixmap.setColor(Color.GRAY);
      pixmap.fill();
      Texture pixmaptex = new Texture(pixmap);
      pixmap.dispose();
      return new StoneBlock(UUID.randomUUID(), coordinate, user.getId(), this.size, pixmaptex);
      //            return new DirtBlock(coordinate, this.size, assetManager.get("dirtblock.jpg",
      // Texture.class));
    } else {
      return new StoneBlock(UUID.randomUUID(), coordinate, user.getId(), this.size);
    }
  }

  private Pixmap createPixmapDefaulltSize() {
    return new Pixmap(this.size, this.size, Pixmap.Format.RGBA8888);
  }
}
