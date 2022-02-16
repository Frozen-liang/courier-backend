package com.sms.courier.generator.pojo;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StringFiles {

    private String filePackageName;

    private List<StringFile> fileList;

}
