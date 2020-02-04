package com.sms.satp.parser.model;

import lombok.Builder;
import lombok.Data;

/**
 * Contact { "name": "API Support", "url": "http://www.example.com/support", "email": "support@example.com" }
 */
@Data
@Builder
public class ApiContact {

    /**
     * The identifying name of the contact person/organization.
     */
    private String name;
    /**
     * The URL pointing to the contact information. MUST be in the format of a URL.
     */
    private String url;
    /**
     * The email address of the contact person/organization. MUST be in the format of an email address.
     */
    private String email;
}
