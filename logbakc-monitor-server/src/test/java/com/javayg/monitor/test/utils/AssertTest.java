package com.javayg.monitor.test.utils;

import cn.hutool.core.lang.Assert;
import com.javayg.log.monitor.common.exception.LogParserException;
import org.junit.jupiter.api.Test;

public class AssertTest {
    @Test
    public void test() throws LogParserException {
        int payloadReadLen = 1234567;
        int payloadLen = 1234567;
        Assert.equals(payloadReadLen, payloadLen, () -> new LogParserException("不一样"));
    }
}
