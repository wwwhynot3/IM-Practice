package com.wwwhynot3.manager.Controller;


import com.huanglb.common.AuthTokenPool;
import com.huanglb.common.Authorization.AuthorizationAuthenticator;
import com.huanglb.common.Authorization.AuthorizationInterceptor;
import com.huanglb.common.Msg.MapperMsg;
import com.huanglb.common.ResponseData;
import com.wwwhynot3.manager.Entity.GroupFile;
import com.wwwhynot3.manager.Entity.User;
import com.wwwhynot3.manager.Service.Interfaces.UserService;
import com.wwwhynot3.manager.View.UserView;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {
    private final UserService userService;
    private final AuthTokenPool authTokenPool;

    @PostMapping(value = "/signup")
    public ResponseData<UserView> createUser(HttpServletResponse response, @RequestBody User user) {
        if (!userService.createUser(user)) {
            return ResponseData.build(MapperMsg.ADD_FAILURE, null);
        }
        Long userId = user.getUserId();
        String token = authTokenPool.makeToken(userId);
        response.addCookie(new Cookie(AuthorizationInterceptor.COOKIE_NAME, token));
        return ResponseData.build(MapperMsg.SUCCESSFUL_LOGIN, userService.getUserView(userId));
    }

    //todo 密码加密传输 思路：使用password对请求网址进行加密传输，服务端在数据库查找密码进行解密，验证解密产物是否是请求网址
    @PostMapping(value = "/login")
    public ResponseData<UserView> login(HttpServletResponse response, @RequestParam("username") String username, @RequestParam("password") String password) {
        Long userId = userService.login(username, password);
        if (userId == null) {
            return ResponseData.build(MapperMsg.INVALID_LOGIN, null);
        }
        String token = authTokenPool.makeToken(userId);
        response.addCookie(new Cookie(AuthorizationInterceptor.COOKIE_NAME, token));
        return ResponseData.build(MapperMsg.SUCCESSFUL_LOGIN, userService.getUserView(userId));
    }

    @AuthorizationAuthenticator
    @PutMapping
    public ResponseData<String> updateUser(@RequestBody User user) {
        boolean modified = userService.updateUser(user);
        return modified
                ? ResponseData.build(MapperMsg.MODIFY_SUCCESS, "更新成功")
                : ResponseData.build(MapperMsg.MODIFY_FAILURE, "更新失败");
    }

    @AuthorizationAuthenticator
    @DeleteMapping
    public ResponseData<String> deleteUser(@RequestAttribute("userId") Long userId) {
        return userService.deleteUser(userId)
                ? ResponseData.build(MapperMsg.DELETE_SUCCESS, "删除成功")
                : ResponseData.build(MapperMsg.DELETE_FAILURE, "删除失败");
    }

    @AuthorizationAuthenticator
    @PostMapping(value = "/friend")
    public ResponseData<String> addUserToFriend(@RequestAttribute("userId") Long userIdMaster,
                                                @RequestParam("userIdSlave") Long userIdSlave,
                                                @RequestParam("concern") Boolean concern) {
        log.info("addUserToFriend: userIdMaster: {}, userIdSlave: {}, concern: {}", userIdMaster, userIdSlave, concern);
        return userService.addUserToFriend(userIdMaster, userIdSlave, concern)
                ? ResponseData.build(MapperMsg.ADD_SUCCESS, "添加成功")
                : ResponseData.build(MapperMsg.ADD_FAILURE, "添加失败");
    }

    @AuthorizationAuthenticator
    @PutMapping(value = "/friend")
    public ResponseData<String> concernUser(@RequestAttribute("userId") Long userIdMaster,
                                            @RequestParam("userIdTarget") Long userIdSlave,
                                            @RequestParam("concern") Boolean concern) {
        log.info("concernUser: userIdMaster: {}, userIdSlave: {}, concern: {}", userIdMaster, userIdSlave, concern);
        return userService.concernUserForUser(userIdMaster, userIdSlave, concern)
                ? ResponseData.build(MapperMsg.MODIFY_SUCCESS, "关注成功")
                : ResponseData.build(MapperMsg.MODIFY_FAILURE, "关注失败");
    }


    @AuthorizationAuthenticator
    @PostMapping(value = "/group")
    public ResponseData<String> joinGroup(@RequestAttribute("userId") Long userId,
                                          @RequestParam("groupId") Long groupId,
                                          @RequestParam("userTitle") String userTitle) {
        return userService.addUserToGroup(userId, groupId, userTitle)
                ? ResponseData.build(MapperMsg.ADD_SUCCESS, "加入成功")
                : ResponseData.build(MapperMsg.ADD_FAILURE, "加入失败");
    }

    @AuthorizationAuthenticator
    @PutMapping(value = "/group")
    public ResponseData<String> topGroupForUser(@RequestAttribute("userId") Long userId,
                                                @RequestParam("groupId") Long groupId) {
        return userService.topGroupForUser(userId, groupId)
                ? ResponseData.build(MapperMsg.MODIFY_SUCCESS, "置顶成功")
                : ResponseData.build(MapperMsg.MODIFY_FAILURE, "置顶失败");
    }

    @AuthorizationAuthenticator
    @PostMapping(value = "/group/file")
    public ResponseData<Boolean> uploadGroupFile(@RequestBody GroupFile groupFile) {
        return ResponseData.Ok(userService.uploadGroupFile(groupFile));
    }

    @AuthorizationAuthenticator
    @GetMapping(value = "/group/file")
    public GroupFile downloadGroupFile(@RequestParam(value = "groupFileId") Long groupFileId) {
        return userService.downloadGroupFile(groupFileId);
    }

    @AuthorizationAuthenticator
    @DeleteMapping(value = "/group/file")
    public boolean deleteGroupFile(@RequestParam(value = "groupFileId") Long groupFileId) {
        return userService.deleteGroupFile(groupFileId);
    }
}
