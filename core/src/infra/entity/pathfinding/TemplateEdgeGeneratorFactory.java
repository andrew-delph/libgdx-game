package infra.entity.pathfinding;

import com.google.inject.Inject;

public class TemplateEdgeGeneratorFactory {

  @Inject RelativeActionEdgeGenerator relativeActionEdgeGenerator;
  @Inject EntityStructureFactory entityStructureFactory;

  @Inject EdgeStore edgeStore;

  @Inject
  TemplateEdgeGeneratorFactory() {}

  public TemplateEdgeGenerator create(RelativeVertex rootRelativeVertex) {
    return new TemplateEdgeGenerator(
        relativeActionEdgeGenerator, entityStructureFactory, edgeStore, rootRelativeVertex);
  }
}
