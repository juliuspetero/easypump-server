package com.easypump.controller.aws;

import com.easypump.controller.BaseController;
import com.easypump.dto.aws.S3DocumentDto;
import com.easypump.model.common.ActionTypeEnum;
import com.easypump.model.common.EntityTypeEnum;
import com.easypump.model.common.RecordHolder;
import com.easypump.model.user.UserActivityLog;
import com.easypump.service.aws.handler.AwsDocumentHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/v1/document")
public class AwsDocumentController extends BaseController {

    private final AwsDocumentHandler awsDocumentHandler;

    @Autowired
    public AwsDocumentController(AwsDocumentHandler awsDocumentHandler) {
        this.awsDocumentHandler = awsDocumentHandler;
    }

    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public RecordHolder<S3DocumentDto> uploadFilesToAws(@RequestParam("documents") MultipartFile[] multipartFiles, @RequestParam("uploadType") String uploadType) {
        return executeAndLogUserActivity(EntityTypeEnum.FILE, ActionTypeEnum.DOWNLOAD, (UserActivityLog log) -> awsDocumentHandler.uploadDocuments(multipartFiles, uploadType));
    }

    @RequestMapping(path = "/download/{id}", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadFile(@PathVariable Integer id) {
        return executeHttpGet(() -> awsDocumentHandler.downloadDocument(id));
    }

}
