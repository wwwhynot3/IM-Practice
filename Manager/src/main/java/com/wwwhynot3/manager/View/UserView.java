package com.wwwhynot3.manager.View;

import com.wwwhynot3.manager.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserView {
    private Long userId;
    private String username;
    private String email;

    private OrganizationView organization;
    private List<SimpleUserView> friends;
    private List<SimpleGroupView> groups;

    public UserView(User user, OrganizationView organization, List<SimpleUserView> friends, List<SimpleGroupView> groups) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.organization = organization;
        this.friends = friends;
        this.groups = groups;
    }
}
