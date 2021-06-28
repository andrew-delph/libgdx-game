package infra.entity.block;

public interface BlockFactory {
  DirtBlock createDirt();

  StoneBlock createStone();

  SkyBlock createSky();
}
