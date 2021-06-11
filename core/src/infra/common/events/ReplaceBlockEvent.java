package infra.common.events;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import infra.entity.block.Block;
import infra.entity.block.SkyBlock;

import java.util.UUID;

public class ReplaceBlockEvent extends Event{

    public static String type = "replace_block";

    UUID target;
    Block replacementBlock;

    public UUID getTarget() {
        return target;
    }

    public Block getReplacementBlock() {
        return replacementBlock;
    }

    @Inject
    public ReplaceBlockEvent(@Assisted UUID target, @Assisted Block replacementBlock) {
        this.target = target;
        this.replacementBlock = replacementBlock;
    }

    @Override
    public String getType() {
        return type;
    }
}
