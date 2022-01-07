package networking.translation;

import chunk.ChunkRange;
import com.google.inject.Guice;
import com.google.inject.Injector;
import common.Coordinates;
import common.events.types.CreateAIEntityEventType;
import common.exceptions.SerializationDataMissing;
import configuration.ClientConfig;
import entity.Entity;
import entity.EntityFactory;
import networking.events.EventTypeFactory;
import networking.events.types.incoming.CreateEntityIncomingEventType;
import networking.events.types.outgoing.CreateEntityOutgoingEventType;
import org.junit.Test;

import java.util.UUID;

public class TranslateCreateAIEvent {

    @Test
    public void testTranslateCreateEntityEvent() throws SerializationDataMissing {
        Injector injector = Guice.createInjector(new ClientConfig());
        NetworkDataDeserializer networkDataDeserializer = injector.getInstance(NetworkDataDeserializer.class);
        EntityFactory entityFactory = injector.getInstance(EntityFactory.class);

        Coordinates coordinates = new Coordinates(0, 1);
        UUID uuid = UUID.randomUUID();

        CreateAIEntityEventType outgoing = EventTypeFactory.createAIEntityEventType(coordinates, uuid);
        CreateAIEntityEventType incoming = NetworkDataDeserializer.createCreateAIEntityEventType(
                NetworkDataSerializer.createCreateAIEntityEventType(outgoing));

        assert incoming.getTarget().equals(outgoing.getTarget());
        assert incoming.getCoordinates().equals(outgoing.getCoordinates());
    }
}
