package com.wwwhynot3.manager.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName(value = "group_file")
@AllArgsConstructor
@NoArgsConstructor
public class GroupFile {
    @TableId(type = IdType.ASSIGN_ID)
    private Long groupFileId;
    private Long groupId;
    private String groupFileName;
    private String groupFileSize;
    private Boolean groupFileIsDirectory;
    private byte[] groupFileContent;
    private Long groupFileIdParent;
}
