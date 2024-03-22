package com.javayg.log.monitor.common.utils;

import com.javayg.log.monitor.common.exception.ApplicationException;

import java.util.Optional;

/**
 * @author 杨港
 * @date 2023/2/23
 * @description
 */
public class OptionalUtils {
    public static void checkEmpty(Optional obj, String emptyMessage) {
        if (obj.isEmpty()) {
            throw new ApplicationException(emptyMessage);
        }
    }
}
