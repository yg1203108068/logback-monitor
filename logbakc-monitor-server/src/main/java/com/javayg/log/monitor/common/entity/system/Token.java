package com.javayg.log.monitor.common.entity.system;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Accessors(chain = true)
@Entity
@Table(name = "t_token")
public class Token {
    @Id
    @Column(name = "token", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @JsonBackReference
    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @CreatedDate
    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "expiration", nullable = false)
    private Instant expiration;

}