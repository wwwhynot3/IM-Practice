package com.wwwhynot3.manager.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName(value = "organizations")
@NoArgsConstructor
@AllArgsConstructor
public class Organization {
    @TableId(type = IdType.ASSIGN_ID)
    private Long organizationId;
    private String organizationName;
    private String contact;
    private Long organizationIdParent;
}
