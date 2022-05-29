package entity.group;

import com.google.inject.Guice;
import com.google.inject.Injector;
import configuration.ClientConfig;
import entity.groups.Group;
import entity.groups.GroupService;
import java.util.UUID;
import org.junit.Test;

public class testGroupService {

  @Test
  public void testGroupService() {
    Injector injector = Guice.createInjector(new ClientConfig());

    GroupService groupService = injector.getInstance(GroupService.class);

    UUID uuid = UUID.randomUUID();

    assert groupService.getGroup(uuid) == null;

    groupService.registerEntityGroup(uuid, Group.AI_GROUP);

    assert groupService.getGroup(uuid) == Group.AI_GROUP;

    groupService.registerEntityGroup(uuid, Group.PLAYER_GROUP);

    assert groupService.getGroup(uuid) == Group.PLAYER_GROUP;

    assert groupService.getInGroup(Group.PLAYER_GROUP).size() == 1;

    assert groupService.getInGroup(Group.PLAYER_GROUP).contains(uuid);

    groupService.removeEntity(uuid);

    assert groupService.getGroup(uuid) == null;
  }
}
