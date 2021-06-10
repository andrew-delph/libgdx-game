package infra.common.events;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import infra.entity.block.SkyBlock;

import java.util.UUID;

public class ReplaceBlockEvent extends Event{

    public static String type = "replace_block";

    UUID target;
    String replacementBlockType;

    public UUID getTarget() {
        return target;
    }

    public String getReplacementBlockType() {
        return replacementBlockType;
    }

    @Inject
    public ReplaceBlockEvent(@Assisted UUID target, @Assisted String replacementBlockType) {
        this.target = target;
        this.replacementBlockType = replacementBlockType;
    }

    @Override
    public String getType() {
        return type;
    }
}
