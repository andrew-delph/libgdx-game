package infra.map.block;

import base.BaseAssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import infra.common.Coordinate;

import java.util.Random;

public class BlockFactory {

    @Inject
    BaseAssetManager assetManager;

    @Inject @Named("provideTexture")
    Boolean provideTexture;

    int size = 10;

    @Inject
    BlockFactory(@Named("CoordinateScale") int size){
        this.size = size;
    }

    public Block createBlock(int x, int y){
        return this.createBlock(new Coordinate(x,y));
    }
    public Block createBlock(Coordinate coordinate){
        if (provideTexture) {
            Pixmap pixmap = new Pixmap( 64, 64, Pixmap.Format.RGBA8888 );
            pixmap.setColor( 0, 1, 0, 0.75f );
            pixmap.fillCircle( 32, 32, 32 );
            Texture pixmaptex = new Texture( pixmap );
            pixmap.dispose();
            return new Block(coordinate, this.size, pixmaptex);
        }
        else{
            return new Block(coordinate, this.size);
        }
    }
    public DirtBlock createDirtBlock(Coordinate coordinate){
        if (provideTexture) {
            return new DirtBlock(coordinate, this.size, assetManager.get("dirtblock.jpg", Texture.class));
        }
        else{
            return new DirtBlock(coordinate, this.size);
        }
    }
}
