package infra.entity.pathfinding.template;

import com.google.inject.Inject;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class EdgeStore {

    Set<TemplateEdge> templateEdges= new HashSet<>();

    @Inject
    EdgeStore(){

    }

    public void add(TemplateEdge templateEdge){
        this.templateEdges.add(templateEdge);
    }

    public List<TemplateEdge> getEdgeList(){
        return new LinkedList<>(this.templateEdges);
    }
}
