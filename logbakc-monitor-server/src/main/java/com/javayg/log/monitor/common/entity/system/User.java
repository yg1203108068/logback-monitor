package com.javayg.log.monitor.common.entity.system;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Where(clause = "del = false")
@Entity
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
@Table(name = "t_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "username")
    private String username;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "last_login_time")
    private Instant lastLoginTime;

    /**
     * 账户有效性
     * <p>
     * 1 有效
     * <p>
     * 0 无效
     */
    @Column(name = "valid", nullable = false)
    private Boolean valid = true;

    @JsonIgnore
    @Column(name = "create_time", nullable = false)
    @CreatedDate
    private Date createTime;

    @JsonIgnore
    @CreatedBy
    @Column(name = "create_by", nullable = false)
    private String createBy;

    @JsonIgnore
    @LastModifiedDate
    @Column(name = "update_time")
    private Date updateTime;

    @JsonIgnore
    @LastModifiedBy
    @Column(name = "update_by")
    private String updateBy;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "t_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

    @Column(name = "locked", nullable = false)
    private Boolean locked = false;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserLock> userLock;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Token> tokens;

    @JsonIgnore
    @Column(name = "public_key")
    private String publicKey;

    @JsonIgnore
    @Column(name = "private_key")
    private String privateKey;

    @Column(name = "del", nullable = false)
    private Boolean del = false;

}