package com.zuhlke.testing.recmocks;

import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;

import java.awt.*;

class CurrentTestTracker extends RunListener {
    private Context currentCtx = new Context(null, null);

    @Override
    public void testStarted(Description description) throws Exception {
        currentCtx = new Context(description.getTestClass(), description.getMethodName());
        super.testStarted(description);
    }

    @Override
    public void testFinished(Description description) throws Exception {
        currentCtx = new Context(null, null);
        super.testFinished(description);
    }

    Context getCurrentContext() {
        return currentCtx;
    }
}
