package com.javayg.log.monitor.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Where(clause = "del = false")
@Table(name = "file")
public class UploadFile {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @CreatedDate
    @Column(name = "create_time", nullable = false)
    private Date createTime;

    @CreatedBy
    @Column(name = "create_by", nullable = false)
    private String createBy;

    @LastModifiedDate
    @Column(name = "update_time")
    private Date updateTime;

    @LastModifiedBy
    @Column(name = "update_by")
    private String updateBy;


    @Column(name = "original_filename", nullable = false)
    private String originalFilename;

    @Column(name = "del", nullable = false)
    private Boolean del = false;

}