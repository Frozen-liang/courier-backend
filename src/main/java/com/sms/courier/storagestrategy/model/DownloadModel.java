package com.sms.courier.storagestrategy.model;

import java.io.InputStream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DownloadModel {

    private String filename;
    private InputStream inputStream;
    @Default
    private String contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
}
