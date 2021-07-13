package infra.entity.pathfinding.template;

import com.google.inject.Inject;

public class TemplateEdgeGeneratorFactory {

  @Inject RelativeActionEdgeGenerator relativeActionEdgeGenerator;
  @Inject BlockStructureFactory blockStructureFactory;

  @Inject EdgeStore edgeStore;

  @Inject
  TemplateEdgeGeneratorFactory() {}

  public TemplateEdgeGenerator create(RelativeVertex rootRelativeVertex) {
    return new TemplateEdgeGenerator(
        relativeActionEdgeGenerator, blockStructureFactory, edgeStore, rootRelativeVertex);
  }
}
