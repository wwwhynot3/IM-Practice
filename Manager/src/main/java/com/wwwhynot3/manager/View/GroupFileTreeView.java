package com.wwwhynot3.manager.View;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupFileTreeView {
    private String CurrentDirectoryName;
    private List<GroupFileTreeView> subGroupFileDirectories;
    private List<GroupFileView> groupFiles;

    public static GroupFileTreeView build(List<GroupFileView> groupFiles) {
        HashMap<Long, List<GroupFileView>> fileMap = new HashMap<>();
        HashMap<Long, List<GroupFileView>> directoryMap = new HashMap<>();
        Long rootId = null;
        for (GroupFileView groupFile : groupFiles) {

            if (groupFile.getGroupFileIdParent().equals(groupFile.getGroupFileId())) {
                rootId = groupFile.getGroupFileId();
            } else if (groupFile.getGroupFileIsDirectory()) {
                directoryMap.putIfAbsent(groupFile.getGroupFileId(), new ArrayList<>());
                directoryMap.get(groupFile.getGroupFileId()).add(groupFile);
            } else {
                fileMap.putIfAbsent(groupFile.getGroupFileId(), new ArrayList<>());
                fileMap.get(groupFile.getGroupFileId()).add(groupFile);
            }
        }
        return build(rootId, "", fileMap, directoryMap);
    }

    private static GroupFileTreeView build(Long rootId, String currentDirectoryName, HashMap<Long, List<GroupFileView>> fileMap, HashMap<Long, List<GroupFileView>> directoryMap) {
        GroupFileTreeView root = new GroupFileTreeView(currentDirectoryName, null, fileMap.get(rootId));
        List<GroupFileTreeView> subGroupFileDirectories = new ArrayList<>();
        if (directoryMap.get(rootId) == null) {
            return root;
        }
        for (GroupFileView groupFile : directoryMap.get(rootId)) {
            subGroupFileDirectories.add(build(groupFile.getGroupFileId(), currentDirectoryName + groupFile.getGroupFileName(), fileMap, directoryMap));
        }
        root.setSubGroupFileDirectories(subGroupFileDirectories);
        return root;
    }


}
