package com.foilen.lanspeedtest;

import org.junit.Assert;
import org.junit.Test;

public class CheckSpeedTest {

    @Test
    public void testCalculateSpeed() {
        Assert.assertEquals(1606.4, CheckSpeed.calculateSpeed(0, 1000), 0.1);
        Assert.assertEquals(803.2, CheckSpeed.calculateSpeed(0, 2000), 0.1);
    }

}
