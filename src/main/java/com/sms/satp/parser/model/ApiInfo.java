package com.sms.satp.parser.model;

import lombok.Builder;
import lombok.Data;

/**
 * Info { "title": "Sample Pet Store App", "description": "This is a sample server for a pet store.", "termsOfService":
 * "http://example.com/terms/", "contact": { "name": "API Support", "url": "http://www.example.com/support", "email":
 * "support@example.com" }, "version": "1.0.1" }
 */
@Data
@Builder
public class ApiInfo {

    /**
     * REQUIRED. The title of the application.
     */
    private String title;
    /**
     * A short description of the application. CommonMark syntax MAY be used for rich text representation.
     */
    private String description;
    /**
     * The contact information for the exposed API.
     */
    private ApiContact contact;
    /**
     * REQUIRED. The version of the OpenAPI document (which is distinct from the OpenAPI Specification version or the
     * API implementation version).
     */
    private String version;
}
