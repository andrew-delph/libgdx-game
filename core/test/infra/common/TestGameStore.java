package infra.common;

import com.google.inject.Guice;
import com.google.inject.Injector;
import configuration.MainConfig;
import infra.entity.Entity;
import infra.entity.EntityFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class TestGameStore {

    Injector injector;
    GameStore gameStore;

    EntityFactory entityFactory;

    @Before
    public void setup() throws IOException {
        injector = Guice.createInjector(new MainConfig());
        gameStore = injector.getInstance(GameStore.class);
        entityFactory = injector.getInstance(EntityFactory.class);
    }

    @Test
    public void testEntityExistence(){
        Entity testEntity = entityFactory.createEntity();
        gameStore.addEntity(testEntity);
        assert testEntity == gameStore.getEntity(testEntity.uuid);
    }
}
