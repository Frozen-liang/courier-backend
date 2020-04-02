package com.sms.satp.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Parameter {

    private String id;
    /**
     * REQUIRED. The name of the parameter. Parameter names are case sensitive. If in is "path", the
     * name field MUST correspond to the associated path segment from the path field in the Paths
     * Object. See Path Templating for further information. If in is "header" and the name field is
     * "Accept", "Content-Type" or "Authorization", the parameter definition SHALL be ignored. For
     * all other cases, the name corresponds to the parameter name used by the in property.
     */
    private String name;

    private Schema schema;

    /**
     * A brief description of the parameter. This could contain examples of use. CommonMark syntax
     * MAY be used for rich text representation.
     */
    private String description;
    /**
     * Determines whether this parameter is mandatory. If the parameter location is "path", this
     * property is REQUIRED and its value MUST be true. Otherwise, the property MAY be included and
     * its default value is false.
     */
    private Boolean required;
    /**
     * Specifies that a parameter is deprecated and SHOULD be transitioned out of usage.
     */
    private Boolean deprecated;
    /**
     * a string value can contain the example with escaping where necessary.
     */
    private String example;
}
