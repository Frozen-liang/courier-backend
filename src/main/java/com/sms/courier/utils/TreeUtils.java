package com.sms.courier.utils;

import com.sms.courier.dto.response.TreeResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public abstract class TreeUtils {

    public static List<TreeResponse> createTree(List<? extends TreeResponse> treeResponses) {
        List<TreeResponse> parentList = treeResponses.stream()
            .filter((tree) -> StringUtils.isBlank(tree.getParentId()))
            .collect(Collectors.toList());
        Map<String, List<TreeResponse>> childMap =
            treeResponses.stream()
                .filter((tree) -> !StringUtils.isBlank(tree.getParentId()) && !StringUtils
                    .equals(tree.getParentId(), tree.getId()))
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
