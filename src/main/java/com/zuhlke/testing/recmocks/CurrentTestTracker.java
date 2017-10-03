package com.zuhlke.testing.recmocks;

import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

import java.awt.*;

class CurrentTestTracker {
    private final TestClass testClass;
    private Context defaultCtx;
    private Context currentCtx;

    public CurrentTestTracker(TestClass testClass) {
        this.testClass = testClass;
        this.defaultCtx = new Context(testClass.getJavaClass(), null);
        this.currentCtx = defaultCtx;
    }

    /**
     * Invoked before the @Begin methods
     * @param method
     */
    public void testStarted(FrameworkMethod method) {
        currentCtx = new Context(testClass.getJavaClass(), method.getName());
    }

    /**
     * Invoked after the @After methods
     * @param method
     */
    public void testFinished(FrameworkMethod method) {

    }

    Context getCurrentContext() {
        return currentCtx;
    }
}
