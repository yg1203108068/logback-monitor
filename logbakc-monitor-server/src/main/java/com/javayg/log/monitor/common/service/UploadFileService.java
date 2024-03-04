package com.javayg.log.monitor.common.service;

import cn.hutool.core.io.FileUtil;
import com.javayg.log.monitor.common.entity.UploadFile;
import com.javayg.log.monitor.common.exception.ApplicationException;
import com.javayg.log.monitor.common.repository.UploadFileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * @author YangGang
 * @date 2023/2/24
 * @description
 */
@Slf4j
@Service
public class UploadFileService extends BaseService<UploadFile, Integer> {
    @Autowired
    UploadFileRepository uploadFileRepository;
    @Value("${uploadDir}")
    private String uploadDir;

    public Resource loadAsResource(String filename) {
        List<UploadFile> uploadFile = uploadFileRepository.findByFileName(filename);
        if (uploadFile == null || uploadFile.size() == 0) {
            throw new ApplicationException("文件不存在");
        }
        File file = new File(uploadDir + "/" + filename);
        if (!file.exists()) {
            throw new ApplicationException("文件不存在");
        }
        return new FileSystemResource(file);
    }

    public UploadFile store(MultipartFile uploadFile) throws IOException {
        int lastDotIndex = uploadFile.getOriginalFilename().lastIndexOf(".");
        String suffix = uploadFile.getOriginalFilename().substring(lastDotIndex);
        log.info("上传文件参数名：{}", uploadFile.getName());
        log.info("源文件名：{}", uploadFile.getOriginalFilename());
        log.info("后缀：{}", suffix);
        String tFileName = String.format("%d_%s", System.currentTimeMillis(), UUID.randomUUID());
        log.info("上传文件地址={}/{}", uploadDir, uploadDir + "/" + tFileName + suffix);
        File file = new File(uploadDir + "/" + tFileName + suffix);
        while (file.exists()) {
            tFileName = String.format("%d_%s", System.currentTimeMillis(), UUID.randomUUID());
            file = new File(uploadDir + "/" + tFileName + suffix);
        }
        log.info("文件不存在={}", file.getAbsolutePath());
        FileUtil.writeFromStream(uploadFile.getInputStream(), file);
        UploadFile modal = new UploadFile();
        modal.setFileName(tFileName + suffix);
        modal.setFilePath(uploadDir + "/" + tFileName + suffix);
        modal.setOriginalFilename(uploadFile.getOriginalFilename());
        return uploadFileRepository.save(modal);
    }
}
