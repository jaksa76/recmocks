package com.zuhlke.testing.recmocks;

import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;

public class CurrentTestTracker extends RunListener {
    private String currentTestClass;
    private String currentTestMethod;

    @Override
    public void testStarted(Description description) throws Exception {
        System.out.println("CurrentTestTracker.testStarted");

        Package aPackage = description.getTestClass().getPackage();
        String packageName = aPackage.getName();
        String className = description.getTestClass().getSimpleName();
        currentTestClass = packageName + "." + className;

        currentTestMethod = description.getMethodName();

        super.testStarted(description);
    }

    @Override
    public void testFinished(Description description) throws Exception {
        currentTestClass = "";
        currentTestMethod = "";
        super.testFinished(description);
    }

    public String getCurrentTestClass() {
        return currentTestClass;
    }

    public String getCurrentTestMethod() {
        return currentTestMethod;
    }
}
