package infra.common.events;

import com.google.inject.Inject;

import java.util.UUID;

import infra.entity.block.Block;

public class ReplaceBlockEvent extends Event {

  public static String type = "replace_block";

  UUID target;
  Block replacementBlock;

  @Inject
  public ReplaceBlockEvent(UUID target, Block replacementBlock) {
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
