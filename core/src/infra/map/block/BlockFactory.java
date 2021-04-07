package infra.map.block;

import base.BaseAssetManager;
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
        if (provideTexture){
            Random rand = new Random();
            int rand_int1 = rand.nextInt(1000);
            if(rand_int1<100){
                return new Block(new Coordinate(x,y), this.size, assetManager.get("dirtblock.jpg", Texture.class));
            }
            else{
                return new Block(new Coordinate(x,y), this.size, assetManager.get("frog.png", Texture.class));
            }
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
