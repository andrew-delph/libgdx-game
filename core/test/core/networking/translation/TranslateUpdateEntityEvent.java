package core.networking.translation;

import com.google.inject.Guice;
import com.google.inject.Injector;
import core.app.screen.assets.animations.AnimationState;
import core.chunk.ChunkRange;
import core.common.CommonFactory;
import core.common.exceptions.SerializationDataMissing;
import core.configuration.ClientConfig;
import core.entity.Entity;
import core.entity.EntityFactory;
import core.entity.attributes.inventory.Equipped;
import core.entity.attributes.inventory.item.EmptyInventoryItem;
import core.entity.attributes.inventory.item.OrbInventoryItem;
import core.entity.attributes.inventory.item.SwordInventoryItem;
import core.entity.attributes.msc.AnimationStateWrapper;
import core.entity.attributes.msc.Coordinates;
import core.networking.events.EventTypeFactory;
import core.networking.events.types.incoming.UpdateEntityIncomingEventType;
import core.networking.events.types.outgoing.UpdateEntityOutgoingEventType;
import org.junit.Test;

public class TranslateUpdateEntityEvent {

  @Test
  public void testTranslateUpdateEntityEvent() throws SerializationDataMissing {
    Injector injector = Guice.createInjector(new ClientConfig());
    NetworkDataDeserializer networkDataDeserializer =
        injector.getInstance(NetworkDataDeserializer.class);
    EntityFactory entityFactory = injector.getInstance(EntityFactory.class);

    Coordinates coordinates = CommonFactory.createCoordinates(0, 1);
    ChunkRange chunkRange = new ChunkRange(coordinates);
    Entity entity = entityFactory.createEntity(CommonFactory.createCoordinates(0, 0));

    UpdateEntityOutgoingEventType outgoing =
        EventTypeFactory.createUpdateEntityOutgoingEvent(coordinates, chunkRange, entity.getUuid());
    UpdateEntityIncomingEventType incoming =
        NetworkDataDeserializer.createUpdateEntityIncomingEvent(
            NetworkDataSerializer.createUpdateEntityOutgoingEventType(outgoing));

    assert outgoing.getChunkRange().equals(incoming.getChunkRange());
    assert outgoing.getAttributeList().equals(incoming.getAttributeList());
  }

  @Test
  public void testTranslateUpdateEntityEventEmptyItem() throws SerializationDataMissing {
    Injector injector = Guice.createInjector(new ClientConfig());
    NetworkDataDeserializer networkDataDeserializer =
        injector.getInstance(NetworkDataDeserializer.class);
    EntityFactory entityFactory = injector.getInstance(EntityFactory.class);

    Coordinates coordinates = CommonFactory.createCoordinates(0, 1);
    ChunkRange chunkRange = new ChunkRange(coordinates);
    Entity entity = entityFactory.createEntity(CommonFactory.createCoordinates(0, 0));

    OrbInventoryItem orb = new OrbInventoryItem(3);

    UpdateEntityOutgoingEventType outgoing =
        EventTypeFactory.createUpdateEntityOutgoingEvent(orb, chunkRange, entity.getUuid());
    UpdateEntityIncomingEventType incoming =
        NetworkDataDeserializer.createUpdateEntityIncomingEvent(
            NetworkDataSerializer.createUpdateEntityOutgoingEventType(outgoing));

    assert outgoing.getChunkRange().equals(incoming.getChunkRange());
    assert outgoing.getAttributeList().equals(incoming.getAttributeList());
  }

  @Test
  public void testTranslateUpdateEntityEventOrbItem() throws SerializationDataMissing {
    Injector injector = Guice.createInjector(new ClientConfig());
    NetworkDataDeserializer networkDataDeserializer =
        injector.getInstance(NetworkDataDeserializer.class);
    EntityFactory entityFactory = injector.getInstance(EntityFactory.class);

    Coordinates coordinates = CommonFactory.createCoordinates(0, 1);
    ChunkRange chunkRange = new ChunkRange(coordinates);
    Entity entity = entityFactory.createEntity(CommonFactory.createCoordinates(0, 0));

    EmptyInventoryItem item = new EmptyInventoryItem(3);

    UpdateEntityOutgoingEventType outgoing =
        EventTypeFactory.createUpdateEntityOutgoingEvent(item, chunkRange, entity.getUuid());
    UpdateEntityIncomingEventType incoming =
        NetworkDataDeserializer.createUpdateEntityIncomingEvent(
            NetworkDataSerializer.createUpdateEntityOutgoingEventType(outgoing));

    assert outgoing.getChunkRange().equals(incoming.getChunkRange());
    assert outgoing.getAttributeList().equals(incoming.getAttributeList());
  }

  @Test
  public void testTranslateUpdateEntityEventEquipped() throws SerializationDataMissing {
    Injector injector = Guice.createInjector(new ClientConfig());

    EntityFactory entityFactory = injector.getInstance(EntityFactory.class);

    Coordinates coordinates = CommonFactory.createCoordinates(0, 1);
    ChunkRange chunkRange = new ChunkRange(coordinates);
    Entity entity = entityFactory.createEntity(CommonFactory.createCoordinates(0, 0));

    Equipped item = new Equipped(2);

    UpdateEntityOutgoingEventType outgoing =
        EventTypeFactory.createUpdateEntityOutgoingEvent(item, chunkRange, entity.getUuid());
    UpdateEntityIncomingEventType incoming =
        NetworkDataDeserializer.createUpdateEntityIncomingEvent(
            NetworkDataSerializer.createUpdateEntityOutgoingEventType(outgoing));

    assert outgoing.getChunkRange().equals(incoming.getChunkRange());
    assert outgoing.getAttributeList().equals(incoming.getAttributeList());

    assert incoming.getAttributeList().size() == 1;
  }

  @Test
  public void testTranslateUpdateEntityEventSword() throws SerializationDataMissing {
    Injector injector = Guice.createInjector(new ClientConfig());

    EntityFactory entityFactory = injector.getInstance(EntityFactory.class);

    Coordinates coordinates = CommonFactory.createCoordinates(0, 1);
    ChunkRange chunkRange = new ChunkRange(coordinates);
    Entity entity = entityFactory.createEntity(CommonFactory.createCoordinates(0, 0));

    SwordInventoryItem item = new SwordInventoryItem(2);

    UpdateEntityOutgoingEventType outgoing =
        EventTypeFactory.createUpdateEntityOutgoingEvent(item, chunkRange, entity.getUuid());
    UpdateEntityIncomingEventType incoming =
        NetworkDataDeserializer.createUpdateEntityIncomingEvent(
            NetworkDataSerializer.createUpdateEntityOutgoingEventType(outgoing));

    assert outgoing.getChunkRange().equals(incoming.getChunkRange());
    assert outgoing.getAttributeList().equals(incoming.getAttributeList());

    assert incoming.getAttributeList().size() == 1;

    assert incoming.getAttributeList().get(0).equals(item);
  }

  @Test
  public void testTranslateUpdateEntityAnimationStateWrapper() throws SerializationDataMissing {
    Injector injector = Guice.createInjector(new ClientConfig());

    EntityFactory entityFactory = injector.getInstance(EntityFactory.class);

    Coordinates coordinates = CommonFactory.createCoordinates(0, 1);
    ChunkRange chunkRange = new ChunkRange(coordinates);
    Entity entity = entityFactory.createEntity(CommonFactory.createCoordinates(0, 0));

    AnimationStateWrapper animationStateWrapper =
        new AnimationStateWrapper(AnimationState.ATTACKING);

    UpdateEntityOutgoingEventType outgoing =
        EventTypeFactory.createUpdateEntityOutgoingEvent(
            animationStateWrapper, chunkRange, entity.getUuid());
    UpdateEntityIncomingEventType incoming =
        NetworkDataDeserializer.createUpdateEntityIncomingEvent(
            NetworkDataSerializer.createUpdateEntityOutgoingEventType(outgoing));

    assert outgoing.getChunkRange().equals(incoming.getChunkRange());
    assert outgoing.getAttributeList().equals(incoming.getAttributeList());

    assert incoming.getAttributeList().size() == 1;

    assert incoming.getAttributeList().get(0).equals(animationStateWrapper);
  }
}
