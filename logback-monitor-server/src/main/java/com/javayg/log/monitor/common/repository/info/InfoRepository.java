package com.javayg.log.monitor.common.repository.info;

import com.javayg.log.monitor.common.entity.Info;
import com.javayg.log.monitor.common.repository.BaseRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 信息表
 */
@Repository
public interface InfoRepository extends BaseRepository<Info, Integer> {
    List<Info> findByCustomerNameLike(@NonNull String customerName);
}