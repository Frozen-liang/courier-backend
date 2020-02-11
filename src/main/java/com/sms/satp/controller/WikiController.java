package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.common.response.Response;
import com.sms.satp.entity.dto.PageDto;
import com.sms.satp.entity.dto.WikiDto;
import com.sms.satp.service.WikiService;
import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.WIKI_PATH)
public class WikiController {

    private final WikiService wikiService;

    public WikiController(WikiService wikiService) {
        this.wikiService = wikiService;
    }

    @GetMapping("/page/{projectId}")
    public Response<Page<WikiDto>> page(PageDto pageDto, @PathVariable String projectId) {
        return Response.ok(wikiService.page(pageDto, projectId));
    }

    @PostMapping()
    public Response add(@Valid @RequestBody WikiDto wikiDto) {
        wikiService.add(wikiDto);
        return Response.ok().build();
    }

    @PutMapping()
    public Response edit(@Valid @RequestBody WikiDto wikiDto) {
        wikiService.edit(wikiDto);
        return Response.ok().build();
    }

    @DeleteMapping("/{ids}")
    public Response delete(@PathVariable String[] ids) {
        for (String id : ids) {
            wikiService.deleteById(id);
        }
        return Response.ok().build();
    }
}