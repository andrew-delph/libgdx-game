package infra.entity.pathfinding;

import com.google.inject.Inject;
import infra.common.GameStore;

public class BlockStructureFactory {
    @Inject
    GameStore gameStore;

    BlockStructure createBlockStructure(){
        return new BlockStructure(this.gameStore);
    }
}
