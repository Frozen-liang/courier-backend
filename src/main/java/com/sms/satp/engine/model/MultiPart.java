package com.sms.satp.engine.model;

import java.io.File;

import java.nio.charset.Charset;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class MultiPart {
    private File file;
    private String controlName;
    private String mimeType;
    private Charset charset;
}
