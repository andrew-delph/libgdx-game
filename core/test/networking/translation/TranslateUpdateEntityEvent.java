package networking.translation;

import chunk.ChunkRange;
import com.google.inject.Guice;
import com.google.inject.Injector;
import common.Coordinates;
import common.exceptions.SerializationDataMissing;
import configuration.ClientConfig;
import entity.Entity;
import entity.EntityFactory;
import networking.events.EventTypeFactory;
import networking.events.types.incoming.UpdateEntityIncomingEventType;
import networking.events.types.outgoing.UpdateEntityOutgoingEventType;
import org.junit.Test;

public class TranslateUpdateEntityEvent {

    @Test
    public void testTranslateUpdateEntityEvent() throws SerializationDataMissing {
        Injector injector = Guice.createInjector(new ClientConfig());
        NetworkDataDeserializer networkDataDeserializer = injector.getInstance(NetworkDataDeserializer.class);
        EntityFactory entityFactory = injector.getInstance(EntityFactory.class);

        Coordinates coordinates = new Coordinates(0, 1);
        ChunkRange chunkRange = new ChunkRange(coordinates);
        Entity entity = entityFactory.createEntity();

        UpdateEntityOutgoingEventType outgoing = EventTypeFactory.createUpdateEntityOutgoingEvent(entity.toNetworkData(), chunkRange);
        UpdateEntityIncomingEventType incoming = NetworkDataDeserializer.createUpdateEntityIncomingEvent(
                NetworkDataSerializer.createUpdateEntityOutgoingEventType(outgoing));

        assert outgoing.getChunkRange().equals(incoming.getChunkRange());
        assert outgoing.getEntityData().equals(incoming.getData());
    }
}
