package com.wwwhynot3.manager.Entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName(value = "user_relation")
@NoArgsConstructor
@AllArgsConstructor
public class UserRelation {
    private Long userIdMaster;
    private Long userIdSlave;
    private Boolean concern;
}
