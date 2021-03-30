package infra.map.block;

import base.BaseAssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import infra.common.Coordinate;

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
        if (provideTexture){
            return new Block(new Coordinate(x,y), this.size, assetManager.get("dirtblock.jpg", Texture.class));
        }
        else{
            return new Block(new Coordinate(x,y), this.size);
        }
    }
    public Block createBlock(Coordinate coordinate){
        if (provideTexture) {
            return new Block(coordinate, this.size, assetManager.get("dirtblock.jpg", Texture.class));
        }
        else{
            return new Block(coordinate, this.size);
        }
    }
}
