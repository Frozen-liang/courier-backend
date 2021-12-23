package com.sms.courier.controller;

import static com.sms.courier.common.constant.Constants.DATABASE;

import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.common.validate.UpdateGroup;
import com.sms.courier.dto.request.DataBaseRequest;
import com.sms.courier.dto.response.DataBaseResponse;
import com.sms.courier.service.DatabaseService;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(DATABASE)
public class DataBaseController {

    private final DatabaseService dataBaseService;

    public DataBaseController(DatabaseService dataBaseService) {
        this.dataBaseService = dataBaseService;
    }

    @GetMapping("/{id}")
    public DataBaseResponse get(@PathVariable String id) {
        return dataBaseService.get(id);
    }

    @GetMapping("/list/{projectId}")
    @PreAuthorize("hasRoleOrAdmin(@role.DATABASE_QUERY_ALL)")
    public List<DataBaseResponse> list(@PathVariable String projectId) {
        return dataBaseService.list(projectId);
    }

    @PostMapping
    @PreAuthorize("hasRoleOrAdmin(@role.DATABASE_CRE_UPD_DEL)")
    public Boolean add(@Validated(InsertGroup.class) @RequestBody DataBaseRequest dataBaseRequest) {
        return dataBaseService.add(dataBaseRequest);
    }

    @PutMapping
    @PreAuthorize("hasRoleOrAdmin(@role.DATABASE_CRE_UPD_DEL)")
    public Boolean edit(@Validated(UpdateGroup.class) @RequestBody DataBaseRequest dataBaseRequest) {
        return dataBaseService.edit(dataBaseRequest);
    }

    @DeleteMapping("/{ids}")
    @PreAuthorize("hasRoleOrAdmin(@role.DATABASE_CRE_UPD_DEL)")
    public Boolean delete(@PathVariable List<String> ids) {
        return dataBaseService.delete(ids);
    }

}
