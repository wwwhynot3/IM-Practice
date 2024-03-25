package com.wwwhynot3.manager.View;

import com.wwwhynot3.manager.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimpleUserView {
    private Long userId;
    private String username;
    private String email;

    private OrganizationView organization;
    private Boolean concern;

    public SimpleUserView(User user, Boolean concern, OrganizationView organization) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.concern = concern;
        this.organization = organization;
    }
}
