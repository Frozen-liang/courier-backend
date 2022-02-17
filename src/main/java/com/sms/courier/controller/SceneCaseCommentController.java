package com.sms.courier.controller;

import static com.sms.courier.common.constant.Constants.SCENE_CASE_COMMENT_PATH;

import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.dto.request.SceneCaseCommentRequest;
import com.sms.courier.dto.response.TreeResponse;
import com.sms.courier.service.SceneCaseCommentService;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(SCENE_CASE_COMMENT_PATH)
public class SceneCaseCommentController {

    private final SceneCaseCommentService sceneCaseCommentService;

    public SceneCaseCommentController(SceneCaseCommentService sceneCaseCommentService) {
        this.sceneCaseCommentService = sceneCaseCommentService;
    }

    @PostMapping
    @PreAuthorize("hasRoleOrAdmin(@role.SCENE_CASE_COMMENT_CRE_UPD_DEL)")
    public Boolean add(@Validated(InsertGroup.class) @RequestBody SceneCaseCommentRequest sceneCaseCommentRequest) {
        return sceneCaseCommentService.add(sceneCaseCommentRequest);
    }

    @GetMapping("/list")
    @PreAuthorize("hasRoleOrAdmin(@role.SCENE_CASE_COMMENT_QUERY_ALL)")
    public List<TreeResponse> list(@RequestParam ObjectId sceneCaseId) {
        return sceneCaseCommentService.list(sceneCaseId);
    }

    @DeleteMapping("/{ids}")
    @PreAuthorize("hasRoleOrAdmin(@role.SCENE_CASE_COMMENT_CRE_UPD_DEL)")
    public Boolean delete(@PathVariable List<String> ids) {
        return sceneCaseCommentService.delete(ids);
    }

}
