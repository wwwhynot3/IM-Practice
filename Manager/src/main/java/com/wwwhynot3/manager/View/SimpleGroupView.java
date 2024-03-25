package com.wwwhynot3.manager.View;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimpleGroupView {
    private Long groupId;
    private String groupName;
    private String userTitle;
    private Boolean top;
}
