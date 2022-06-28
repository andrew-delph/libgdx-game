package networking.translation;

import chunk.ChunkRange;
import com.google.inject.Guice;
import com.google.inject.Injector;
import common.exceptions.SerializationDataMissing;
import configuration.ClientConfig;
import entity.Entity;
import entity.EntityFactory;
import entity.attributes.msc.Coordinates;
import entity.attributes.inventory.Equipped;
import entity.attributes.inventory.item.EmptyInventoryItem;
import entity.attributes.inventory.item.OrbInventoryItem;
import entity.attributes.inventory.item.SwordInventoryItem;
import networking.events.EventTypeFactory;
import networking.events.types.incoming.UpdateEntityIncomingEventType;
import networking.events.types.outgoing.UpdateEntityOutgoingEventType;
import org.junit.Test;

public class TranslateUpdateEntityEvent {

  @Test
  public void testTranslateUpdateEntityEvent() throws SerializationDataMissing {
    Injector injector = Guice.createInjector(new ClientConfig());
    NetworkDataDeserializer networkDataDeserializer =
        injector.getInstance(NetworkDataDeserializer.class);
    EntityFactory entityFactory = injector.getInstance(EntityFactory.class);

    Coordinates coordinates = new Coordinates(0, 1);
    ChunkRange chunkRange = new ChunkRange(coordinates);
    Entity entity = entityFactory.createEntity(new Coordinates(0, 0));

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

    Coordinates coordinates = new Coordinates(0, 1);
    ChunkRange chunkRange = new ChunkRange(coordinates);
    Entity entity = entityFactory.createEntity(new Coordinates(0, 0));

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

    Coordinates coordinates = new Coordinates(0, 1);
    ChunkRange chunkRange = new ChunkRange(coordinates);
    Entity entity = entityFactory.createEntity(new Coordinates(0, 0));

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

    Coordinates coordinates = new Coordinates(0, 1);
    ChunkRange chunkRange = new ChunkRange(coordinates);
    Entity entity = entityFactory.createEntity(new Coordinates(0, 0));

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

    Coordinates coordinates = new Coordinates(0, 1);
    ChunkRange chunkRange = new ChunkRange(coordinates);
    Entity entity = entityFactory.createEntity(new Coordinates(0, 0));

    SwordInventoryItem item = new SwordInventoryItem(2);

    UpdateEntityOutgoingEventType outgoing =
        EventTypeFactory.createUpdateEntityOutgoingEvent(item, chunkRange, entity.getUuid());
    UpdateEntityIncomingEventType incoming =
        NetworkDataDeserializer.createUpdateEntityIncomingEvent(
            NetworkDataSerializer.createUpdateEntityOutgoingEventType(outgoing));

    assert outgoing.getChunkRange().equals(incoming.getChunkRange());
    assert outgoing.getAttributeList().equals(incoming.getAttributeList());

    assert incoming.getAttributeList().size() == 1;
  }
}
