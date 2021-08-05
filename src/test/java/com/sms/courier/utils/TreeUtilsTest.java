package com.sms.courier.utils;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.courier.dto.response.ApiGroupResponse;
import java.util.List;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for TreeUtils")
public class TreeUtilsTest {

    private final List<ApiGroupResponse> apiGroupResponseList = List
        .of(ApiGroupResponse.builder().parentId(ID).id(ID).depth(1).build());
    private static final String ID = ObjectId.get().toString();

    @Test
    @DisplayName("Test the createTree method in the TreeUtils")
    public void createTree_test() {
        assertThat(TreeUtils.createTree(apiGroupResponseList)).isNotNull();
    }
}
