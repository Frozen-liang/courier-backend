package com.sms.satp.utils;

import com.sms.satp.dto.response.TreeResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public abstract class TreeUtils {

    private static final Integer PARENT_DEPTH = 1;

    public static List<TreeResponse> createTree(List<? extends TreeResponse> treeResponses) {
        List<TreeResponse> parentList = treeResponses.stream()
            .filter((tree) -> PARENT_DEPTH.equals(tree.getDepth()))
            .collect(Collectors.toList());
        Map<String, List<TreeResponse>> childMap =
            treeResponses.stream()
                .filter(
                    (tree) -> tree.getDepth() > PARENT_DEPTH && !StringUtils.equals(tree.getParentId(), tree.getId()))
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
