package com.sms.courier.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.MediaType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExportExcelResponse {

    private String filename;
    private String contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
    private Workbook workbook;
}
