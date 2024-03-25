package com.wwwhynot3.manager.Service.Implements;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.wwwhynot3.manager.Entity.Group;
import com.wwwhynot3.manager.Entity.GroupFile;
import com.wwwhynot3.manager.Entity.GroupUser;
import com.wwwhynot3.manager.Mapper.GroupFileMapper;
import com.wwwhynot3.manager.Mapper.GroupMapper;
import com.wwwhynot3.manager.Mapper.GroupUserMapper;
import com.wwwhynot3.manager.Service.Interfaces.GroupService;
import com.wwwhynot3.manager.View.GroupFileTreeView;
import com.wwwhynot3.manager.View.GroupFileView;
import com.wwwhynot3.manager.View.GroupMember;
import com.wwwhynot3.manager.View.GroupView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GroupServiceImpls implements GroupService {
    private final GroupMapper groupMapper;
    private final GroupUserMapper groupUserMapper;
    private final GroupFileMapper groupFileMapper;

    /**
     * 创建群组，同时创建群组初始文件
     *
     * @param group
     * @return
     */
    @Override
    public boolean createGroup(Group group) {
        if (groupMapper.insert(group) <= 0) {
            log.error("创建群组初始文件失败，group: {}", group.toString());
            return false;
        }
        Long groupId = group.getGroupId();
        GroupFile groupFile = new GroupFile(null,
                groupId,
                "",
                "",
                true,
                new byte[0],
                0L);
        boolean inserted = groupFileMapper.insert(groupFile) > 0;
        if (!inserted) {
            log.error("创建群组初始文件失败，groupId: {}", groupId);
        }
        Long groupFileId = groupFile.getGroupFileId();
        LambdaUpdateWrapper<GroupFile> wrapper = Wrappers.<GroupFile>lambdaUpdate()
                .eq(GroupFile::getGroupFileId, groupFileId)
                .set(GroupFile::getGroupFileIdParent, groupFileId);
        return groupFileMapper.update(null, wrapper) > 0;
    }

    /**
     * 删除群组，同时删除群组文件
     *
     * @param groupId
     * @return
     */
    @Override
    public boolean dismissGroup(Long groupId) {
        if (groupFileMapper.delete(Wrappers.<GroupFile>lambdaQuery().eq(GroupFile::getGroupId, groupId)) <= 0) {
            log.error("删除群组文件失败，groupId: {}", groupId);
            return false;
        }
        if (groupUserMapper.delete(Wrappers.<GroupUser>lambdaQuery().eq(GroupUser::getGroupId, groupId)) <= 0) {
            log.error("删除群组成员失败，groupId: {}", groupId);
            return false;
        }
        return groupMapper.deleteById(groupId) > 0;
    }

    @Override
    public boolean modifyGroup(Group group) {
        return groupMapper.updateById(group) > 0;
    }

    @Override
    public Group getGroup(Long groupId) {
        return groupMapper.selectById(groupId);
    }

    @Override
    public GroupView getGroupView(Long groupId) {
        Group group = getGroup(groupId);
        if (group == null) {
            return null;
        }
        List<GroupMember> groupMembers = getGroupMembers(groupId);
        GroupFileTreeView groupFileTreeView = getGroupFileTreeView(groupId);
        return new GroupView(group.getGroupId(), group.getGroupName(), groupMembers, groupFileTreeView);
    }

    @Override
    public GroupUser getGroupUser(Long groupId, Long userId) {
        return groupUserMapper.selectOne(Wrappers.<GroupUser>lambdaQuery()
                .eq(GroupUser::getGroupId, groupId)
                .eq(GroupUser::getUserId, userId));
    }

    @Override
    public boolean addGroupUser(Long groupId, Long userId, String userTitle) {
        return groupUserMapper.insert(new GroupUser(groupId, userId, userTitle, false)) > 0;
    }

    @Override
    public int addGroupUsers(Long groupId, Long[] userIds, String[] userTitles) {
        if (userIds == null || userTitles == null || userIds.length != userTitles.length) {
            return 0;
        }
        List<GroupUser> collect = IntStream.range(0, userIds.length)
                .mapToObj(i -> new GroupUser(groupId, userIds[i], userTitles[i], false))
                .toList();
        if (collect.size() == 1) {
            return groupUserMapper.insert(collect.getFirst());
        } else {
            return groupUserMapper.insertBatchSomeColumn(collect);
        }
    }

    @Override
    public List<GroupMember> getGroupMembers(Long groupId) {
        MPJLambdaWrapper<GroupUser> wrapper = new MPJLambdaWrapper<GroupUser>()
                .selectAsClass(GroupUser.class, GroupMember.class)
                .eq(GroupUser::getGroupId, groupId);
        return groupUserMapper.selectJoinList(GroupMember.class, wrapper);
    }

    @Override
    public boolean changeGroupUserTitle(Long groupId, Long userId, String title) {
        return groupUserMapper.update(Wrappers.<GroupUser>lambdaUpdate()
                .eq(GroupUser::getGroupId, groupId)
                .eq(GroupUser::getUserId, userId)
                .set(GroupUser::getUserTitle, title)) > 0;
    }

    @Override
    public boolean removeGroupUser(Long groupId, Long userId) {
        return groupUserMapper.delete(Wrappers.<GroupUser>lambdaQuery()
                .eq(GroupUser::getGroupId, groupId)
                .eq(GroupUser::getUserId, userId)) > 0;
    }

    @Override
    public int removeGroupUsers(Long groupId, Long[] userIds) {
        return groupUserMapper.delete(Wrappers.<GroupUser>lambdaQuery()
                .eq(GroupUser::getGroupId, groupId)
                .in(GroupUser::getUserId, (Object) userIds));
    }


    @Override
    public List<GroupFileView> getGroupFileView(Long groupId) {
        return null;
    }

    @Override
    public GroupFileTreeView getGroupFileTreeView(Long groupId) {
//        LambdaQueryWrapper<GroupFile> wrapper = Wrappers.<GroupFile>lambdaQuery()
//                .eq(GroupFile::getGroupId, groupId)
//                .select(GroupFile::getGroupFileId, GroupFile::getGroupFileName,
//                        GroupFile::getGroupFileSize, GroupFile::getGroupFileIdParent);
        MPJLambdaWrapper<GroupFile> wrapper = new MPJLambdaWrapper<GroupFile>()
                .selectAsClass(GroupFile.class, GroupFileView.class)
                .eq(GroupFile::getGroupId, groupId);
        List<GroupFileView> list = groupFileMapper.selectJoinList(GroupFileView.class, wrapper);
        return GroupFileTreeView.build(list);
    }
}
