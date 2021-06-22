package infra.networking.events;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import infra.chunk.ChunkRange;
import infra.common.events.Event;
import infra.entity.block.Block;
import infra.networking.NetworkObjects;
import infra.networking.events.interfaces.SerializeNetworkEvent;

import java.util.UUID;

public class ReplaceBlockOutgoingEvent extends Event implements SerializeNetworkEvent {

  public static String type = "replace_block_outgoing";

  UUID target;
  Block replacementBlock;
  ChunkRange chunkRange;

  @Inject
  public ReplaceBlockOutgoingEvent(
      @Assisted UUID target, @Assisted Block replacementBlock, @Assisted ChunkRange chunkRange) {
    this.target = target;
    this.replacementBlock = replacementBlock;
    this.chunkRange = chunkRange;
  }

  public ChunkRange getChunkRange() {
    return chunkRange;
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

  @Override
  public NetworkObjects.NetworkEvent toNetworkEvent() {
    NetworkObjects.NetworkData target =
        NetworkObjects.NetworkData.newBuilder()
            .setKey("target")
            .setValue(this.target.toString())
            .build();
    NetworkObjects.NetworkData replacementBlockType =
        NetworkObjects.NetworkData.newBuilder()
            .setKey("replacementBlockData")
            .addChildren(this.getReplacementBlock().toNetworkData())
            .build();
    NetworkObjects.NetworkData data =
        NetworkObjects.NetworkData.newBuilder()
            .addChildren(target)
            .addChildren(replacementBlockType)
            .build();
    return NetworkObjects.NetworkEvent.newBuilder().setData(data).setEvent(type).build();
  }
}
