package core.entity.attributes.inventory;

import core.app.game.Game;
import core.app.game.GameController;
import core.chunk.ChunkRange;
import core.chunk.world.exceptions.BodyNotFound;
import com.google.inject.Guice;
import com.google.inject.Injector;
import core.common.Clock;
import core.common.GameStore;
import core.common.exceptions.ChunkNotFound;
import core.common.exceptions.SerializationDataMissing;
import core.common.exceptions.WrongVersion;
import core.configuration.StandAloneConfig;
import core.entity.Entity;
import core.entity.attributes.inventory.item.OrbInventoryItem;
import core.entity.attributes.msc.Coordinates;
import core.entity.misc.Orb;
import java.io.IOException;
import org.junit.Test;

public class testOrbInventory {

  @Test
  public void testPickup()
      throws WrongVersion, SerializationDataMissing, IOException, InterruptedException,
          BodyNotFound, ChunkNotFound {
    // create a game. start it.
    // put a block down
    // put an entity on top of the block.
    // spawn 11 obs
    // the entity should have 10 orbs
    // 1 orbs should still exist
    Injector injector = Guice.createInjector(new StandAloneConfig());
    Game game = injector.getInstance(Game.class);
    Clock clock = injector.getInstance(Clock.class);
    GameController gameController = injector.getInstance(GameController.class);
    GameStore gameStore = injector.getInstance(GameStore.class);
    game.start();
    clock.waitForTick(10);

    Entity entity1 = gameController.createEntity(new Coordinates(1, 1));
    for (int i = 0; i < 5; i++) {
      gameController.createOrb(new Coordinates(1, 1));
    }
    clock.waitForTick(10);
    assert entity1.getBag().freeSpace() == 15;

    for (int i = 0; i < 20; i++) {
      gameController.createOrb(new Coordinates(1, 1));
    }
    clock.waitForTick(10);
    assert entity1.getBag().freeSpace() == 0;

    assert gameStore.getChunk(new ChunkRange(new Coordinates(0, 0))).getEntityList().stream()
            .filter((e) -> e instanceof Orb)
            .count()
        == 5;
  }

  @Test
  public void testPickup2()
      throws InterruptedException, ChunkNotFound, WrongVersion, SerializationDataMissing,
          IOException, BodyNotFound {
    // same as testPickup
    // but 2 entities should exist
    // after 11 orbs there should be none in the world. sum of orbs is 11
    // after 21 orbs. there is one left over. both full of orbs.
    Injector injector = Guice.createInjector(new StandAloneConfig());
    Game game = injector.getInstance(Game.class);
    Clock clock = injector.getInstance(Clock.class);
    GameController gameController = injector.getInstance(GameController.class);
    GameStore gameStore = injector.getInstance(GameStore.class);
    game.start();
    clock.waitForTick(10);

    Entity entity1 = gameController.createEntity(new Coordinates(1, 1));
    Entity entity2 = gameController.createEntity(new Coordinates(1, 1));
    for (int i = 0; i < 5; i++) {
      gameController.createOrb(new Coordinates(1, 1));
    }
    clock.waitForTick(10);
    assert gameStore.getChunk(new ChunkRange(new Coordinates(0, 0))).getEntityList().stream()
        .noneMatch((e) -> e instanceof Orb);

    for (int i = 0; i < 40; i++) {
      gameController.createOrb(new Coordinates(1, 1));
    }
    clock.waitForTick(10);
    assert entity1.getBag().freeSpace() == 0;
    assert entity2.getBag().freeSpace() == 0;

    assert gameStore.getChunk(new ChunkRange(new Coordinates(0, 0))).getEntityList().stream()
            .filter((e) -> e instanceof Orb)
            .count()
        == 5;
  }

  @Test
  public void testEqual() {
    OrbInventoryItem o1 = new OrbInventoryItem(1);
    OrbInventoryItem o2 = new OrbInventoryItem(1);

    assert o1.equals(o2);
  }

  @Test
  public void testNotEqual() {
    OrbInventoryItem o1 = new OrbInventoryItem(1);
    OrbInventoryItem o2 = new OrbInventoryItem(2);

    assert !o1.equals(o2);
  }
}
