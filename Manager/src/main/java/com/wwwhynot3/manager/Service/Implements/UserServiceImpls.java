package com.wwwhynot3.manager.Service.Implements;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wwwhynot3.manager.Entity.*;
import com.wwwhynot3.manager.Mapper.*;
import com.wwwhynot3.manager.Service.Interfaces.UserService;
import com.wwwhynot3.manager.View.OrganizationView;
import com.wwwhynot3.manager.View.SimpleGroupView;
import com.wwwhynot3.manager.View.SimpleUserView;
import com.wwwhynot3.manager.View.UserView;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpls implements UserService {
    private final UserMapper userMapper;
    private final OrganizationMapper organizationMapper;
    private final UserRelationMapper userRelationMapper;
    private final GroupMapper groupMapper;
    private final GroupUserMapper groupUserMapper;
    private final GroupFileMapper groupFileMapper;

    @Override
    public Long login(String username, String password) {
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery()
                .eq(User::getUsername, username)
                .eq(User::getPassword, password);
        User user = userMapper.selectOne(wrapper);
        return user == null ? null : user.getUserId();
    }

    @Override
    public boolean createUser(User user) {
        return userMapper.insert(user) > 0;
    }

    @Override
    public boolean deleteUser(Long userId) {
        return userMapper.deleteById(userId) > 0;
    }

    @Override
    public boolean updateUser(User user) {
        return userMapper.updateById(user) > 0;
    }

    @Override
    public User getUser(Long userId) {
        return userMapper.selectById(userId);
    }

    @Override
    public List<UserRelation> getUserRelations(Long userId) {
        return userRelationMapper.selectList(Wrappers.<UserRelation>lambdaQuery()
                .eq(UserRelation::getUserIdMaster, userId));
    }


    @Override
    public List<User> getUserFriends(Long userId) {
        return getUserRelations(userId).stream()
                .map(userRelation -> userMapper.selectById(userRelation.getUserIdSlave()))
                .collect(Collectors.toList());
    }

    @Override
    public List<GroupUser> getUserGroupUsers(Long userId) {
        return groupUserMapper.selectList(Wrappers.<GroupUser>lambdaQuery()
                .eq(GroupUser::getUserId, userId));
    }

    @Override
    public List<Group> getUserGroups(Long userId) {
        return getUserGroupUsers(userId).stream()
                .map(groupUser -> groupMapper.selectById(groupUser.getGroupId()))
                .collect(Collectors.toList());

    }

    @Override
    public UserView getUserView(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return null;
        }
        List<Organization> organizationList = getOrganization(user.getOrganizationId());
        return new UserView(user,
                new OrganizationView(organizationList,
                        organizationList.stream()
                                .map(Organization::getOrganizationName)
                                .collect(Collectors.joining())),
                getUserRelations(userId).stream()
                        .map(userRelation -> {
                            User userSlave = userMapper.selectById(userRelation.getUserIdSlave());
                            List<Organization> organizationListSlave = getOrganization(userSlave.getOrganizationId());
                            return new SimpleUserView(userSlave, userRelation.getConcern(),
                                    new OrganizationView(organizationListSlave,
                                            organizationListSlave.stream()
                                                    .map(Organization::getOrganizationName)
                                                    .collect(Collectors.joining())
                                    )
                            );
                        })
                        .collect(Collectors.toList()),
                getUserGroupUsers(userId).stream()
                        .map(groupUser -> {
                            Group group = groupMapper.selectById(groupUser.getGroupId());
                            return new SimpleGroupView(group.getGroupId(), group.getGroupName(), groupUser.getUserTitle(), groupUser.getTop());
                        })
                        .collect(Collectors.toList())
        );
    }

    public List<Organization> getOrganization(Long organizationId) {
        List<Organization> res = new ArrayList<>();
        Organization root = organizationMapper.selectById(organizationId);
        while (root != null && !root.getOrganizationId().equals(root.getOrganizationIdParent())) {
            res.add(root);
            root = organizationMapper.selectById(root.getOrganizationIdParent());
        }
        return res;
    }

    @Override
    public boolean addUserToGroup(Long userId, Long groupId, String userTitle) {
        return groupUserMapper.insert(new GroupUser(groupId, userId, userTitle, false)) > 0;
    }

    @Override
    public boolean removeUserFromGroup(Long userId, Long groupId) {
        return groupUserMapper.delete(Wrappers.<GroupUser>lambdaQuery()
                .eq(GroupUser::getGroupId, groupId)
                .eq(GroupUser::getUserId, userId)) > 0;
    }

    @Override
    public boolean topGroupForUser(Long userId, Long groupId) {
        return groupUserMapper.update(Wrappers.<GroupUser>lambdaUpdate()
                .eq(GroupUser::getGroupId, groupId)
                .eq(GroupUser::getUserId, userId)
                .set(GroupUser::getTop, true)) > 0;
    }

    @Override
    public boolean addUserToFriend(Long userIdMaster, Long userIdSlave, Boolean concern) {
        return userRelationMapper.insert(new UserRelation(userIdMaster, userIdSlave, concern)) > 0;
    }

    @Override
    public boolean concernUserForUser(Long userIdMaster, Long userIdSlave, Boolean concern) {
        return userRelationMapper.update(Wrappers.<UserRelation>lambdaUpdate()
                .eq(UserRelation::getUserIdMaster, userIdMaster)
                .eq(UserRelation::getUserIdSlave, userIdSlave)
                .set(UserRelation::getConcern, concern)) > 0;
    }

    @Override
    public boolean uploadGroupFile(Long groupId, String fileName, String groupFileSize, Boolean isDirectory, byte[] groupFileContent, Long groupFileIdParent) {
        return groupFileMapper.insert(
                new GroupFile(0L, groupId, fileName, groupFileSize, isDirectory, groupFileContent, groupFileIdParent)) > 0;
    }

    @Override
    public boolean uploadGroupFile(GroupFile groupFile) {
        return groupFileMapper.insert(groupFile) > 0;
    }

    @Override
    public int updateGroupFiles(List<GroupFile> groupFiles) {
        return groupFileMapper.insertBatchSomeColumn(groupFiles);
    }

    @Override
    public boolean deleteGroupFile(Long groupFileId) {
        return groupFileMapper.deleteById(groupFileId) > 0;
    }

    @Override
    public GroupFile downloadGroupFile(Long groupFileId) {
        return groupFileMapper.selectById(groupFileId);
    }
}
