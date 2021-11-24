package common.events.types;

import com.google.inject.Inject;
import entity.block.Block;

import java.util.UUID;

public class ReplaceBlockEventType extends EventType {

  public static String type = "replace_block";

  UUID target;
  Block replacementBlock;

  @Inject
  public ReplaceBlockEventType(UUID target, Block replacementBlock) {
    this.target = target;
    this.replacementBlock = replacementBlock;
  }

  public UUID getTarget() {
    return target;
  }

  public Block getReplacementBlock() {
    return replacementBlock;
  }

  @Override
  public String getType() {
    return type;
  }
}
