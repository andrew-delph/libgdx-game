package infra.entity.collision.type;

import infra.entity.block.Block;

public class BlockEnvironmentBehavior implements EnvironmentBehavior {
  private Block source;

  public BlockEnvironmentBehavior(Block source) {
    this.source = source;
  }

  @Override
  public Block getSource() {
    return source;
  }
}
