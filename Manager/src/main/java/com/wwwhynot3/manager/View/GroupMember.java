package com.wwwhynot3.manager.View;

import com.wwwhynot3.manager.Entity.GroupUser;
import com.wwwhynot3.manager.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupMember {
    private Long userId;
    private String userTitle;
    private String username;
    private String email;
    private OrganizationView organizationView;

    public GroupMember(GroupUser groupUser, User user, OrganizationView organizationView) {
        this.userId = groupUser.getUserId();
        this.userTitle = groupUser.getUserTitle();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.organizationView = organizationView;
    }

}
