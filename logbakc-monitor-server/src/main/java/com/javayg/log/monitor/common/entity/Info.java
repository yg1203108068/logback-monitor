package com.javayg.log.monitor.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.javayg.log.monitor.common.entity.system.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
@Table(name = "info")
public class Info {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "project_name", nullable = false)
    private String projectName;

    /**
     * 0: 在谈
     * <p>
     * 1: 已签约
     */
    @Column(name = "status")
    private Integer status;

    @Column(name = "create_time")
    @CreatedDate
    private Date createTime;

    @Column(name = "create_by")
    @CreatedBy
    private String createBy;


    @Column(name = "update_time")
    @LastModifiedDate
    private Date updateTime;

    @Column(name = "update_by")
    @LastModifiedBy
    private String updateBy;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "info_accessory",
            joinColumns = @JoinColumn(name = "info_id"),
            inverseJoinColumns = @JoinColumn(name = "file_id"))
    private Set<UploadFile> uploadFile = new LinkedHashSet<>();

    @Column(name = "del", nullable = false)
    private Boolean del = false;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "create_by", referencedColumnName = "username", insertable = false, updatable = false)
    private User submitter;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "update_by", referencedColumnName = "username", insertable = false, updatable = false)
    private User updateUser;

}