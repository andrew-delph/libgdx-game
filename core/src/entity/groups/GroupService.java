package entity.groups;

import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class GroupService {
  SetMultimap<Group, UUID> groupMap = MultimapBuilder.hashKeys().hashSetValues().build();
  Map<UUID, Group> uuidMap = new HashMap<>();

  public Group getGroup(UUID entityUUID) {
    return uuidMap.get(entityUUID);
  }

  public void registerEntityGroup(UUID uuid, Group group) {
    removeEntity(uuid);
    groupMap.put(group, uuid);
    uuidMap.put(uuid, group);
  }

  public Set<UUID> getInGroup(Group group) {
    return groupMap.get(group);
  }

  public void removeEntity(UUID uuid) {
    Group targetGroup = uuidMap.remove(uuid);
    if (targetGroup == null) return;
    groupMap.remove(targetGroup, uuid);
  }
}
