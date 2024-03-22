package com.javayg.log.monitor.common.entity.system;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "t_role")
public class Role {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "create_time")
    private Instant createTime;

    @Column(name = "update_time")
    private Instant updateTime;

    @ManyToMany(mappedBy = "roles")
    @JsonBackReference
    private Set<User> users = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "roles")
    private Set<Menu> menus = new LinkedHashSet<>();

    @Column(name = "code")
    private String code;

}