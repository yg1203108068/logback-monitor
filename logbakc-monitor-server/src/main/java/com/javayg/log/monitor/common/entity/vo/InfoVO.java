package com.javayg.log.monitor.common.entity.vo;

import com.javayg.log.monitor.common.entity.Info;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 杨港
 * @date 2023/2/25
 * @description
 */
@Data
public class InfoVO {
    private Integer id;
    private String customerName;
    private String projectName;
    private Integer status;
    private String submitter;
    private Set<FileVO> uploadFile;
    private String createBy;
    private String updateBy;

    public InfoVO(Info info) {
        this.id = info.getId();
        this.customerName = info.getCustomerName();
        this.projectName = info.getProjectName();
        this.status = info.getStatus();
        this.submitter = info.getSubmitter().getNickname();
        this.uploadFile = info.getUploadFile().parallelStream().map(FileVO::new).collect(Collectors.toSet());
        this.createBy = info.getSubmitter().getNickname();
        if (info.getUpdateUser() != null) {
            this.updateBy = info.getUpdateUser().getNickname();
        }
    }
}
