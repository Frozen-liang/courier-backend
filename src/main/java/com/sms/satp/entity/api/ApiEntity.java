package com.sms.satp.entity.api;

import com.sms.satp.common.enums.ApiProtocol;
import com.sms.satp.common.enums.ApiRequestParamType;
import com.sms.satp.common.enums.ApiStatus;
import com.sms.satp.common.enums.RequestMethod;
import com.sms.satp.entity.api.common.HeaderInfo;
import com.sms.satp.entity.api.common.ParamInfo;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Api")
public class ApiEntity {
//    apiID int(11) unsigned auto_increment comment '接口ID'
//    primary key,
//    apiName varchar(255) not null comment '接口名称',
//    apiURI mediumtext not null comment '接口路径',
//    apiProtocol tinyint(1) unsigned null comment '请求协议类型',
//    apiFailureMock longtext null comment '失败示例',
//    apiSuccessMock longtext null comment '成功示例',
//    apiRequestType tinyint(1) unsigned null comment '请求类型',
//    apiStatus tinyint(1) unsigned default 0 not null comment '接口状态',
//    apiUpdateTime datetime not null comment '接口更新时间',
//    groupID int(11) unsigned not null comment '分组ID',
//    projectID int(11) unsigned not null comment '项目ID',
//    starred tinyint(1) unsigned default 0 not null comment '星标状态',
//    removed tinyint(1) unsigned default 0 not null comment '是否移入回收站',
//    removeTime datetime null comment '移入回收站时间',
//    apiNoteType tinyint(1) unsigned default 0 not null comment '详细说明类型',
//    apiNoteRaw longtext null comment '详细说明markdown内容',
//    apiNote longtext null comment '详细说明富文本内容',
//    apiRequestParamType tinyint unsigned default 0 null comment '请求参数类型(0:FROM-DATA，1:RAW，2:JSON，3:XML，4:Binary)',
//    apiRequestRaw longtext null comment '请求参数源数据',
//    apiRequestBinary text null comment '请求参数二进制数据',
//    updateUserID int unsigned not null comment '更新者ID',
//    mockRule mediumtext null comment 'mock规则',
//    mockResult longtext null comment 'mock结果',
//    mockConfig mediumtext null comment 'mock配置',
//    createTime datetime null comment '创建时间',
//    apiFailureStatusCode varchar(20) default '200' null comment '失败返回状态码',
//    apiSuccessStatusCode varchar(20) default '200' null comment '成功返回状态码',
//    beforeInject longtext null comment '前注入',
//    afterInject longtext null comment '后注入',
//    createUserID int null comment '创建者',
//    authInfo mediumtext null comment '鉴权',
//    isShared tinyint default 1 null comment '是否开启分享，0不开启分享，1开启分享',
//    apiFailureContentType varchar(255) default 'text/html; charset=UTF-8' null comment '失败示例ContentType',
//    apiSuccessContentType varchar(255) default 'text/html; charset=UTF-8' null comment '成功示例ContentType',
//    apiTag varchar(255) null comment '接口标签',
//    swaggerID varchar(255) null comment 'swagger接口唯一标志',
//    apiManagerID int null comment 'API负责人',
//    apiType varchar(20) null comment '接口类型',
//    customInfo mediumtext null comment '自定义信息字段'

    //    "headerInfo": [],
//        "responseHeader": null,
//        "requestInfo": [
//    {
//        "paramKey": "UserName",
//        "paramName": null,
//        "paramNotNull": "0",
//        "paramType": "0",
//        "paramValueList": null
//    },
//    {
//        "paramKey": "Password",
//        "paramName": null,
//        "paramNotNull": "0",
//        "paramType": "0",
//        "paramValueList": null
//    },
//    {
//        "paramKey": "UtcOffset",
//        "paramName": null,
//        "paramNotNull": "1",
//        "paramType": "3",
//        "paramValueList": null
//    },
//    {
//        "paramKey": "IsSupportDST",
//        "paramName": null,
//        "paramNotNull": "1",
//        "paramType": "8",
//        "paramValueList": null
//    }
//    ],
//        "urlParam": [],
//        "restfulParam": [],
//        "resultInfo": [],
//        "mockInfo": {
//        "rule": "",
//            "result": ""
//    },
//        "resultParamType": 0,
//        "resultParamJsonType": 0
    @MongoId(FieldType.OBJECT_ID)
    private String id;

    @Field(targetType = FieldType.OBJECT_ID)
    private String projectId;

    @Field(targetType = FieldType.OBJECT_ID)
    private String groupId;

    private String apiName;

    private String apiUrl;

    private ApiProtocol apiProtocol;

    private RequestMethod requestMethod;

    private ApiRequestParamType apiRequestParamType;

    private List<HeaderInfo> headerInfo;

    private List<ParamInfo> paramInfo;

    private ApiStatus apiStatus;

    private boolean removed;

    private String preInject;

    private String postInject;

    private String swaggerId;
    @CreatedBy
    private Long createUserId;
    @CreatedDate
    private LocalDateTime createTime;
    @LastModifiedDate
    private LocalDateTime modifyTime;


}
