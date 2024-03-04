package com.javayg.log.monitor.common.entity.vo;

import com.javayg.log.monitor.common.entity.UploadFile;
import lombok.Data;

/**
 * @author 杨港
 * @date 2023/2/25
 * @description
 */
@Data
public class FileVO {
    /*
    uid: uid
    name: "7280659bgy1fgv7c3gkieg20bi06d4qr.gif"
     */
    private Integer id;
    private String uid;
    private String name;
    private String originalFilename;
    private String fileName;

    public FileVO(UploadFile file) {
        this.id = file.getId();
        this.uid = String.format("rc-%d", file.getId());
        this.name = file.getOriginalFilename();
        this.originalFilename = file.getOriginalFilename();
        this.fileName = file.getFileName();
    }
}
