package com.wwwhynot3.manager.Controller;


import com.huanglb.common.AuthTokenPool;
import com.huanglb.common.Authorization.AuthorizationAuthenticator;
import com.huanglb.common.Msg.MapperMsg;
import com.huanglb.common.ResponseData;
import com.wwwhynot3.manager.Entity.Group;
import com.wwwhynot3.manager.Service.Interfaces.GroupService;
import com.wwwhynot3.manager.View.GroupView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/group")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GroupController {

    private final GroupService groupService;
    private final AuthTokenPool authTokenPool;

    @AuthorizationAuthenticator
    @PostMapping
    public ResponseData<GroupView> createGroup(@RequestAttribute("userId") Long userId,
                                               @RequestParam("groupName") String groupName,
                                               @RequestParam(value = "groupMembers", required = false) Long[] groupMembers,
                                               @RequestParam(value = "groupMemberTitles", required = false) String[] groupMemberTitles) {


        Group group = new Group(null, groupName);
        if (!groupService.createGroup(group)) {
            return ResponseData.build(MapperMsg.ADD_FAILURE, null);
        }
        Long groupId = group.getGroupId();
        // 添加群主
        if (!groupService.addGroupUser(groupId, userId, "群主")) {
            return ResponseData.build(MapperMsg.ADD_FAILURE, null);
        }
        // 添加其他群成员
        if (groupMembers != null && groupMembers.length > 0 && groupMemberTitles != null && groupMemberTitles.length > 0) {
            return groupService.addGroupUsers(groupId, groupMembers, groupMemberTitles) > 0
                    ? ResponseData.build(MapperMsg.ADD_SUCCESS, groupService.getGroupView(groupId))
                    : ResponseData.build(MapperMsg.ADD_FAILURE, null);
        }
        return ResponseData.build(MapperMsg.ADD_SUCCESS, groupService.getGroupView(groupId));
    }

    @AuthorizationAuthenticator
    @GetMapping
    public ResponseData<GroupView> getGroup(@RequestAttribute("userId") Long userId,
                                            @RequestParam("groupId") Long groupId) {
        return ResponseData.build(MapperMsg.QUERY_SUCCESS, groupService.getGroupView(groupId));
    }

    @AuthorizationAuthenticator
    @PutMapping
    public ResponseData<GroupView> modifyGroup(@RequestAttribute("userId") Long userId,
                                               @RequestBody Group group) {
        return groupService.modifyGroup(group)
                ? ResponseData.build(MapperMsg.MODIFY_SUCCESS, groupService.getGroupView(group.getGroupId()))
                : ResponseData.build(MapperMsg.MODIFY_FAILURE, null);
    }

    @AuthorizationAuthenticator
    @DeleteMapping
    public ResponseData<Boolean> dismissGroup(@RequestAttribute("userId") Long userId,
                                              @RequestParam("groupId") Long groupId) {
        return groupService.dismissGroup(groupId)
                ? ResponseData.build(MapperMsg.DELETE_SUCCESS, true)
                : ResponseData.build(MapperMsg.DELETE_FAILURE, false);
    }

    @AuthorizationAuthenticator
    @PostMapping("/user")
    public ResponseData<String> addGroupUser(@RequestAttribute("userId") Long userId,
                                             @RequestParam("groupId") Long groupId,
                                             @RequestParam("groupMemberId") Long groupMemberId,
                                             @RequestParam("groupMemberTitle") String groupMemberTitle) {
        if (!groupService.getGroupUser(groupId, userId).getUserTitle().equals(Group.OWNER)) {
            return ResponseData.build(MapperMsg.ADD_FAILURE, "用户没有管理员权限");
        }
        return groupService.addGroupUser(groupId, groupMemberId, groupMemberTitle)
                ? ResponseData.build(MapperMsg.ADD_SUCCESS, "用户添加成功")
                : ResponseData.build(MapperMsg.ADD_FAILURE, "用户添加失败");
    }

    @AuthorizationAuthenticator
    @PutMapping("/user")
    public ResponseData<String> changeGroupUserTitle(@RequestAttribute("userId") Long userId,
                                                     @RequestParam("groupId") Long groupId,
                                                     @RequestParam("groupMemberId") Long groupMemberId,
                                                     @RequestParam("groupMemberTitle") String groupMemberTitle) {
        if (!groupService.getGroupUser(groupId, userId).getUserTitle().equals(Group.OWNER)) {
            return ResponseData.build(MapperMsg.ADD_FAILURE, "用户没有管理员权限");
        }
        return groupService.changeGroupUserTitle(groupId, groupMemberId, groupMemberTitle)
                ? ResponseData.build(MapperMsg.MODIFY_SUCCESS, "用户权限修改成功")
                : ResponseData.build(MapperMsg.MODIFY_FAILURE, "用户权限修改失败");
    }

    @AuthorizationAuthenticator
    @DeleteMapping("/user")
    public ResponseData<String> removeGroupUser(@RequestAttribute("userId") Long userId,
                                                @RequestParam("groupId") Long groupId,
                                                @RequestParam("groupMemberId") Long groupMemberId) {
        if (!groupService.getGroupUser(groupId, userId).getUserTitle().equals(Group.OWNER)) {
            return ResponseData.build(MapperMsg.ADD_FAILURE, "用户没有管理员权限");
        }
        return groupService.removeGroupUser(groupId, groupMemberId)
                ? ResponseData.build(MapperMsg.DELETE_SUCCESS, "群聊删除成功")
                : ResponseData.build(MapperMsg.DELETE_FAILURE, "群聊删除失败");
    }
}
