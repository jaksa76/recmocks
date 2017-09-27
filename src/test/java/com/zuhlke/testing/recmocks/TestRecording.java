package com.zuhlke.testing.recmocks;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RecMocks.class)
public class TestRecording {

    @Test
    public void testRecording() throws Exception {
        List<String> list = RecMocks.recmock(new ArrayList<String>());

        list.add("A");
        list.add("B");
        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));

        File file = new File("recmocks/traces/com/zuhlke/testing/recmocks/TestRecording.testRecording.ArrayList.1");
        assertTrue(file.exists());
    }
}
