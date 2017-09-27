package com.zuhlke.testing.recmocks;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(RecMocks.class)
public class ReplayTestSubclass1 extends ReusableAbstractReplayTest {
    @Test
    public void checkTraces() throws Exception {
        assertEquals("Bob", name);
    }
}
