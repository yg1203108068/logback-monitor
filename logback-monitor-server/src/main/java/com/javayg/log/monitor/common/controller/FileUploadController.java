package com.javayg.log.monitor.common.controller;

import com.javayg.log.monitor.common.entity.UploadFile;
import com.javayg.log.monitor.common.entity.vo.RestApi;
import com.javayg.log.monitor.common.service.UploadFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author YangGang
 * @date 2023/2/24
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class FileUploadController {
    private final UploadFileService uploadFileService;

    @Autowired
    public FileUploadController(UploadFileService uploadFileService) {
        this.uploadFileService = uploadFileService;
    }

    @GetMapping("/file")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@RequestParam String filename) {
        log.info("文件名={}", filename);
        Resource file = uploadFileService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/upload")
    public RestApi<UploadFile> handleFileUpload(@RequestParam("file") MultipartFile file) {
        UploadFile store;
        try {
            store = uploadFileService.store(file);
        } catch (IOException e) {
            e.printStackTrace();
            return RestApi.fail("上传文件失败");
        }
        return RestApi.success(store);
    }


}
