package com.wwwhynot3.manager.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName(value = "im_practice.groups")
@NoArgsConstructor
@AllArgsConstructor
public class Group {
    public static final String OWNER = "群主";
    @TableId(type = IdType.ASSIGN_ID)
    private Long groupId;
    private String groupName;
}
