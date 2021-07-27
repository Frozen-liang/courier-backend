package com.sms.satp.utils;

import com.sms.satp.dto.response.TreeResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;

public abstract class TreeUtils {

    private static final Integer PARENT_DEPTH = 1;

    public static List<TreeResponse> createTree(List<? extends TreeResponse> treeResponses) {
        List<TreeResponse> parentList = treeResponses.stream()
            .filter((e) -> PARENT_DEPTH.equals(e.getDepth()))
            .collect(Collectors.toList());
        Map<String, List<TreeResponse>> childMap =
            treeResponses.stream().filter((e) -> e.getDepth() > PARENT_DEPTH)
                .collect(Collectors.groupingBy(TreeResponse::getParentId));
        setChild(parentList, childMap);
        return parentList;
    }

    private static void setChild(List<TreeResponse> parentList, Map<String, List<TreeResponse>> childMap) {
        if (CollectionUtils.isEmpty(parentList)) {
            return;
        }
        parentList.forEach(treeBaseResponse -> {
            List<TreeResponse> childList = childMap.get(treeBaseResponse.getId());
            treeBaseResponse.setChildList(childList);
            setChild(childList, childMap);
        });
    }
}
