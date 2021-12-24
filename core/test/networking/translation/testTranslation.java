package networking.translation;

import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class testTranslation {

    @Test
    public void testUUID() {
        UUID uuidExpected = UUID.randomUUID();
        UUID uuidRetrieved = NetworkDataDeserializer.createUUID(NetworkDataSerializer.createUUID(uuidExpected));
        assert uuidExpected.equals(uuidRetrieved);
    }

    @Test
    public void testUUIDList() {

        List<UUID> expectedList = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            expectedList.add(UUID.randomUUID());
        }

        List<UUID> retrievedList = NetworkDataDeserializer.createUUIDList(NetworkDataSerializer.createUUIDList(expectedList));
        Assert.assertEquals(expectedList, retrievedList);
    }
}
