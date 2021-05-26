package infra.entity.block;

public interface BlockFactory {
  Block create();

  DirtBlock createDirt();

  StoneBlock createStone();

  SkyBlock createSky();
}
