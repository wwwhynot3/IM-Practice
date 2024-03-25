package com.wwwhynot3.manager.Entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName(value = "group_user")
@AllArgsConstructor
@NoArgsConstructor
public class GroupUser {
    private Long groupId;
    private Long userId;
    private String userTitle;
    private Boolean top;
}
