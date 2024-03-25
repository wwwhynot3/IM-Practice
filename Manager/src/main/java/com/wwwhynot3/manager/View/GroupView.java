package com.wwwhynot3.manager.View;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupView {
    private Long groupId;
    private String groupName;
    private List<GroupMember> groupMembers;
    private GroupFileTreeView groupFileTree;
}
