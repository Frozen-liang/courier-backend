package com.sms.courier.controller;

import static com.sms.courier.common.constant.Constants.API_COMMENT_PATE;

import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.dto.request.ApiCommentRequest;
import com.sms.courier.dto.response.TreeResponse;
import com.sms.courier.service.ApiCommentService;
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
@RequestMapping(API_COMMENT_PATE)
public class ApiCommentController {

    private final ApiCommentService apiCommentService;

    public ApiCommentController(ApiCommentService apiCommentService) {
        this.apiCommentService = apiCommentService;
    }

    @PostMapping
    @PreAuthorize("hasRoleOrAdmin(@role.API_COMMENT_CRE_UPD_DEL)")
    public Boolean add(@Validated(InsertGroup.class) @RequestBody ApiCommentRequest apiCommentRequest) {
        return apiCommentService.add(apiCommentRequest);
    }

    @GetMapping("/list")
    @PreAuthorize("hasRoleOrAdmin(@role.API_COMMENT_QUERY_ALL)")
    public List<TreeResponse> list(@RequestParam ObjectId apiId) {
        return apiCommentService.list(apiId);
    }

    @DeleteMapping("/{ids}")
    @PreAuthorize("hasRoleOrAdmin(@role.API_COMMENT_CRE_UPD_DEL)")
    public Boolean delete(@PathVariable List<String> ids) {
        return apiCommentService.delete(ids);
    }
}
