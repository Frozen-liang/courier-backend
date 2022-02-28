package com.sms.courier.service;

import com.sms.courier.dto.request.TestFileRequest;
import com.sms.courier.dto.response.FileInfoResponse;
import com.sms.courier.storagestrategy.model.DownloadModel;
import java.util.List;
import java.util.Map;

public interface FileService {

    Map<String, String> getAllRegion();

    List<FileInfoResponse> list(String projectId);

    String insertTestFile(TestFileRequest testFileRequest);

    Boolean updateTestFile(TestFileRequest testFileRequest);

    Boolean deleteTestFileById(String id);

    DownloadModel downloadTestFile(String id);

    FileInfoResponse findById(String id);
}
