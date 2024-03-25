package com.wwwhynot3.manager.View;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupFileView {
    private Long groupFileId;
    private String groupFileName;
    private Long groupFileSize;
    private Boolean groupFileIsDirectory;
    private Long groupFileIdParent;
}
