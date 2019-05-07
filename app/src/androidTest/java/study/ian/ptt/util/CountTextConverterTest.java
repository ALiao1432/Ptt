package study.ian.ptt.util;

import org.junit.Assert;
import org.junit.Test;

public class CountTextConverterTest {

    @Test
    public void getUserCountTextTest() {
        String result = CountTextConverter.getUserCountText("66");
        Assert.assertEquals("66", result);
    }
}
