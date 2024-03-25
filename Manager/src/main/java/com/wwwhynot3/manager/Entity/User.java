package com.wwwhynot3.manager.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName(value = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @TableId(type = IdType.ASSIGN_ID)
    private Long userId;
    private String username;
    private String password;
    private String email;
    private Long organizationId;

    public User(User user) {
        this.userId = user.userId;
        this.username = user.username;
        this.password = user.password;
        this.email = user.email;
        this.organizationId = user.organizationId;
    }
}
