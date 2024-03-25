package com.wwwhynot3.manager.Service.Interfaces;

import com.wwwhynot3.manager.Entity.Group;
import com.wwwhynot3.manager.Entity.GroupUser;
import com.wwwhynot3.manager.View.GroupFileTreeView;
import com.wwwhynot3.manager.View.GroupFileView;
import com.wwwhynot3.manager.View.GroupMember;
import com.wwwhynot3.manager.View.GroupView;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GroupService {
    boolean createGroup(Group group);

    boolean dismissGroup(Long groupId);

    boolean modifyGroup(Group group);

    Group getGroup(Long groupId);

    GroupView getGroupView(Long groupId);

    GroupUser getGroupUser(Long groupId, Long userId);

    boolean addGroupUser(Long groupId, Long userId, String userTitle);

    int addGroupUsers(Long groupId, Long[] userIds, String[] userTitles);

    boolean changeGroupUserTitle(Long groupId, Long userId, String title);

    boolean removeGroupUser(Long groupId, Long userId);

    int removeGroupUsers(Long groupId, Long[] userIds);

    List<GroupMember> getGroupMembers(Long groupId);

    List<GroupFileView> getGroupFileView(Long groupId);

    GroupFileTreeView getGroupFileTreeView(Long groupId);
}
