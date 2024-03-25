package com.wwwhynot3.manager.Service.Interfaces;

import com.wwwhynot3.manager.Entity.*;
import com.wwwhynot3.manager.View.UserView;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    Long login(String username, String password);

    boolean createUser(User user);

    boolean deleteUser(Long userId);

    boolean updateUser(User user);

    User getUser(Long userId);

    List<UserRelation> getUserRelations(Long userId);


    List<User> getUserFriends(Long userId);

    List<GroupUser> getUserGroupUsers(Long userId);

    List<Group> getUserGroups(Long userId);

    UserView getUserView(Long userId);

    boolean addUserToGroup(Long userId, Long groupId, String userTitle);

    boolean removeUserFromGroup(Long userId, Long groupId);

    boolean topGroupForUser(Long userId, Long groupId);

    boolean addUserToFriend(Long userIdMaster, Long userIdSlave, Boolean concern);

    boolean concernUserForUser(Long userIdMaster, Long userIdSlave, Boolean concern);

    boolean uploadGroupFile(Long groupId, String fileName, String groupFileSize, Boolean isDirectory, byte[] groupFileContent, Long groupFileIdParent);

    boolean uploadGroupFile(GroupFile groupFile);

    int updateGroupFiles(List<GroupFile> groupFiles);

    boolean deleteGroupFile(Long groupFileId);

    GroupFile downloadGroupFile(Long groupFileId);

}
