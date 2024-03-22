package com.javayg.log.monitor.common.repository;

import com.javayg.log.monitor.common.entity.UploadFile;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author YangGang
 * @date 2023/2/24
 * @description
 */
@Repository
public interface UploadFileRepository extends BaseRepository<UploadFile, Integer> {
    List<UploadFile> findByFileName(String fileName);
}